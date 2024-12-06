package com.tzh.video.receiver

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tzh.video.util.LogUtils
import com.tzh.video.widget.MyWidgetProvider
import com.tzh.video.widget.WidgetType
import com.tzh.wallpaperlib.dao.daoutils.DaoWidgetUtils
import com.tzh.wallpaperlib.util.toDefault
import com.tzh.wallpaperlib.widget.WidgetUtil

/**
 * 接收通知
 */
class MyBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(context : Context, intent: Intent) {
        LogUtils.e("=========","触发广播")
        if(intent.action == "com.tzh.video.AUTO"){
            LogUtils.e("=========","com.tzh.video.AUTO")
            val widgetType = intent.getStringExtra(AppWidgetManager.EXTRA_APPWIDGET_ID).toDefault("")
            LogUtils.e("=========",widgetType)
            val list = DaoWidgetUtils.getInstance().daoQueryUserByToken(WidgetType.MyWidgetProvider)
            if(list.size > 0){
                val dto = list[0]
                dto.name = "我的组件"
                dto.num1 = dto.num1 + 1
                DaoWidgetUtils.getInstance().updateUser(dto)
                WidgetUtil.update(context, MyWidgetProvider::class.java)
            }
        }
    }
}