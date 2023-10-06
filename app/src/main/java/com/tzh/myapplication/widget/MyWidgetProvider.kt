package com.tzh.myapplication.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.tzh.wallpaper.R
import com.tzh.wallpaper.widget.base.BaseWidgetProvider

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
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(R.id.tv_name, pendingIntent)
            appWidgetManager?.updateAppWidget(appWidgetId, views)
        }
    }


    companion object{
        /**
         * 更新组件调用
         */
        fun updateAppWidget(
            context: Context?,
            appWidgetId: Int,
            text: String
        ){
            val views = RemoteViews(context!!.packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.tv_name,text)
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId,views)
        }
    }
}