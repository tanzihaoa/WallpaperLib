package com.tzh.wallpaper.widget.base

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.tzh.wallpaper.R

open class BaseWidgetProvider: AppWidgetProvider() {
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
            views.setOnClickPendingIntent(R.id.widget_textview, pendingIntent)
            appWidgetManager?.updateAppWidget(appWidgetId, views)
        }
    }

    protected open fun getRemoteViews() : RemoteViews?{
        return null
    }

    companion object{
        /**
         * 更新组件调用
         */
        fun updateAppWidget(
            context: Context?,
            appWidgetManager: AppWidgetManager?,
            appWidgetId: Int,
            text: String
        ){
            val views = RemoteViews(context!!.packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.widget_textview,text)
            appWidgetManager?.updateAppWidget(appWidgetId,views)
        }
    }
}