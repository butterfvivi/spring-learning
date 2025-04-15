package org.vivi.framework.iexceltoolkit.tool.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vivi.framework.iexceltoolkit.tool.config.ExportBeanConfig;
import org.vivi.framework.iexceltoolkit.tool.mybatis.enums.Constant;
import org.vivi.framework.iexceltoolkit.tool.mybatis.interfaces.IDAOAdapter;
import org.vivi.framework.iexceltoolkit.tool.service.IQueryService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class QueryServiceImpl implements IQueryService {

    private Logger logger = LoggerFactory.getLogger(QueryServiceImpl.class);

    @Override
    public List exeQuery(IDAOAdapter idaoAdapter, ExportBeanConfig exportBean) throws Exception {
        idaoAdapter.registerDAO(exportBean.getDaoOperater());

        int count =  idaoAdapter.getCount(exportBean.getCountSql(),exportBean.getParams());

        logger.info("queryCount = {}",count);
        if(count == 0){
            return null;
        }
        Class clazz = exportBean.getT().getClass();
        //如果不是用的对象模型则设置为空
        if(!exportBean.isUseObjectModel()){
            clazz = null;
        }

        if(exportBean.isUseParallelQuery()){
            long startTime = System.currentTimeMillis();
            //使用并行，但是查询出的条数count小于并行查询条数阈值size,此时退化为串行查询
            if(count <= exportBean.getSize()){
                String newquerySql = this.getSql(exportBean.getQuerySql(),count);
                logger.info("The degradation of newquerySql = {}" , newquerySql);

                List list = idaoAdapter.getListBySql(clazz,newquerySql,exportBean.getParams());
                //解除dao操作对象引用
                idaoAdapter.disRegist();
                return list;
            } else{
                int queryCount = count / exportBean.getLimitCount();
                if(count % exportBean.getLimitCount() != 0){
                    queryCount = count / exportBean.getLimitCount() + 1;
                }

                List<CompletableFuture<List>> resultFuture = new ArrayList<>();
                List resultList = new ArrayList();

                for (int q = 0;q < queryCount;q++){
                    int limitCount = exportBean.getLimitCount() * q;
                    String tmpQuerySql = exportBean.getQuerySql();
                    tmpQuerySql = tmpQuerySql.replace(Constant.LIMIT_CYCLE_COUNT,limitCount + "");

                    //最后不够，则不需要再整取limitCount
                    if(count - exportBean.getLimitCount() * q < exportBean.getLimitCount()){
                        limitCount = count - exportBean.getLimitCount() * q;
                        tmpQuerySql = tmpQuerySql.replace(Constant.LIMIT_COUNT, limitCount + "");
                    }else{
                        tmpQuerySql = tmpQuerySql.replace(Constant.LIMIT_COUNT, exportBean.getLimitCount() + "");
                    }

                    String finalTmpQuerySql = tmpQuerySql;
                    logger.info("finalTmpQuerySql = {}",finalTmpQuerySql);
                    Class finalClazz = clazz;
                    CompletableFuture<List> future = CompletableFuture.supplyAsync(() -> {
                        try {
                            return idaoAdapter.getListBySql(finalClazz, finalTmpQuerySql,exportBean.getParams());
                        } catch (Exception e) {
                            logger.error("并行查询失败",e);
                            return null;
                        }
                    });
                    resultFuture.add(future);
                }

                resultFuture.stream().forEach(v->{
                    try {
                        resultList.addAll(v.get());
                    } catch (InterruptedException e) {
                        logger.error("获取结果集失败InterruptedException",e);
                    } catch (ExecutionException e) {
                        logger.error("获取结果集失败ExecutionException",e);
                    }
                });
                //解除dao操作对象引用
                idaoAdapter.disRegist();
                long endTime = System.currentTimeMillis();
                logger.info("ParallelQuery useTime = {}",(endTime - startTime));
                if(resultList.size() != count){
                    logger.error("queryCount is not match result list size,please check your sql params!");
                }
                return resultList;
            }
        }else {
            long startTime = System.currentTimeMillis();
            String tmpQuerySql = exportBean.getQuerySql();
            tmpQuerySql = tmpQuerySql.replace(Constant.LIMIT_CYCLE_COUNT,"0");
            tmpQuerySql = tmpQuerySql.replace(Constant.LIMIT_COUNT, count + "");

            logger.info("tmpQuerySql = {}",tmpQuerySql);
            List list =  idaoAdapter.getListBySql(clazz, tmpQuerySql,exportBean.getParams());
            //解除dao操作对象引用
            idaoAdapter.disRegist();
            long endTime = System.currentTimeMillis();
            logger.info("query useTime = {}",(endTime - startTime));

            if(list.size() != count){
                logger.error("queryCount is not match result list size,please check your sql params!");
            }
            return list;
        }
    }

    @Override
    public List exeQuery(ExportBeanConfig exportBean) throws Exception {
        return List.of();
    }


    private String getSql(String querySql,int count){
        String newquerySql = querySql.replace(Constant.LIMIT_CYCLE_COUNT, "0");
        return newquerySql.replace(Constant.LIMIT_COUNT, count + "");
    }

}

