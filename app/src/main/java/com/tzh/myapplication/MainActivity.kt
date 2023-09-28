package com.tzh.myapplication

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.tzh.myapplication.activity.VideoActivity
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityMainBinding
import com.tzh.wallpaper.util.video.VideoUtil
import com.tzh.wallpaper.util.wallpaper.WallpaperManagerUtil
import com.tzh.wallpaper.widget.WidgetUtil
import com.tzh.wallpaper.widget.base.BaseWidgetProvider


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

    @RequiresApi(Build.VERSION_CODES.O)
    fun toRecycler(){
        WidgetUtil.addToMainScreen(this, BaseWidgetProvider::class.java)
    }

    /**
     * 跟新小组件
     */
    fun upDateWidget(){
        WidgetUtil.update(this,BaseWidgetProvider::class.java)
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