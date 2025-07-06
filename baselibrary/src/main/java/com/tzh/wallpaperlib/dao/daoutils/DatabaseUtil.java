package com.tzh.wallpaperlib.dao.daoutils;

import android.content.Context;

import androidx.room.Room;

public class DatabaseUtil {
    public static void init(Context context){
        Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "app_database")
                .createFromAsset("database/myapp.db")
                .build();
    }
}
