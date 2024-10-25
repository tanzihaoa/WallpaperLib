package com.tzh.wallpaperlib.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import cn.jzvd.JzvdStd
import com.tzh.wallpaperlib.R

class MyJZVideoPlayerStandard(context: Context, attrs: AttributeSet) : JzvdStd(context, attrs) {

    override fun init(context: Context) {
        super.init(context)

    }

    override fun getLayoutId(): Int {
        return R.layout.layout_jzstd_notitle
    }

    override fun onClick(v: View?) {

    }
}