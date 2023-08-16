package com.tzh.wallpaper.util.wallpaper

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tzh.wallpaper.util.BitmapUtil
import com.zzsr.baselibrary.util.OnPermissionCallBackListener
import com.zzsr.baselibrary.util.PermissionXUtil

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
}