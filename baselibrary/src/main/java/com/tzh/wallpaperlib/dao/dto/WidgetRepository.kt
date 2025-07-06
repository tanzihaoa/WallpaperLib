package com.tzh.wallpaperlib.dao.dto

import com.tzh.wallpaperlib.dao.daoutils.WidgetDao

class WidgetRepository(val widgetDao : WidgetDao) {
    fun insert(dto : WidgetDto) = widgetDao.insert(dto)
    fun update(dto : WidgetDto) = widgetDao.update(dto)
    fun getAllWidget() = widgetDao.getAllWidget()
    fun getWidgetByToken(token : String) = widgetDao.getWidgetByToken(token)
}