package com.tzh.wallpaperlib.dao.daoutils;

import com.tzh.wallpaperlib.greendao.DaoSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * 通用greendao工具
 * @param <T>
 */
public class CommonDaoUtils<T> {

    private static final String TAG = CommonDaoUtils.class.getSimpleName();

    private final DaoSession daoSession;
    private final Class<T> entityClass;
    private final AbstractDao<T, Long> entityDao;

    public CommonDaoUtils(Class<T> pEntityClass, AbstractDao<T, Long> pEntityDao)
    {
        DaoManager mManager = DaoManager.getInstance();
        daoSession = mManager.getDaoSession();
        entityClass = pEntityClass;
        entityDao = pEntityDao;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    /**
     * 插入记录，如果表未创建，先创建表
     *
     * @param pEntity
     * @return
     */
    public boolean insert(T pEntity)
    {
        return entityDao.insert(pEntity) != -1;
    }

    /**
     * 插入记录数据存在则替换，数据不存在则插入
     *
     * @param pEntity 插入的数据
     */
    public boolean insertOrReplace(T pEntity)
    {
        return entityDao.insertOrReplace(pEntity) != -1;
    }


    /**
     * 插入多条数据，在子线程操作
     *
     * @param pEntityList 插入的数据
     */
    public boolean insertMulti(final List<T> pEntityList)
    {
        try
        {
            daoSession.runInTx(new Runnable()
            {
                @Override
                public void run()
                {
                    for (T meizi : pEntityList)
                    {
                        daoSession.insertOrReplace(meizi);
                    }
                }
            });
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改一条数据
     *
     * @param pEntity 修改的数据
     */
    public boolean update(T pEntity)
    {
        try
        {
            daoSession.update(pEntity);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除单条记录
     *
     * @param pEntity 删除的数据
     */
    public boolean delete(T pEntity)
    {
        try
        {
            //按照id删除
            daoSession.delete(pEntity);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除所有记录
     *
     */
    public boolean deleteAll()
    {
        try
        {
            //按照id删除
            daoSession.deleteAll(entityClass);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询所有记录
     *
     */
    public List<T> queryAll()
    {
        return daoSession.loadAll(entityClass);
    }

    /**
     * 根据主键id查询记录
     *
     * @param key 查询ID
     */
    public T queryById(long key)
    {
        return daoSession.load(entityClass, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<T> queryByNativeSql(String sql, String[] conditions)
    {
        return daoSession.queryRaw(entityClass, sql, conditions);
    }


    /**
     * 使用queryBuilder进行查询
     *
     */
    public List<T> queryByQueryBuilder(WhereCondition cond, WhereCondition... condMore)
    {
        QueryBuilder<T> queryBuilder = daoSession.queryBuilder(entityClass);
        return queryBuilder.where(cond, condMore).list();
    }
    /**
     * 使用queryBuilder进行查询
     *
     */
    public List<T> queryByBuilder(WhereCondition cond)
    {
        QueryBuilder<T> queryBuilder = daoSession.queryBuilder(entityClass);
        return queryBuilder.where(cond).list();
    }

}

