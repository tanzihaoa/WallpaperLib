package com.tzh.wallpaper.widget

import androidx.annotation.StringDef

@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPE
)
@MustBeDocumented
@StringDef(
    WidgetType.MyWidgetProvider,
)
@Retention(AnnotationRetention.SOURCE)
annotation class WidgetType {
    companion object{
        /**
         * 组件
         */
        const val MyWidgetProvider = "MyWidgetProvider"
    }
}