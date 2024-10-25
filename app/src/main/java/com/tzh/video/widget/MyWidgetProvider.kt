package com.tzh.video.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.tzh.wallpaperlib.R
import com.tzh.wallpaperlib.dao.daoutils.DaoWidgetUtils
import com.tzh.wallpaperlib.dao.dto.WidgetDto
import com.tzh.wallpaperlib.widget.base.BaseWidgetProvider
import com.tzh.wallpaperlib.widget.WidgetUtil

class MyWidgetProvider : BaseWidgetProvider() {
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        appWidgetIds?.forEach { appWidgetId ->
            val views = RemoteViews(context?.packageName, R.layout.widget_layout)
            getRemoteViews()?.let {

            }
            val list = DaoWidgetUtils.getInstance().daoQueryUserByToken(WidgetType.MyWidgetProvider)
            if(list.size > 0){
                val dto = list[0]
                if(dto.widget_id != appWidgetId.toString()){
                    dto.widget_id = appWidgetId.toString()
                    DaoWidgetUtils.getInstance().updateUser(dto)
                }
                views.setTextViewText(R.id.tv_name,dto.name)
                views.setTextViewText(R.id.tv_num,dto.num1.toString())
            }
            val intent = Intent(context,MyWidgetProvider::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(R.id.tv_name, pendingIntent)
            appWidgetManager?.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        intent?.apply {
            val action = this.action
            val deskWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,-1)
            if(deskWidgetId >= 0){
                val list = DaoWidgetUtils.getInstance().daoQueryUserByWidgetId(deskWidgetId.toString())
                if(list.size > 0){
                    val dto = list[0]
                    dto.num1 += 1
                    DaoWidgetUtils.getInstance().updateUser(dto)
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
            AppWidgetManager.getInstance(context).updateAppWidget(dto.widget_id.toInt(),views)
        }
    }
}