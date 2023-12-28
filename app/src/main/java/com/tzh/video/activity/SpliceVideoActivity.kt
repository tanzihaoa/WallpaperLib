package com.tzh.video.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.tzh.mylibrary.util.GsonUtil
import com.tzh.mylibrary.util.toDefault
import com.tzh.video.R
import com.tzh.video.base.AppBaseActivity
import com.tzh.video.databinding.ActivitySpliceVideoBinding
import com.tzh.video.dialog.VideoHintDialog
import com.tzh.video.util.ToastUtil
import com.tzh.video.util.img.GlideEngine
import com.tzh.video.util.img.ImgUtil
import com.tzh.wallpaper.util.OnPermissionCallBackListener
import com.tzh.wallpaper.util.PermissionXUtil
import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Random


class SpliceVideoActivity : AppBaseActivity<ActivitySpliceVideoBinding>(R.layout.activity_splice_video) {

    companion object{
        fun start(activity : AppCompatActivity){
            PermissionXUtil.requestAnyPermission(activity, mutableListOf<String>().apply {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            },object : OnPermissionCallBackListener {
                override fun onAgree() {
                    activity.startActivity(Intent(activity,SpliceVideoActivity::class.java))
                }

                override fun onDisAgree() {

                }
            })
        }
    }

    val mDialog by lazy {
        VideoHintDialog(this)
    }

    /**
     * 选择的片头
     */
    val headList = ArrayList<Photo?>()

    /**
     * 选择的固定片段
     */
    val bodyList = ArrayList<Photo?>()

    /**
     * 选择的内容
     */
    val contentList = ArrayList<Photo?>()

    /**
     * 固定需要添加的内容
     */
    val immobilizationList = ArrayList<Photo?>()

    override fun initView() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding.activity = this
        //选择开头
        binding.tvSelectHead.setOnClickListener {
            selectVideo(1)
        }
        //选择固定部分
        binding.tvSelectBody.setOnClickListener {
            selectVideo(2)
        }
        //选择内容部分
        binding.tvSelectContent.setOnClickListener {
            selectVideo(3)
        }

