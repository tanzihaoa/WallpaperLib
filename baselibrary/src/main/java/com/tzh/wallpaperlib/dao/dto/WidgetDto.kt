package com.tzh.wallpaperlib.dao.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "widgetDto")
class WidgetDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String? = null,
    var token: String? = null,
    var widget_id: String? = null,
    var text1: String? = null,
    var text2: String? = null,
    var text3: String? = null,
    var text4: String? = null,
    var text5: String? = null,
    var text6: String? = null,
    var text7: String? = null,
    var text8: String? = null,
    var text9: String? = null,
    var text10: String? = null,
    var num1: Int = 0,
    var num2: Int = 0,
    var num3: Int = 0,
    var num4: Int = 0,
    var num5: Int = 0,
    var num6: Int = 0,
    var num7: Int = 0,
    var num8: Int = 0,
    var num9: Int = 0,
    var num10: Int = 0
)