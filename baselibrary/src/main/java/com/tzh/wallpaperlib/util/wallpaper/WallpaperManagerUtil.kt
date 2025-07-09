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
import com.tzh.wallpaperlib.dialog.BaseWallpaperDialog
import com.tzh.wallpaperlib.dialog.VolumeDialog
import com.tzh.wallpaperlib.service.VideoWallpaper
import com.tzh.wallpaperlib.util.BitmapUtil
import com.tzh.wallpaperlib.util.OnPermissionCallBackListener
import com.tzh.wallpaperlib.util.PermissionXUtil
import com.tzh.wallpaperlib.util.download.DownloadType
import com.tzh.wallpaperlib.util.download.FileDownloadUtil
import java.io.File
import java.io.IOException


object WallpaperManagerUtil {
    /**
     * 设置选择壁纸弹窗
     */
    fun setWallpaperDialog(context: Context, url : String,listener : WallPaperListener ?= null){
        BaseWallpaperDialog(context,object : BaseWallpaperDialog.WallpaperListener{
            override fun wallpaper(type: Int) {
                if(type == 1){
                    setSystemWallpaper(context,url,listener)
                }else{
                    setLockWallpaper(context,url,listener)
                }
            }
        }).show()

    }

    /**
     * 设置壁纸
     */
    fun setWallpaper(context: Context, url : String,listener : WallPaperListener ?= null){
        val manager = context.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
        BitmapUtil.urlToBitmap(context,url,object : BitmapUtil.BitmapListener{
            override fun sure(bitmap: Bitmap) {
                listener?.ok()
                manager.setBitmap(bitmap)
                Toast.makeText(context,"设置成功",Toast.LENGTH_SHORT).show()
            }

            override fun error() {
                listener?.error()
                Toast.makeText(context,"设置失败",Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * 设置桌面壁纸
     */
    fun setSystemWallpaper(context: Context, url : String,listener : WallPaperListener ?= null){
        val manager = context.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
        BitmapUtil.urlToBitmap(context,url,object : BitmapUtil.BitmapListener{
            override fun sure(bitmap: Bitmap) {
                listener?.ok()
                manager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                Toast.makeText(context,"设置成功",Toast.LENGTH_SHORT).show()
            }

            override fun error() {
                listener?.error()
                Toast.makeText(context,"设置失败",Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * 设置锁屏壁纸
     */
    fun setLockWallpaper(context: Context, url : String,listener : WallPaperListener ?= null){
        val manager = context.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
        BitmapUtil.urlToBitmap(context,url,object : BitmapUtil.BitmapListener{
            override fun sure(bitmap: Bitmap) {
                listener?.ok()
                manager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                Toast.makeText(context,"设置成功",Toast.LENGTH_SHORT).show()
            }

            override fun error() {
                listener?.error()
                Toast.makeText(context,"设置失败",Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * 设置视频壁纸
     * @param url 视频链接
     * @param isVolume 是否有声音
     */
    fun setVideoWallpaper(fragment : Fragment, url : String,isVolume : Boolean = true,listener : WallPaperListener ?= null){
        fragment.context?.apply {
            val fileDownloadUtil = FileDownloadUtil(this@apply,DownloadType.MP4)
            if(fileDownloadUtil.isHaveFile(url)){
                listener?.ok()
                Log.e("setVideoWallpaper=====1",fileDownloadUtil.getPath(url))
                VideoWallpaper.setToWallPaper(this@apply,fileDownloadUtil.getPath(url),isVolume)
            }else{
                fileDownloadUtil.onDownloadFile(url,object : FileDownloadUtil.OnDownloadListener(){
                    override fun onSuccess(file: File) {
                        Log.e("setVideoWallpaper=====2",fileDownloadUtil.getPath(url))
                        listener?.ok()
                        VideoWallpaper.setToWallPaper(this@apply,fileDownloadUtil.getPath(url),isVolume)
                    }

                    override fun onError(throwable: Throwable) {
                        listener?.error()
                    }
                })
            }
        }
    }

    /**
     * 设置视频壁纸
     * @param url 视频链接
     */
    fun setVideoWallpaperDialog(fragment : Fragment, url : String,listener : WallPaperListener ?= null){
        fragment.context?.let {
            VolumeDialog(it,object : VolumeDialog.VolumeListener{
                override fun volume(volume: Boolean) {
                    val fileDownloadUtil = FileDownloadUtil(it,DownloadType.MP4)
                    if(fileDownloadUtil.isHaveFile(url)){
                        listener?.ok()
                        VideoWallpaper.setToWallPaper(it,fileDownloadUtil.getPath(url),volume)
                    }else{
                        fileDownloadUtil.onDownloadFile(url,object : FileDownloadUtil.OnDownloadListener(){
                            override fun onSuccess(file: File) {
                                listener?.ok()
                                VideoWallpaper.setToWallPaper(it,fileDownloadUtil.getPath(url),volume)
                            }

                            override fun onError(throwable: Throwable) {
                                listener?.error()
                            }
                        })
                    }
                }
            }).show()
        }
    }

    /**
     * 设置视频壁纸
     * @param url 视频链接
     * @param isVolume 是否有声音
     */
    fun setVideoWallpaper(activity : AppCompatActivity, url : String,isVolume : Boolean = true,listener : WallPaperListener ?= null){
        val fileDownloadUtil = FileDownloadUtil(activity,DownloadType.MP4)
        if(fileDownloadUtil.isHaveFile(url)){
            listener?.ok()
            VideoWallpaper.setToWallPaper(activity,fileDownloadUtil.getPath(url),isVolume)
        }else{
            fileDownloadUtil.onDownloadFile(url,object : FileDownloadUtil.OnDownloadListener(){
                override fun onSuccess(file: File) {
                    listener?.ok()
                    VideoWallpaper.setToWallPaper(activity,fileDownloadUtil.getPath(url),isVolume)
                }

                override fun onError(throwable: Throwable) {
                    listener?.error()
                }
            })
        }
    }

    /**
     * 设置视频壁纸
     * @param url 视频链接
     * @param isVolume 是否有声音
     */
    fun setVideoWallpaperDialog(activity : AppCompatActivity, url : String,listener : WallPaperListener ?= null){
        VolumeDialog(activity,object : VolumeDialog.VolumeListener{
            override fun volume(volume: Boolean) {
                PermissionXUtil.requestAnyPermission(activity, mutableListOf<String>().apply {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                },object : OnPermissionCallBackListener {
                    override fun onAgree() {
                        val fileDownloadUtil = FileDownloadUtil(activity,DownloadType.MP4)
                        if(fileDownloadUtil.isHaveFile(url)){
                            listener?.ok()
                            VideoWallpaper.setToWallPaper(activity,fileDownloadUtil.getPath(url),volume)
                        }else{
                            fileDownloadUtil.onDownloadFile(url,object : FileDownloadUtil.OnDownloadListener(){
                                override fun onSuccess(file: File) {
                                    listener?.ok()
                                    VideoWallpaper.setToWallPaper(activity,fileDownloadUtil.getPath(url),volume)
                                }

                                override fun onError(throwable: Throwable) {
                                    listener?.error()
                                }
                            })
                        }
                    }

                    override fun onDisAgree() {
                        listener?.error()
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

    interface WallPaperListener{
        fun ok()

        fun error()
    }
}