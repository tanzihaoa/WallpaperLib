package com.tzh.wallpaperlib.dialog

import android.content.Context
import com.tzh.wallpaperlib.R
import com.tzh.wallpaperlib.databinding.DialogWallpaperHintBinding

/**
 * 提示dialog
 */
class WallpaperHintDialog(context : Context, private val listener: HintDialogListener) : BaseBindingDialog<DialogWallpaperHintBinding>(context, R.layout.dialog_wallpaper_hint){

    override fun initView() {
        //取消
        binding.tvCancel.setOnClickListener {
            listener.cancel()
            dismiss()
        }

        //确定
        binding.tvOk.setOnClickListener {
            listener.ok()
            dismiss()
        }
    }

    override fun initData() {

    }

    fun show(content : String) {
        binding.tvContent.text = content
        binding.tvCancel.text = "取消"
        binding.tvOk.text = "确定"
        show()
    }

    fun show(content : String,ok : String = "确定",cancel : String = "取消") {
        binding.tvContent.text = content
        binding.tvCancel.text = cancel
        binding.tvOk.text = ok
        show()
    }

    interface HintDialogListener{
        fun cancel()

        fun ok()
    }


}