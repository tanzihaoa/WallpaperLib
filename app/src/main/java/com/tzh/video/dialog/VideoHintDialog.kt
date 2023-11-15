package com.tzh.video.dialog

import android.content.Context
import com.tzh.video.databinding.DialogVideoHintBinding
import com.tzh.wallpaper.dialog.BaseBindingDialog

/**
 * 提示dialog
 */
class VideoHintDialog(context : Context) : BaseBindingDialog<DialogVideoHintBinding>(context, com.tzh.video.R.layout.dialog_video_hint){

    init {
        isCancelableDialog = false
        isCanceledOnTouchOutsideDialog = false
    }

    override fun initView() {

        //确定
        binding.tvOk.setOnClickListener {
            dismiss()
        }
    }

    override fun initData() {

    }

    fun setText(content : String) {
        binding.tvContent.text = content
    }

}