        //选择固定视频部分
        binding.tvSelectImmobilization.setOnClickListener {
            selectVideo(4)
        }
    }

    /**
     * 选择视频
     * @param type 1选择头部 2选择固定片段 3选择内容
     */
    fun selectVideo(type : Int){
        EasyPhotos.createAlbum(this, false, false, GlideEngine.getInstance())
            .setFileProviderAuthority("com.tzh.video.fileprovider")
            .setSelectedPhotos(if(type == 1) headList else if(type == 2) bodyList else if(type == 3) contentList  else immobilizationList)
            .complexSelector(true, 99, 99)
            .start(object : SelectCallback() {
                override fun onResult(photos: ArrayList<Photo>, isOriginal: Boolean) {
                    when (type){
                        1->{
                            headList.clear()
                            headList.addAll(photos)
                            compute()
                        }
                        2->{
                            bodyList.clear()
                            bodyList.addAll(photos)
                            compute()
                        }
                        3->{
                            contentList.clear()
                            contentList.addAll(photos)
                            compute()
                        }
                        4->{
                            immobilizationList.clear()
                            immobilizationList.addAll(photos)
                            compute()
                        }
                    }
                }

                override fun onCancel() {

                }
            })
    }

    override fun initData() {

    }

    val videoList = mutableListOf<ArrayList<String>>()

    fun compute(){
        binding.tvSelectHead.text = if(headList.size == 0) "选择视频开头" else "选择视频开头(${headList.size})"
        binding.tvSelectBody.text = if(bodyList.size == 0) "选择视频中间固定片段" else "选择视频中间固定片段(${bodyList.size})"
        binding.tvSelectContent.text = if(contentList.size == 0) "选择视频内容" else "选择视频内容(${contentList.size})"
        binding.tvSelectImmobilization.text = if(immobilizationList.size == 0) "选择固定视频内容" else "选择固定视频内容(${immobilizationList.size})"
        if(headList.size > 0 && bodyList.size > 0 && contentList.size > 0 && immobilizationList.size > 0){
            val num = binding.etNum.text.toString().toInt()
            if(bodyList.size - 1 >= num && contentList.size >= num){
                videoList.clear()
                val list1 = combine(contentList,num)
                val list2 = addImmobilization(list1)
                for(head in headList){
                    for(cList in list2){
                        val mList = ArrayList<String>()
                        mList.add(head?.path.toDefault(""))
                        for((index,url) in cList.withIndex()){
                            mList.add(bodyList[index]?.path.toDefault(""))
                            mList.add(url?.path.toDefault(""))
                        }
                        Log.e("====",GsonUtil.GsonString(mList))
                        videoList.add(mList)
                    }
                }
                binding.tvNum.text = "可以生成${videoList.size}个视频"
            }else{
                binding.tvNum.text = "可以生成0个视频"
            }
        }else{
            binding.tvNum.text = "可以生成0个视频"
        }
    }

    /**
     * 当前生成下标
     */
    var index = 0
    @SuppressLint("AutoDispose")
    fun startVideo(isNew : Boolean = false){
        if(isNew){
            index = 0
            mDialog.show()
        }else{
            index += 1
        }

        if(headList.size == 0){
            ToastUtil.show("请选择片头")
        }else if(bodyList.size == 0){
            ToastUtil.show("请选择固定视频")
        }else if(contentList.size == 0){
            ToastUtil.show("请选择视频")
        }else if(immobilizationList.size == 0){
            ToastUtil.show("请选择固定视频")
        }else if(videoList.size == 0){
            ToastUtil.show("暂无可生成视频")
        }else{
            if(index < videoList.size){
                val list = videoList[index]
                //开始执行FFmpeg命令
                RxFFmpegInvoke.getInstance().runCommandRxJava(ImgUtil.getBoxblur(list))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(MyRxFFmpegSubscriber(object : MyRxFFmpegSubscriber.VideoListener{
                        override fun finish() {
                            startVideo()
                        }

                        override fun progress(progress: Int, progressTime: Long) {
                            mDialog.setText("合成视频进行中(${index+1}/${videoList.size})")
                        }

                        override fun error() {
                            startVideo()
                        }
                    }))
            }else{
                mDialog.setText("视频合成完成")
                ToastUtil.show("视频合成完成")
            }
        }
    }

    class MyRxFFmpegSubscriber(val listener : VideoListener) : RxFFmpegSubscriber() {

        override fun onFinish() {
            listener.finish()
        }

        override fun onProgress(progress: Int, progressTime: Long) {
            listener.progress(progress,progressTime)
        }

        override fun onCancel() {

        }

        override fun onError(message: String) {
            listener.error()
        }

        interface VideoListener{
            fun finish()

            fun progress(progress: Int, progressTime: Long)

            fun error()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    fun combine(array: MutableList<Photo?>, length: Int): MutableList<Array<Photo?>> {
        val result = mutableListOf<Array<Photo?>>()
        if (length == 1) {
            // 当要求组合长度为 1 时，直接将每个元素与一个空数组组合
            for (item in array) {
                result.add(arrayOf(item))
            }
        } else if (length > 1) {
            // 当要求组合长度大于 1 时，递归地组合更短的数组
            for (i in 0 until array.size) {
                val subList = mutableListOf<Photo?>()
                subList.addAll(array)
                subList.removeAt(i)
                val subResult = combine(subList, length - 1)
                for (subArray in subResult) {
                    val newElement = arrayOf(array[i], *subArray)
                    result.add(newElement)
                }
            }
        }
        return result
    }

    /**
     * 添加固定视频
     */
    fun addImmobilization(list: MutableList<Array<Photo?>>) : MutableList<Array<Photo?>>{
        return if(immobilizationList.size > 0){
            for((i,sList) in list.withIndex()){
                //获取随机的固定视频
                val index = Random().nextInt(immobilizationList.size)
                val immobilization = immobilizationList[index]

                //获取添加到的随机位置
                val index2 = Random().nextInt(sList.size+1)
                list[i] = addArrayInIndex(sList,immobilization!!,index2)
                Log.e("====gg===",GsonUtil.GsonString(list[i]))
            }

            list
        }else{
            list
        }
    }

    fun addArrayInIndex(array : Array<Photo?>,dto : Photo,index : Int) : Array<Photo?> {
        val numbers = arrayOfNulls<Photo>(array.size+1)
        // 将原数组的元素拷贝到新数组中，并将新元素插入到指定位置
        for (i in numbers.indices) {
            if (i == index) {
                numbers[i] = dto
            } else if (i < index) {
                numbers[i] = array[i]
            } else {
                numbers[i] = array[i - 1]
            }
        }

        return numbers
    }
}