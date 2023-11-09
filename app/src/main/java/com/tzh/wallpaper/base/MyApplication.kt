package com.tzh.wallpaper.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.tzh.wallpaper.dao.daoutils.DaoManager

class MyApplication : Application() {

    companion object {

        lateinit var mContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        DaoManager.getInstance().init(this)

    }

    /**
     * 这里会在onCreate之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}