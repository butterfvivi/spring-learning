package org.vivi.framework.ireport.demo.common.mybatis;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;

public class MybatisTemplateSqlExcutor {

    /**  
     *  脚本模板
     */
    private static final String SCRIPT_TEMPLATE = "<script>\n%s\n</script>";
    
    /**  
     * @Fields EMPTY_XML : 空MP配置模板，用于构建MP环境配置，（放这里是由于博客的编辑器识别问题，会导致高亮错误）
     */
    private static final String EMPTY_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" //
            + "<!DOCTYPE configuration\r\n" //
            + " PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\"\r\n" //
            + " \"http://mybatis.org/dtd/mybatis-3-config.dtd\">\r\n"//
            + "<configuration>\r\n" //
            + "</configuration>";
    
    /**
     * MP环境配置
     */
    private static Configuration configuration;
    
    static {
    	  InputStream inputStream = new ByteArrayInputStream(EMPTY_XML.getBytes(StandardCharsets.UTF_8));
          XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(inputStream, null, null);
          configuration = xmlConfigBuilder.parse();
    }
    
    /**
     * 从MP复制过来的脚本解析方法
     */
    private static SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType) {
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }
    
    /**  
     * parseSql
     * 解析sql
     */
    public static String parseSql(String sqlTemplate, Map<String, Object> params) throws SQLException {
    	 String script = String.format(SCRIPT_TEMPLATE, sqlTemplate);
    	 XPathParser parser = new XPathParser(script, false, new Properties(), new XMLMapperEntityResolver());
    	 SqlSource source = createSqlSource(configuration, parser.evalNode("/script"), Map.class);
    	 BoundSql boundSql = source.getBoundSql(params);
    	 List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
         Object parameterObject = boundSql.getParameterObject();
         String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
         if(!CollectionUtils.isEmpty(parameterMappings))
         {
        	 MetaObject metaObject = configuration.newMetaObject(parameterObject);
        	 for (ParameterMapping parameterMapping : parameterMappings)
        	 {
        		 String propertyName = parameterMapping.getProperty();
        		 if (metaObject.hasGetter(propertyName))
        		 {
        			 Object obj = metaObject.getValue(propertyName);
        		     sql = sql.replaceFirst("\\?",Matcher.quoteReplacement(getParameterValue(obj)));
        		 }else if (boundSql.hasAdditionalParameter(propertyName))
        	     {
        		      // 该分支是动态sql
        		      Object obj = boundSql.getAdditionalParameter(propertyName);
        		      sql = sql.replaceFirst("\\?",Matcher.quoteReplacement(getParameterValue(obj)));
        		 }else
        		 {// 打印出缺失，提醒该参数缺失并防止错位
        		      sql = sql.replaceFirst("\\?", "缺失");
        		 }
        	 }
         }
         return sql;
    }
    
	/**  
	 *  getParameterValue
	 *  获取参数值
	 */
	private static String getParameterValue(Object obj) {
		String value = null;
		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format(new Date()) + "'";
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "";
			}
		}
		return value;
	}

}
