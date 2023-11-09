package com.tzh.wallpaper.activity

import android.content.Context
import android.content.Intent
import com.tzh.wallpaper.R
import com.tzh.wallpaper.base.AppBaseActivity
import com.tzh.wallpaper.databinding.ActivitySpliceVideoBinding
import com.tzh.wallpaper.util.img.CameraUtil
import com.tzh.wallpaper.util.img.ImageDTO

class SpliceVideoActivity : AppBaseActivity<ActivitySpliceVideoBinding>(R.layout.activity_splice_video) {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context,SpliceVideoActivity::class.java))
        }
    }

    override fun initView() {
        binding.tvSelect.setOnClickListener {
            CameraUtil.createAlbum(this,6,object : CameraUtil.onSelectCallback{
                override fun onResult(photos: MutableList<ImageDTO>?) {

                }

                override fun onCancel() {

                }
            })
        }
    }

    override fun initData() {

    }
}