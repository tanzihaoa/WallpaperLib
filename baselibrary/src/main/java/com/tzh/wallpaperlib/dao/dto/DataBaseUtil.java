package com.tzh.wallpaperlib.dao.dto;

import android.content.Context;

import com.tzh.wallpaperlib.dao.daoutils.AppDatabase;
import com.tzh.wallpaperlib.dao.daoutils.WidgetDao;

public class DataBaseUtil {
    public static WidgetDao getWidgetDao(Context context){
        return AppDatabase.Companion.getDatabase(context).widgetDao();
    }
}
