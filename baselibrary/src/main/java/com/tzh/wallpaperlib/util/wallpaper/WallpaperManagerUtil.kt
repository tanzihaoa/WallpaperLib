package com.tzh.wallpaperlib.util.wallpaper

import android.Manifest
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tzh.wallpaperlib.dialog.VolumeDialog
import com.tzh.wallpaperlib.service.VideoWallpaper
import com.tzh.wallpaperlib.util.BitmapUtil
import com.tzh.wallpaperlib.util.download.DownloadType
import com.tzh.wallpaperlib.util.download.FileDownloadUtil
import com.tzh.wallpaperlib.util.OnPermissionCallBackListener
import com.tzh.wallpaperlib.util.PermissionXUtil
import java.io.File
import java.io.IOException

object WallpaperManagerUtil {
    /**
     * 设置壁纸
     */
    fun setWallpaper(context: AppCompatActivity, url : String){
        PermissionXUtil.requestAnyPermission(context, mutableListOf<String>().apply {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        },object : OnPermissionCallBackListener {
            override fun onAgree() {
                val manager = context.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
                BitmapUtil.urlToBitmap(context,url,object : BitmapUtil.BitmapListener{
                    override fun sure(bitmap: Bitmap) {
                        manager.setBitmap(bitmap)
                        Toast.makeText(context,"设置成功",Toast.LENGTH_SHORT).show()
                    }
                })
            }

            override fun onDisAgree() {

            }
        })
    }

    /**
     * 设置壁纸
     */
    fun setWallpaper(fragment : Fragment, url : String){
        PermissionXUtil.requestAnyPermission(fragment, mutableListOf<String>().apply {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        },object : OnPermissionCallBackListener {
            override fun onAgree() {
                val manager = fragment.activity?.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
                fragment.context?.let {
                    BitmapUtil.urlToBitmap(it,url,object : BitmapUtil.BitmapListener{
                        override fun sure(bitmap: Bitmap) {
                            manager.setBitmap(bitmap)
                            Toast.makeText(it,"设置成功",Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onDisAgree() {

            }
        })
    }

    /**
     * 设置视频壁纸
     * @param url 视频链接
     * @param isVolume 是否有声音
     */
    fun setVideoWallpaper(fragment : Fragment, url : String,isVolume : Boolean = true){
        fragment.context?.apply {
            PermissionXUtil.requestAnyPermission(fragment, mutableListOf<String>().apply {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            },object : OnPermissionCallBackListener {
                override fun onAgree() {
                    val fileDownloadUtil = FileDownloadUtil(this@apply,DownloadType.MP4)
                    if(fileDownloadUtil.isHaveFile(url)){
                        Log.e("setVideoWallpaper=====1",fileDownloadUtil.getPath(url))
                        VideoWallpaper.setToWallPaper(this@apply,fileDownloadUtil.getPath(url),isVolume)
                    }else{
                        fileDownloadUtil.onDownloadFile(url,object : FileDownloadUtil.OnDownloadListener(){
                            override fun onSuccess(file: File) {
                                Log.e("setVideoWallpaper=====2",fileDownloadUtil.getPath(url))
                                VideoWallpaper.setToWallPaper(this@apply,fileDownloadUtil.getPath(url),isVolume)
                            }

                            override fun onError(throwable: Throwable) {

                            }
                        })
                    }
                }

                override fun onDisAgree() {

                }
            })
        }
    }

    /**
     * 设置视频壁纸
     * @param url 视频链接
     * @param isVolume 是否有声音
     */
    fun setVideoWallpaperDialog(fragment : Fragment, url : String){
        fragment.context?.let {
            VolumeDialog(it,object : VolumeDialog.VolumeListener{
                override fun volume(volume: Boolean) {
                    PermissionXUtil.requestAnyPermission(fragment, mutableListOf<String>().apply {
                        add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    },object : OnPermissionCallBackListener {
                        override fun onAgree() {
                            val fileDownloadUtil = FileDownloadUtil(it,DownloadType.MP4)
                            if(fileDownloadUtil.isHaveFile(url)){
                                VideoWallpaper.setToWallPaper(it,fileDownloadUtil.getPath(url),volume)
                            }else{
                                fileDownloadUtil.onDownloadFile(url,object : FileDownloadUtil.OnDownloadListener(){
                                    override fun onSuccess(file: File) {
                                        VideoWallpaper.setToWallPaper(it,fileDownloadUtil.getPath(url),volume)
                                    }

                                    override fun onError(throwable: Throwable) {

                                    }
                                })
                            }
                        }

                        override fun onDisAgree() {

                        }
                    })
                }
            }).show()
        }
    }

    /**
     * 设置视频壁纸
     * @param url 视频链接
     * @param isVolume 是否有声音
     */
    fun setVideoWallpaper(activity : AppCompatActivity, url : String,isVolume : Boolean = true){
        PermissionXUtil.requestAnyPermission(activity, mutableListOf<String>().apply {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        },object : OnPermissionCallBackListener {
            override fun onAgree() {
                val fileDownloadUtil = FileDownloadUtil(activity,DownloadType.MP4)
                if(fileDownloadUtil.isHaveFile(url)){
                    VideoWallpaper.setToWallPaper(activity,fileDownloadUtil.getPath(url),isVolume)
                }else{
                    fileDownloadUtil.onDownloadFile(url,object : FileDownloadUtil.OnDownloadListener(){
                        override fun onSuccess(file: File) {
                            VideoWallpaper.setToWallPaper(activity,fileDownloadUtil.getPath(url),isVolume)
                        }

                        override fun onError(throwable: Throwable) {

                        }
                    })
                }
            }

            override fun onDisAgree() {

            }
        })
    }

    /**
     * 设置视频壁纸
     * @param url 视频链接
     * @param isVolume 是否有声音
     */
    fun setVideoWallpaperDialog(activity : AppCompatActivity, url : String){
        VolumeDialog(activity,object : VolumeDialog.VolumeListener{
            override fun volume(volume: Boolean) {
                PermissionXUtil.requestAnyPermission(activity, mutableListOf<String>().apply {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                },object : OnPermissionCallBackListener {
                    override fun onAgree() {
                        val fileDownloadUtil = FileDownloadUtil(activity,DownloadType.MP4)
                        if(fileDownloadUtil.isHaveFile(url)){
                            VideoWallpaper.setToWallPaper(activity,fileDownloadUtil.getPath(url),volume)
                        }else{
                            fileDownloadUtil.onDownloadFile(url,object : FileDownloadUtil.OnDownloadListener(){
                                override fun onSuccess(file: File) {
                                    VideoWallpaper.setToWallPaper(activity,fileDownloadUtil.getPath(url),volume)
                                }

                                override fun onError(throwable: Throwable) {

                                }
                            })
                        }
                    }

                    override fun onDisAgree() {

                    }
                })
            }
        }).show()
    }

    private fun setLocalVideoWallpaper(context : Context, url : String,isVolume : Boolean = true){
        VideoWallpaper.sVideoPath = url
        VideoWallpaper.isVolume = isVolume
        try {
            context.clearWallpaper()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
        intent.putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(context, VideoWallpaper::class.java)
        )
        context.startActivity(intent)
    }
}