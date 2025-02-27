//package org.vivi.framework.dynamic.sqlbatis.serialization;
//
//import com.alibaba.fastjson2.JSON;
//import com.alibaba.fastjson2.JSONReader;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//
//import org.apache.ibatis.logging.Log;
//import org.apache.ibatis.logging.LogFactory;
//import org.vivi.framework.dynamic.sqlbatis.serialization.session.DynamicSqlSessionTemplate;
//
//import javax.sql.DataSource;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class MappedStatementInitializeHandler {
//    private static final Log logger = LogFactory.getLog(MappedStatementInitializeHandler.class);
//    private String dynamicMappedStatementDataSourceKey;
//
//    public MappedStatementInitializeHandler(String mappedStatementDataSourceKey) {
//        this.dynamicMappedStatementDataSourceKey = mappedStatementDataSourceKey;
//    }
//
//    /**
//     * 初始化数据源
//     */
//    public void initDatasource(DynamicSqlSessionTemplate dynamicSqlSessionTemplate, DataSourceMapper dataSourceMapper) {
//        DynamicDataSourceHolder.set(dynamicMappedStatementDataSourceKey);
//        try {
//            LambdaQueryWrapper<DataSourceEntity> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(DataSourceEntity::getIsDeleted, 0);
//            List<DataSourceEntity> dds = dataSourceMapper.selectList(wrapper);
//            dds.stream().forEach(ds -> {
//                String key = NamespaceHelper.getIdentity(ds.getCode(), ds.getCode());
//                DruidDataSource dataSource = new DruidDataSource();
//                dataSource.setUrl(ds.getUrl());
//                dataSource.setUsername(ds.getUsername());
//                dataSource.setPassword(ds.getPassword());
//                dataSource.setDriverClassName(ds.getDriverClass());
//                DataSourceProperty property = BeanUtils.copy(ds, new DataSourceProperty());
//                property.setUid(String.valueOf(ds.getId()));
//                Map attributes = JSON.parseObject(ds.getAttributes());
//
//                dynamicSqlSessionTemplate.addDataSource(key, dataSource, property, attributes);
//            });
//        }catch (Exception e){
//            trip(dynamicSqlSessionTemplate,"load database dataSource error.",e);
//        }finally {
//            DynamicDataSourceHolder.clear();
//        }
//    }
//
//    /**
//     * 初始化sql
//     */
//    public void initMappedStatement(DynamicSqlSessionTemplate dynamicSqlSessionTemplate, MappedStatementMapper mappedStatementMapper) {
//        DynamicDataSourceHolder.set(dynamicMappedStatementDataSourceKey);
//        try {
//            LambdaQueryWrapper<MappedStatementEntity> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(MappedStatementEntity::getIsDeleted, 0);
//            List<DynamicMappedStatement> dmsList = mappedStatementMapper.selectList(wrapper).stream().map(ms -> {
//                        DynamicMappedStatement dms = new DynamicMappedStatement().toBuilder()
//                                .setTenantCode(ms.getTenantCode())
//                                .setNamespace(ms.getNamespace())
//                                .setShortId(ms.getCode())
//                                .setUid(String.valueOf(ms.getId()))
//                                .setName(ms.getName())
//                                .setCommandType(ms.getCommandType())
//                                .setEnable(ms.getStatus() != null && ms.getStatus() == 1)
//                                .setSqlStatement(ms.getSqlStatement())
//                                .setResultType(ms.getResultType())
//                                .setResultMap(ms.getResultMap())
//                                .setReturnType(ms.getReturnType())
//                                .setTables(StringUtils.isNotBlank(ms.getTables()) ? JSON.parseArray(ms.getTables(),String.class): null)
//                                .setDynamicDataSourceKeys(StringUtils.isNotBlank(ms.getDataSourceCodes()) ? JSON.parseArray(ms.getDataSourceCodes(),String.class).stream().toArray(String[]::new): null)
//                                .setInputParameter(StringUtils.isNotBlank(ms.getInputParameter()) ? JSON.parseArray(ms.getInputParameter(), MappedStatementMetaField.class, JSONReader.Feature.SupportClassForName) : null)
//                                .setOutputParameter(StringUtils.isNotBlank(ms.getOutputParameter()) ? JSON.parseArray(ms.getOutputParameter(), MappedStatementMetaField.class, JSONReader.Feature.SupportClassForName) : null)
//                                .setCreateTime(ms.getCreateTime())
//                                .setCreateBy(ms.getCreateBy())
//                                .setUpdateTime(ms.getUpdateTime())
//                                .setUpdateBy(ms.getUpdateBy())
//                                .builder();
//                        if (StringUtils.isNotBlank(ms.getAttribute())) {
//                            DynamicMappedStatementAttributes attributes = JSON.parseObject(ms.getAttribute(), DynamicMappedStatementAttributes.class);
//                            BeanUtils.copy(attributes, dms);
//                        }
//                        return dms;
//                    }
//            ).collect(Collectors.toList());
//            dynamicSqlSessionTemplate.getDynamicMappedStatementHelper().addParseDynamicMappedStatement(dmsList);
//        }catch (Exception e){
//          trip(dynamicSqlSessionTemplate,"load database mappedStatement error.",e);
//        }finally {
//            DynamicDataSourceHolder.clear();
//        }
//    }
//
//    public void trip(DynamicSqlSessionTemplate dynamicSqlSessionTemplate,String message,Exception e) {
//        DataSource dataSource = dynamicSqlSessionTemplate.getDataSource();
//        if(dataSource == null){
//            logger.error("not found datasource.");
//        }else if(dataSource instanceof DynamicMultipleDataSource){
//            DynamicMultipleDataSource multipleDataSource = (DynamicMultipleDataSource) dataSource;
//            if(CollectionUtils.isEmpty(multipleDataSource.getDataSources())){
//                logger.error("not found datasource.");
//            }else if(multipleDataSource.getDataSources().size() > 1){
//                logger.error("Please check the data source configuration.\n");
//                logger.error(
//                        " ①\n" +
//                                "       application.yml set \"dynamic-mapped-statement.data-source.key=dms\"\n"+
//                                "       @Bean(\"dms\")\n" +
//                                "       @ConfigurationProperties(prefix = \"spring.datasource\")\n" +
//                                "       public DruidDataSource datasource(){\n" +
//                                "           return new DruidDataSource();\n" +
//                                "       }"+
//                                "\nor\n" +
//                                "②\n" +
//                                "           @Primary\n" +
//                                "           @Bean(\"dms\")\n" +
//                                "           @ConfigurationProperties(prefix = \"spring.datasource\")\n" +
//                                "           public DruidDataSource datasource(){\n" +
//                                "               return new DruidDataSource();\n" +
//                                "           }");
//
//            }
//        }else {
//            logger.error("not found datasource.");
//        }
//        logger.error(message,e);
//    }
//}
