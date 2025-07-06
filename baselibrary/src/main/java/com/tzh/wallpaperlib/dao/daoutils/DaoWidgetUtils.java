package com.tzh.wallpaperlib.dao.daoutils;

import com.tzh.wallpaperlib.dao.dto.WidgetDto;

import java.util.List;

public class DaoWidgetUtils {
    private volatile static DaoWidgetUtils instance;

    public BaseCommonDaoUtils<WidgetDto> userCommonDaoUtils;


    public static DaoWidgetUtils getInstance()
    {
        synchronized (DaoWidgetUtils.class) {
            if(instance==null){
                instance = new DaoWidgetUtils();
            }
        }

        return instance;
    }

    private DaoWidgetUtils()
    {
        BaseDaoManager mManager = BaseDaoManager.getInstance();

        userCommonDaoUtils = new BaseCommonDaoUtils<>(WidgetDto.class,mManager.getDaoSession().getWidgetDtoDao());

    }


    //新建用户
    public void daoInsertDefaultUser(WidgetDto dto){
        userCommonDaoUtils.insert(dto);
    }

    //查询用户
    public List<WidgetDto> daoQueryAllUser(){
        return userCommonDaoUtils.queryAll();
    }

    //查询用户
    public WidgetDto daoQueryUser(long id){
        return userCommonDaoUtils.queryById(id);
    }

    //查询用户
    public List<WidgetDto> daoQueryUserByWidgetId(String widgetId){
        return userCommonDaoUtils.queryByNativeSql("where widget_id=?", new String[]{widgetId});
    }

    //查询用户
    public List<WidgetDto> daoQueryUserByToken(String token){
        return userCommonDaoUtils.queryByNativeSql("where token=?", new String[]{token});
    }


    //删除用户
    public boolean deleteAllUser(){
        return userCommonDaoUtils.deleteAll();
    }

    //更新用户
    public boolean updateUser(WidgetDto user){
        return userCommonDaoUtils.update(user);
    }
}
