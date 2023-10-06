package com.tzh.wallpaper.widget.base

import android.appwidget.AppWidgetProvider
import android.widget.RemoteViews

open class BaseWidgetProvider: AppWidgetProvider() {

    protected open fun getRemoteViews() : RemoteViews?{
        return null
    }
}