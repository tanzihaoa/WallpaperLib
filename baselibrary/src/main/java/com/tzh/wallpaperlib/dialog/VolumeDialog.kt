package com.tzh.wallpaperlib.dialog

import android.content.Context
import com.tzh.wallpaperlib.R
import com.tzh.wallpaperlib.databinding.DialogVolumeBinding

class VolumeDialog(context: Context,val listener : VolumeListener) : BaseBindingDialog<DialogVolumeBinding>(context,R.layout.dialog_volume) {
    override fun initView() {
        binding.tvVolumeYes.setOnClickListener {
            dismiss()
            listener.volume(true)
        }

        binding.tvVolumeNo.setOnClickListener {
            dismiss()
            listener.volume(false)
        }
    }

    override fun initData() {

    }

    interface VolumeListener{
        fun volume(volume : Boolean)
    }
}