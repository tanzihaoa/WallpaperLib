package com.tzh.wallpaper.widget

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
    fun update(context: Context,cls : Class<*>){
        val intent = Intent(context,BaseWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val bundle = Bundle()
        bundle.putIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS, getAppWidgetIds(context,cls))
        intent.putExtras(bundle)
        context.sendBroadcast(intent)
    }

    /**
     * 添加到主屏幕
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun addToMainScreen(context: Context,cls : Class<*>){
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val myProvider = ComponentName(context,cls)

        if (getAppWidgetIds(context,BaseWidgetProvider::class.java).isNotEmpty()) {
            Toast.makeText(context,"组件已经存在",Toast.LENGTH_SHORT).show()
            return
        }

        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            appWidgetManager.requestPinAppWidget(myProvider, null, null)
        }
    }

    private fun getAppWidgetIds(context: Context,cls : Class<*>) :IntArray{
        val awm = AppWidgetManager.getInstance(context)
        val appWidgetIDs: IntArray = awm.getAppWidgetIds(ComponentName(context,cls))
        return appWidgetIDs
    }
}