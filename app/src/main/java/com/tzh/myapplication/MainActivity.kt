package com.tzh.myapplication

import android.util.Log
import com.tzh.myapplication.activity.VideoActivity
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityMainBinding
import com.tzh.wallpaper.util.video.VideoUtil
import com.tzh.wallpaper.util.wallpaper.WallpaperManagerUtil


class MainActivity : AppBaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    val url = "https://wallpaper.yjtechome.top/20163381_1679646019698_655549.mp4?e=2322980345&token=WsDlLjDgtDdmtdIV5Or9qEHGB6sODAC-O3wwCqF7:-TTzGSF-eMgJhVQm0BX7_EKl1EQ="


    override fun initView() {
        binding.v = this
        binding.tvWallpaper.setOnClickListener {
            start()
        }
    }

    override fun initData() {

    }

    fun toRecycler(){

    }

    fun toImage(){
        VideoActivity.start(this)
    }

    fun start(){
        Log.e("start=====","start")
        WallpaperManagerUtil.setVideoWallpaperDialog(this,url)
    }

    fun saveVideo(){
        VideoUtil.saveVideo(this,url)
    }
}