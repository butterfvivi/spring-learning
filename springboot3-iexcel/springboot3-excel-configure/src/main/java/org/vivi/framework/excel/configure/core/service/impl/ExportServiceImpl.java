package org.vivi.framework.excel.configure.core.service.impl;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vivi.framework.excel.configure.common.Constant;
import org.vivi.framework.excel.configure.core.entity.ExportBeanConfig;
import org.vivi.framework.excel.configure.base.interfaces.IDAOAdapter;
import org.vivi.framework.excel.configure.core.service.IExport;
import org.vivi.framework.excel.configure.core.service.IQueryService;

import java.util.List;

public class ExportServiceImpl implements IExport {

    @Autowired
    private IDAOAdapter daoAdapter;


    @Autowired
    private IQueryService queryService;

    private Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);

    @Override
    public List getExportList(ExportBeanConfig exportBean) throws Exception {
        String countSql = exportBean.getCountSql();
        String querySql = exportBean.getQuerySql();

        if(exportBean.isUseParallelQuery() && (!querySql.contains(Constant.LIMIT_CYCLE_COUNT) || !querySql.contains(Constant.LIMIT_COUNT))){
            logger.error("useParallelQuery must have $LIMIT_CYCLE_COUNT and $LIMIT_COUNT to generate ParallelQuery  sql ..............");
            return null;
        }
        if(exportBean.isUseParallelQuery() && (exportBean.getSize()==0 || exportBean.getLimitCount() == 0)){
            logger.error("useParallelQuery must set size>0 and limitCount>0 to generate ParallelQuery  sql ..............");
            return null;
        }

        if(StrUtil.isEmpty(countSql) || StrUtil.isEmpty(querySql)){
            logger.error("querySql is null..............");
            return null;
        }
        return queryService.exeQuery(daoAdapter,exportBean);
    }

}

