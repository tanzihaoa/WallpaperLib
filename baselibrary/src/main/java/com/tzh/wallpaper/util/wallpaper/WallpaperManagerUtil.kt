package com.tzh.wallpaper.util.wallpaper

import android.Manifest
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tzh.wallpaper.service.VideoWallpaper
import com.tzh.wallpaper.util.BitmapUtil
import com.tzh.wallpaper.util.download.DownloadType
import com.tzh.wallpaper.util.download.FileDownloadUtil
import com.tzh.wallpaper.util.toDefault
import com.zzsr.baselibrary.util.OnPermissionCallBackListener
import com.zzsr.baselibrary.util.PermissionXUtil
import java.io.File
import java.io.IOException

object WallpaperManagerUtil {
    /**
     * 设置壁纸
     */
    fun setWallpaper(context: AppCompatActivity, url : String){
        PermissionXUtil.requestAnyPermission(context, mutableListOf<String>().apply {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        },object : OnPermissionCallBackListener{
            override fun onAgree() {
                val manager = context.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
                BitmapUtil.urlToBitmap(context,url,object : BitmapUtil.BitmapListener{
                    override fun sure(bitmap: Bitmap) {
                        manager.setBitmap(bitmap)
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
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        },object : OnPermissionCallBackListener{
            override fun onAgree() {
                val manager = fragment.activity?.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
                fragment.context?.let {
                    BitmapUtil.urlToBitmap(it,url,object : BitmapUtil.BitmapListener{
                        override fun sure(bitmap: Bitmap) {
                            manager.setBitmap(bitmap)
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
    fun setVideoWallpaper(fragment : Fragment, url : String,isVolume : Boolean = false){
        fragment.context?.apply {
            PermissionXUtil.requestAnyPermission(fragment, mutableListOf<String>().apply {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            },object : OnPermissionCallBackListener{
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
    fun setVideoWallpaper(activity : AppCompatActivity, url : String,isVolume : Boolean = false){
        PermissionXUtil.requestAnyPermission(activity, mutableListOf<String>().apply {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        },object : OnPermissionCallBackListener{
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

    private fun setLocalVideoWallpaper(context : Context, url : String,isVolume : Boolean = false){
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
            ComponentName(context,VideoWallpaper::class.java)
        )
        context.startActivity(intent)
    }
}