package com.tzh.wallpaper.widget

import android.Manifest
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.tzh.wallpaper.widget.base.BaseWidgetProvider

object WidgetUtil {

    private val TAG = this.javaClass.simpleName

    /**
     * 更新小组件，触发组件的onUpdate
     */
    fun update(context: Context){
        val intent = Intent(context,BaseWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val bundle = Bundle()
        bundle.putIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS, getAppWidgetIds(context))
        intent.putExtras(bundle)
        context.sendBroadcast(intent)
    }

    /**
     * 添加到主屏幕
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun addToMainScreen(context: Context){
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val myProvider = ComponentName(context, BaseWidgetProvider::class.java)

        if (getAppWidgetIds(context).isNotEmpty()) {
            Toast.makeText(context,"组件已经存在",Toast.LENGTH_SHORT).show()
            return
        }

        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            appWidgetManager.requestPinAppWidget(myProvider, null, null)
        }
    }
    private fun getAppWidgetIds(context: Context) :IntArray{
        val awm = AppWidgetManager.getInstance(context)
        val appWidgetIDs: IntArray = awm.getAppWidgetIds(ComponentName(context,BaseWidgetProvider::class.java))
        return appWidgetIDs
    }
}