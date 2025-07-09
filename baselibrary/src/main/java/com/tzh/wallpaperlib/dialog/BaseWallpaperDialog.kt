package com.tzh.wallpaperlib.dialog

import android.content.Context
import com.tzh.wallpaperlib.R
import com.tzh.wallpaperlib.databinding.DialogBaseWallpaperBinding
import com.tzh.wallpaperlib.databinding.DialogVolumeBinding

/**
 * 设置壁纸选择桌面或者锁屏
 */
class BaseWallpaperDialog(context: Context, val listener : WallpaperListener) : BaseBindingDialog<DialogBaseWallpaperBinding>(context,R.layout.dialog_base_wallpaper) {

    init {
        initBottomDialog()
    }

    override fun initView() {
        binding.layoutHome.setOnClickListener {
            dismiss()
            listener.wallpaper(1)
        }

        binding.layoutLocker.setOnClickListener {
            dismiss()
            listener.wallpaper(2)
        }
    }

    override fun initData() {

    }

    interface WallpaperListener{
        fun wallpaper(type : Int)
    }
}