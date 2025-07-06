package com.tzh.wallpaperlib.dao.daoutils

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tzh.wallpaperlib.dao.dto.WidgetDto


@Dao
interface WidgetDao {
    @Insert
    fun insert(dto: WidgetDto)

    @Update
    fun update(dto: WidgetDto)

    @Delete
    fun delete(dto: WidgetDto)

    @Query("SELECT * FROM widgetDto")
    fun getAllWidget(): List<WidgetDto>

    @Query("SELECT * FROM widgetDto WHERE token = :token")
    fun getWidgetByToken(token: String): List<WidgetDto>

    @Query("SELECT * FROM widgetDto WHERE widget_id = :widgetId")
    fun getWidgetByWidgetId(widgetId: String): List<WidgetDto>
}