package org.vivi.framework.excel.configure.core.entity;

import org.vivi.framework.excel.configure.core.beans.ConverterFieldBean;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 导出参数配置文件
 */
public class ExportBeanConfig {

    /**
     * 查询对象模型
     */
    private Object t;
    /**
     * 查询条数sql
     */
    private String countSql;

    /**
     * 查询结果集sql
     */
    private String querySql;

    /**
     * 查询参数
     */
    private Object [] params;
    /**
     * 控制导出超过多少使用并行导出策略
     */
    private int size;

    /**
     * 控制并行导出下每次导出多少
     */
    private int limitCount;

    /**
     * 使用并行查询
     */
    private boolean useParallelQuery;


    /**
     * 数据源替换
     */
    private Map<String, Map<String,String>> queryCacheMap;

    /**
     * 指定底层返回的数据格式是List<object>
     *     还是List<List<String>>
     * true:使用 List<object>
     * false:使用List<List<String>>
     */
    private boolean useObjectModel;

    /**
     * 生成文件的地址
     */
    private String filePath;

    /**
     * 自定义转换器spring对象
     */
    private List<String> converterList;

    /**
     * 有几个转换器就有几个过滤属性bean
     */
    private List<ConverterFieldBean> converterFieldBeanList;

    /**
     * 底层DAO执行对象
     */
    private Object daoOperater;


    public Object getDaoOperater() {
        return daoOperater;
    }

    public void setDaoOperater(Object daoOperater) {
        this.daoOperater = daoOperater;
    }

    public List<String> getConverterList() {
        return converterList;
    }

    public void setConverterList(List<String> converterList) {
        this.converterList = converterList;
    }

    public List<ConverterFieldBean> getConverterFieldBeanList() {
        return converterFieldBeanList;
    }

    public void setConverterFieldBeanList(List<ConverterFieldBean> converterFieldBeanList) {
        this.converterFieldBeanList = converterFieldBeanList;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isUseObjectModel() {
        return useObjectModel;
    }

    public void setUseObjectModel(boolean useObjectModel) {
        this.useObjectModel = useObjectModel;
    }

    public Map<String, Map<String, String>> getQueryCacheMap() {
        return queryCacheMap;
    }

    public void setQueryCacheMap(Map<String, Map<String, String>> queryCacheMap) {
        this.queryCacheMap = queryCacheMap;
    }

    public boolean isUseParallelQuery() {
        return useParallelQuery;
    }

    public void setUseParallelQuery(boolean useParallelQuery) {
        this.useParallelQuery = useParallelQuery;
    }

    public int getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }

    public Object getT() {
        return t;
    }

    public void setT(Object t) {
        this.t = t;
    }

    public String getCountSql() {
        return countSql;
    }

    public void setCountSql(String countSql) {
        this.countSql = countSql;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ExportBeanConfig{" +
                "t=" + t +
                ", countSql='" + countSql + '\'' +
                ", querySql='" + querySql + '\'' +
                ", params=" + Arrays.toString(params) +
                ", size=" + size +
                ", limitCount=" + limitCount +
                ", useParallelQuery=" + useParallelQuery +
                ", queryCacheMap=" + queryCacheMap +
                ", useObjectModel=" + useObjectModel +
                ", filePath='" + filePath + '\'' +
                ", converterList=" + converterList +
                ", converterFieldBeanList=" + converterFieldBeanList +
                ", daoOperater=" + daoOperater +
                '}';
    }

}
