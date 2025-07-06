package com.tzh.wallpaperlib.dao.daoutils;

import android.content.Context;

import com.tzh.wallpaperlib.BuildConfig;
import com.tzh.wallpaperlib.greendao.DaoMaster;
import com.tzh.wallpaperlib.greendao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * 创建数据库、创建数据库表、包含增删改查的操作以及数据库的升级
 */
public class BaseDaoManager {
    private static final String TAG = BaseDaoManager.class.getSimpleName();
    private static final String DB_NAME = "diary.db";

    private Context context;

    //多线程中要被共享的使用volatile关键字修饰
    private volatile static BaseDaoManager manager ;
    private static DaoMaster sDaoMaster;
    private static DaoMaster.DevOpenHelper sHelper;
    private static DaoSession sDaoSession;

    /**
     * 单例模式获得操作数据库对象
     *
     * @return
     */
    public static BaseDaoManager getInstance()
    {
        synchronized (BaseDaoManager.class) {
            if (manager == null) {
                manager = new BaseDaoManager();
            }
        }
        return manager;
    }

    private BaseDaoManager()
    {
        setDebug();
    }

    public void init(Context context)
    {
        this.context = context;
    }

    /**
     * 判断是否有存在数据库，如果没有则创建
     *
     * @return
     */
    public DaoMaster getDaoMaster()
    {
        if (sDaoMaster == null)
        {
            sHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            sDaoMaster = new DaoMaster(sHelper.getWritableDatabase());
        }
        return sDaoMaster;
    }

    /**
     * 完成对数据库的添加、删除、修改、查询操作，仅仅是一个接口
     *
     * @return
     */
    public DaoSession getDaoSession()
    {
        if (sDaoSession == null)
        {
            if (sDaoMaster == null)
            {
                sDaoMaster = getDaoMaster();
            }
            sDaoSession = sDaoMaster.newSession();
        }
        return sDaoSession;
    }

    /**
     * 打开输出日志，默认关闭
     */
    public void setDebug()
    {
        if (BuildConfig.DEBUG)
        {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }
    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    public void closeConnection()
    {
        closeHelper();
        closeDaoSession();
    }

    public void closeHelper()
    {
        if (sHelper != null)
        {
            sHelper.close();
            sHelper = null;
        }
    }

    public void closeDaoSession()
    {
        if (sDaoSession != null)
        {
            sDaoSession.clear();
            sDaoSession = null;
        }
    }
}

