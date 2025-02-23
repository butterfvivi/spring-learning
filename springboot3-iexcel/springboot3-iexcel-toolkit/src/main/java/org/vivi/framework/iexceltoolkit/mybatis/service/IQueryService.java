package org.vivi.framework.iexceltoolkit.mybatis.service;


import org.vivi.framework.iexceltoolkit.mybatis.entity.ExportBeanConfig;
import org.vivi.framework.iexceltoolkit.mybatis.interfaces.IDAOAdapter;

import java.util.List;

/**
 *  封装底层SQL业务查询细节，
 *  将sql查询与业务拼接解耦
 */
public interface IQueryService {

    /**
     * 相当于执行业务的DAO实现层
     * @param idaoAdapter
     * @param exportBean
     * @return
     * @throws Exception
     */
    List exeQuery(IDAOAdapter idaoAdapter, ExportBeanConfig exportBean) throws Exception;

    List exeQuery( ExportBeanConfig exportBean) throws Exception;
}
