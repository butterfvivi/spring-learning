package org.vivi.framework.excel.configure.base.interfaces;

import java.util.List;

/**
 * 适配底层DAO对象
 */
public interface IDAOAdapter {

    /**
     * 注册DAO对象
     *
     * @param daoObject
     */
    void registerDAO(Object daoObject);


    /**
     * 执行sql获取需要导出的数据条数
     *
     * @param sql
     * @param param
     * @return
     */
    int getCount(String sql, Object... param) throws Exception;

    /**
     * 执行sql获取需要导出的数据列表
     *
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    List getListBySql(Class<?> clazz, String sql, Object... params) throws Exception;

    /**
     * 取消注册对象
     */
    void disRegist();
}
