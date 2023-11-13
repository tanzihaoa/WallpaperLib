package com.tzh.video

import android.util.Log
import com.tzh.video.activity.SpliceVideoActivity
import com.tzh.video.activity.VideoActivity
import com.tzh.video.base.AppBaseActivity
import com.tzh.video.databinding.ActivityMainBinding
import com.tzh.video.widget.MyWidgetProvider
import com.tzh.video.widget.WidgetType
import com.tzh.wallpaper.dao.daoutils.DaoWidgetUtils
import com.tzh.wallpaper.dao.dto.WidgetDto
import com.tzh.wallpaper.util.video.VideoUtil
import com.tzh.wallpaper.util.wallpaper.WallpaperManagerUtil
import com.tzh.wallpaper.widget.WidgetUtil


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
        val dto = WidgetDto()
        dto.token = WidgetType.MyWidgetProvider
        dto.num1 = 1
        dto.name = "我的应用"
        WidgetUtil.addToMainScreen(this,dto, MyWidgetProvider::class.java)
    }

    /**
     * 更新小组件
     */
    fun upDateWidget(){
        val list = DaoWidgetUtils.getInstance().daoQueryUserByToken(WidgetType.MyWidgetProvider)
        if(list.size > 0){
            val dto = list[0]
            dto.name = "我的组件"
            dto.num1 = dto.num1 + 1
            DaoWidgetUtils.getInstance().updateUser(dto)
            WidgetUtil.update(this,MyWidgetProvider::class.java)
        }
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

    fun spliceVideo(){
        SpliceVideoActivity.start(this)
    }
}