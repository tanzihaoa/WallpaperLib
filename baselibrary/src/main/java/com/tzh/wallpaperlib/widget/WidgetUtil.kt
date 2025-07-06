package com.tzh.wallpaperlib.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.tzh.wallpaperlib.dao.daoutils.AppDatabase
import com.tzh.wallpaperlib.dao.dto.WidgetDto
import com.tzh.wallpaperlib.dao.dto.WidgetRepository
import com.tzh.wallpaperlib.dialog.WallpaperHintDialog
import com.tzh.wallpaperlib.util.RomUtils
import com.tzh.wallpaperlib.util.toDefault
import com.tzh.wallpaperlib.util.wallpaper.RuntimeSettingPage
import com.tzh.wallpaperlib.util.wallpaper.ShortcutPermission

object WidgetUtil {

    private val TAG = this.javaClass.simpleName

    /**
     * 更新小组件，触发组件的onUpdate
     */
    fun update(context: Context,cls : Class<*>){
        val intent = Intent(context,cls)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val bundle = Bundle()
        bundle.putIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS, getAppWidgetIds(context,cls))
        intent.putExtras(bundle)
        context.sendBroadcast(intent)
    }

    /**
     * 添加到主屏幕
     */
    fun addToMainScreen(context: Context, dto : WidgetDto, cls : Class<*>){
        if(ShortcutPermission.check(context)){
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val myProvider = ComponentName(context,cls)

            if (getAppWidgetIds(context,cls).isNotEmpty()) {
                Toast.makeText(context,"组件已经存在",Toast.LENGTH_SHORT).show()
                return
            }
            val dao = WidgetRepository(AppDatabase.getDatabase(context).widgetDao())
            if(dao.getWidgetByToken(dto.token.toDefault("")).size.toDefault(0) == 0){
                dao.insert(dto)
            }else{
                dao.update(dto)
            }
            if(Build.VERSION.SDK_INT >= 26){
                if (appWidgetManager.isRequestPinAppWidgetSupported) {
                    appWidgetManager.requestPinAppWidget(myProvider, null, null)
                    if(RomUtils.isXiaomi()){
                        Toast.makeText(context,"组件添加成功",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(context,"手机版本过低",Toast.LENGTH_SHORT).show()
            }
        }else{
            WallpaperHintDialog(context,object : WallpaperHintDialog.HintDialogListener{
                override fun cancel() {

                }

                override fun ok() {
                    RuntimeSettingPage.start(context)
                }
            }).show("未打开手机快捷方式权限，确定去打开吗？")
        }
    }

    private fun getAppWidgetIds(context: Context, cls: Class<*>): IntArray {
        val awm = AppWidgetManager.getInstance(context)
        return awm.getAppWidgetIds(ComponentName(context, cls))
    }
}