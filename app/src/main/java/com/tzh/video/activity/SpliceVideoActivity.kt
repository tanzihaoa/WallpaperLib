package com.tzh.video.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.tzh.mylibrary.util.GsonUtil
import com.tzh.video.R
import com.tzh.video.base.AppBaseActivity
import com.tzh.video.databinding.ActivitySpliceVideoBinding
import com.tzh.video.util.ToastUtil
import com.tzh.video.util.img.CameraUtil
import com.tzh.video.util.img.GlideEngine
import com.tzh.video.util.img.ImgUtil
import com.tzh.wallpaper.util.OnPermissionCallBackListener
import com.tzh.wallpaper.util.PermissionXUtil
import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.math.ceil


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

    /**
     * 选择的片头
     */
    val headList = ArrayList<Photo>()

    /**
     * 选择的固定片段
     */
    val bodyList = ArrayList<Photo>()

    /**
     * 选择的内容
     */
    val contentList = ArrayList<Photo>()

    val myRxFFmpegSubscriber by lazy {
        MyRxFFmpegSubscriber()
    }

    override fun initView() {
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
    }

    /**
     * 选择视频
     * @param type 1选择头部 2选择固定片段 3选择内容
     */
    fun selectVideo(type : Int){
        EasyPhotos.createAlbum(this, false, false, GlideEngine.getInstance())
            .setFileProviderAuthority("com.tzh.video.fileprovider")
            .setSelectedPhotos(if(type == 1) headList else if(type == 2) bodyList else contentList)
            .complexSelector(true, 99, 0)
            .onlyVideo() //                .complexSelector(true,1,num>0?num:1)//参数说明：是否只能选择单类型，视频数，图片数。
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
                    }
                }

                override fun onCancel() {

                }
            })
    }

    override fun initData() {

    }

    val videoList = mutableListOf<Photo>()

    fun compute(){
        binding.tvSelectHead.text = if(headList.size == 0) "选择视频开头" else "选择视频开头(${headList.size})"
        binding.tvSelectBody.text = if(bodyList.size == 0) "选择视频中间固定片段" else "选择视频中间固定片段(${bodyList.size})"
        binding.tvSelectContent.text = if(contentList.size == 0) "选择视频内容" else "选择视频内容(${contentList.size})"
        if(headList.size > 0 && bodyList.size > 0 && contentList.size > 0){
            val num = binding.etNum.text.toString().toInt()
            if(bodyList.size >= num && contentList.size >= num){
                val list = combine(contentList,num)
                for(cList in list){
                    val m = CameraUtil.photoToString(cList.toMutableList())
                    Log.e("====",GsonUtil.GsonString(m))
                }
                binding.tvNum.text = "可以生成${list.size}个视频"
            }else{
                binding.tvNum.text = "可以生成0个视频"
            }
        }else{
            binding.tvNum.text = "可以生成0个视频"
        }
    }

    @SuppressLint("AutoDispose")
    fun startVideo(){
        if(headList.isEmpty()){
            ToastUtil.show("请选择片头")
        }else if(bodyList.isEmpty()){
            ToastUtil.show("请选择固定视频")
        }else if(contentList.isEmpty()){
            ToastUtil.show("")
        }
        val list = CameraUtil.photoToString(contentList)
        //开始执行FFmpeg命令
        RxFFmpegInvoke.getInstance()
            .runCommandRxJava(ImgUtil.getBoxblur(list))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(myRxFFmpegSubscriber)
    }

    class MyRxFFmpegSubscriber : RxFFmpegSubscriber() {

        override fun onFinish() {
            Log.e("onProgress=====","完成")
        }

        override fun onProgress(progress: Int, progressTime: Long) {
            Log.e("onProgress=====",progress.toString())
        }

        override fun onCancel() {

        }

        override fun onError(message: String) {
            Log.e("onProgress=====",message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    fun combine(array: MutableList<Photo>, length: Int): MutableList<Array<Photo>> {
        val result = mutableListOf<Array<Photo>>()
        if (length == 1) {
            // 当要求组合长度为 1 时，直接将每个元素与一个空数组组合
            for (item in array) {
                result.add(arrayOf(item))
            }
        } else if (length > 1) {
            // 当要求组合长度大于 1 时，递归地组合更短的数组
            for (i in 0 until array.size) {
                val subList = mutableListOf<Photo>()
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
}