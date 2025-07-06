package com.tzh.video.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.tzh.video.receiver.MyBroadcastReceiver
import com.tzh.wallpaperlib.R
import com.tzh.wallpaperlib.dao.dto.DataBaseUtil
import com.tzh.wallpaperlib.dao.dto.WidgetDto
import com.tzh.wallpaperlib.util.toDefault
import com.tzh.wallpaperlib.widget.base.BaseWidgetProvider
import com.tzh.wallpaperlib.widget.WidgetUtil

class MyWidgetProvider : BaseWidgetProvider() {
    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        appWidgetIds?.forEach { appWidgetId ->
            val views = RemoteViews(context?.packageName, R.layout.widget_layout)
            getRemoteViews()?.let {

            }
            val list = DataBaseUtil.getWidgetDao(context).getWidgetByToken(WidgetType.MyWidgetProvider)
            if(list.isNotEmpty()){
                val dto = list[0]
                if(dto.widget_id != appWidgetId.toString()){
                    dto.widget_id = appWidgetId.toString()
                    DataBaseUtil.getWidgetDao(context).update(dto)
                }
                views.setTextViewText(R.id.tv_name,dto.name)
                views.setTextViewText(R.id.tv_num,dto.num1.toString())
            }
            val intent = Intent(context, MyBroadcastReceiver::class.java)
            intent.action = "com.tzh.video.AUTO"
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, WidgetType.MyWidgetProvider)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.image_bg, pendingIntent)
            appWidgetManager?.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        intent?.apply {
            val action = this.action
            val deskWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,-1)
            if(deskWidgetId >= 0){
                val list = DataBaseUtil.getWidgetDao(context).getWidgetByWidgetId(deskWidgetId.toString())
                if(list.isNotEmpty()){
                    val dto = list[0]
                    dto.num1 += 1
                    DataBaseUtil.getWidgetDao(context).update(dto)
                    if (context != null) {
                        WidgetUtil.update(context,MyWidgetProvider::class.java)
                    }
                }
            }
        }
    }

    companion object{
        /**
         * 更新组件调用
         */
        fun updateAppWidget(
            context: Context,
            dto : WidgetDto
        ){
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.tv_name,dto.name)
            views.setTextViewText(R.id.tv_num,dto.num1.toString())
            AppWidgetManager.getInstance(context).updateAppWidget(dto.widget_id.toDefault("0").toInt(),views)
        }
    }
}