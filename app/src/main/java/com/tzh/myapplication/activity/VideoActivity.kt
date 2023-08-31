package com.tzh.myapplication.activity

import android.content.Context
import android.content.Intent
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityVideoBinding

class VideoActivity : AppBaseActivity<ActivityVideoBinding>(R.layout.activity_video) {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context,VideoActivity::class.java))
        }
    }

    val url = "https://wallpaper.yjtechome.top/20163381_1679646019698_655549.mp4?e=2322980345&token=WsDlLjDgtDdmtdIV5Or9qEHGB6sODAC-O3wwCqF7:-TTzGSF-eMgJhVQm0BX7_EKl1EQ="

    override fun initView() {
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
        val jzDataSource = JZDataSource(url, "")
        jzDataSource.looping = true
        binding.videoPlay.setUp(jzDataSource,JzvdStd.SCREEN_NORMAL)

        binding.videoPlay.startVideoAfterPreloading()
    }

    override fun initData() {

    }

    override fun onPause() {
        super.onPause()
        //home back
        Jzvd.goOnPlayOnPause()
    }

    override fun onResume() {
        super.onResume()
        //home back
        Jzvd.goOnPlayOnResume()
    }
}