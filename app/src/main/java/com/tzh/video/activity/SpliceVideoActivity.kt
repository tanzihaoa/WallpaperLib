package com.tzh.video.activity

import android.Manifest
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.tzh.video.R
import com.tzh.video.base.AppBaseActivity
import com.tzh.video.databinding.ActivitySpliceVideoBinding
import com.tzh.video.util.img.CameraUtil
import com.tzh.video.util.img.GlideEngine
import com.tzh.video.util.img.ImgUtil
import com.tzh.wallpaper.util.OnPermissionCallBackListener
import com.tzh.wallpaper.util.PermissionXUtil
import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegSubscriber


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

    val myRxFFmpegSubscriber by lazy {
        MyRxFFmpegSubscriber()
    }

    override fun initView() {
        binding.tvSelect.setOnClickListener {
            EasyPhotos.createAlbum(this, false, false, GlideEngine.getInstance())
                .setFileProviderAuthority("com.tzh.video.fileprovider")
                .complexSelector(true, 99, 0)
                .onlyVideo() //                .complexSelector(true,1,num>0?num:1)//参数说明：是否只能选择单类型，视频数，图片数。
                .start(object : SelectCallback() {
                    override fun onResult(photos: ArrayList<Photo>, isOriginal: Boolean) {
                        val list = CameraUtil.photoToString(photos)
                        val outName = "/storage/emulated/0/DCIM/Camera/" + "video_123456789.mp4"
                        //开始执行FFmpeg命令
                        RxFFmpegInvoke.getInstance()
                            .runCommandRxJava(ImgUtil.toList(list,outName))
//                                .runCommandRxJava(command)
                            .subscribe(myRxFFmpegSubscriber)
                    }

                    override fun onCancel() {

                    }
                })
        }
    }

    override fun initData() {

    }

    class MyRxFFmpegSubscriber : RxFFmpegSubscriber() {

        init {

        }

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
}