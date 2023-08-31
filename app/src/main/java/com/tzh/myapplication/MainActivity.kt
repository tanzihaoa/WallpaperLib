package com.tzh.myapplication

import android.util.Log
import com.tzh.myapplication.activity.VideoActivity
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityMainBinding
import com.tzh.wallpaper.util.video.VideoUtil
import com.tzh.wallpaper.util.wallpaper.WallpaperManagerUtil


class MainActivity : AppBaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    val url = "https://api.uubook.cn/oss0818/api/ossUrl.php?time=1693382619&go=1&process=&osskey=wallpaper_static/0daef4b6e87179cc161e0c82853ba83f.mp4&key=14eb0eb5ebaf47ec3be5cebda85bbc09"


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
        WallpaperManagerUtil.setVideoWallpaper(this,url,true)
    }

    fun saveVideo(){
        VideoUtil.saveVideo(this,url)
    }
}