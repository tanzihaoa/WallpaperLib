package com.tzh.wallpaperlib.widget.base

import android.appwidget.AppWidgetProvider
import android.widget.RemoteViews

open class BaseWidgetProvider: AppWidgetProvider() {

    protected open fun getRemoteViews() : RemoteViews?{
        return null
    }
}