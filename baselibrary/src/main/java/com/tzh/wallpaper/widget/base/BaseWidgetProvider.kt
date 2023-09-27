package com.tzh.wallpaper.widget.base

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.tzh.wallpaper.R

class BaseWidgetProvider: AppWidgetProvider() {
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        appWidgetIds?.forEach { appWidgetId ->
            val views = RemoteViews(context?.packageName, R.layout.widget_layout)
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(R.id.widget_textview, pendingIntent)
            appWidgetManager?.updateAppWidget(appWidgetId, views)
        }
    }

    companion object{
        /**
         * 更新组件调用
         */
        fun updateAppWidget(
            context: Context?,
            appWidgetManager: AppWidgetManager?,
            appWidgetId: Int,
            imgRes: Int
        ){
            val views = RemoteViews(context!!.packageName, R.layout.widget_layout)
            views.setImageViewResource(R.id.widget_textview,imgRes)
            appWidgetManager?.updateAppWidget(appWidgetId,views)
        }
    }
}