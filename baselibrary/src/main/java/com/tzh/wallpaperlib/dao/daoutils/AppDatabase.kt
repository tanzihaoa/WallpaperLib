package com.tzh.wallpaperlib.dao.daoutils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tzh.wallpaperlib.dao.dto.WidgetDto

@Database(
    entities = [WidgetDto::class],  // 多实体类
    version = 1,
    exportSchema = true  // 导出Schema用于迁移:cite[3]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun widgetDao(): WidgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,"app_db"
                )
                .addCallback(databaseCallback)  // 初始回调
//                .addMigrations(MIGRATION_1_2)  // 迁移策略
                .allowMainThreadQueries()
                .build()
                .also { INSTANCE = it }
            }
        }

        // 数据库创建回调
        private val databaseCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // 初始化数据
            }
        }

        // 版本迁移示例（v1→v2）
//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE books (id INTEGER PRIMARY KEY, title TEXT)")
//            }
//        }
    }
}