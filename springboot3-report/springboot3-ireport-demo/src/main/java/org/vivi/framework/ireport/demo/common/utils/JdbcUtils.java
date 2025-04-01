package org.vivi.framework.ireport.demo.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.vivi.framework.ireport.demo.common.constant.Constants;
import org.vivi.framework.ireport.demo.common.enums.SQLDriverEnum;
import org.vivi.framework.ireport.demo.common.exception.BizException;
import org.vivi.framework.ireport.demo.common.mybatis.MybatisTemplateSqlExcutor;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcUtils {

    private static SqlSession sqlSession;

    static {
        SqlSessionFactory sqlSessionFactory = IocUtil.getBean("sqlSessionFactory");
        sqlSession = sqlSessionFactory.openSession();
    }

    /**
     * 解析sql并获取sql中的列
     */
    public static List<Map<String, Object>> parseMetaDataColumns(String sqlText, int dataSourceType, String sqlParams) {
        List<Map<String, Object>> result = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            stmt = sqlSession.getConnection().prepareStatement(sqlText);
            Map<String, Object> params = new HashMap<>();
            if(StringUtils.isNotEmpty(sqlParams))
            {
                JSONArray jsonArray = JSONArray.parseArray(sqlParams);
                if(!CollectionUtils.isEmpty(jsonArray))
                {
                    sqlText = processSqlDynamicParam(sqlText);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String paramType = jsonArray.getJSONObject(i).getString("paramType");
                        String paramDefault = jsonArray.getJSONObject(i).getString("paramDefault");
                        String paramCode = jsonArray.getJSONObject(i).getString("paramCode");
                        String dateFormat = jsonArray.getJSONObject(i).getString("dateFormat");
                        if(StringUtils.isNotEmpty(paramType))
                        {
                            if("date".equals(paramType.toLowerCase()))
                            {
                                params.put(paramCode, StringUtils.isNotEmpty(dateFormat)?DateUtil.getNow(dateFormat): DateUtil.getNow(DateUtil.FORMAT_LONOGRAM));
                            }else if("mutiselect".equals(paramType.toLowerCase()) || "multitreeselect".equals(paramType.toLowerCase()))
                            {
                                params.put(paramCode, new JSONArray());
                            }
                            else {
                                params.put(paramCode, paramDefault);
                            }
                        }else {
                            params.put(paramCode, paramDefault);
                        }

                    }
                }
            }

            rs = stmt.executeQuery(preprocessSqlText(sqlText,dataSourceType,params));
            final ResultSetMetaData rsMataData = rs.getMetaData();
            final int count = rsMataData.getColumnCount();
            result = new ArrayList<>(count);
            for (int i = 1; i <= count; i++) {
                Map<String, Object> column = new HashMap<String, Object>();
                column.put("columnName", rsMataData.getColumnName(i));
                column.put("name", rsMataData.getColumnLabel(i));
                column.put("dataType", rsMataData.getColumnTypeName(i));
                column.put("width", rsMataData.getColumnDisplaySize(i));
                result.add(column);
            }
        } catch (final SQLException ex) {
            throw new BizException("500","error.sql");
        } finally {
            releaseJdbcResource(sqlSession, stmt, rs);
        }
        return result;
    }

    /**
     * 预处理sql，防止sql数据过多,并且处理sql中的参数</p>
     */
    public static String preprocessSqlText(String sqlText,int dataSourceType,Map<String, Object> params) throws SQLException {
        sqlText = StringUtils.stripEnd(sqlText.trim(), ";");
        Pattern paramPattern=Pattern.compile("\\$\\s*\\{(.*?)}");
        Matcher parammatcher=paramPattern.matcher(sqlText);
        while(parammatcher.find()){
            sqlText = parammatcher.replaceAll("''");
        }
        sqlText = MybatisTemplateSqlExcutor.parseSql(sqlText,params);
        System.err.println("解析后的sql:"+sqlText);
        if(SQLDriverEnum.MYSQL.getCode().intValue() == dataSourceType)
        {
            final Pattern pattern = Pattern.compile(".*limit\\s+(\\S+?)($|;|\\s+.+)", Pattern.CASE_INSENSITIVE);
            final Matcher matcher = pattern.matcher(sqlText);
            if (matcher.find()) {

            }else {
                sqlText = sqlText + " limit 1";
            }

        }else if(SQLDriverEnum.ORACLE.getCode().intValue() == dataSourceType)
        {
            sqlText = Constants.ORACLE_START + sqlText + Constants.ORACLE_END;
        }

        return sqlText;
    }

    /**
     * 释放数据库资源
     */
    public static void releaseJdbcResource(final SqlSession session, final Statement stmt, final ResultSet rs) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (session != null) {
                session.close();
            }
        } catch (final SQLException ex) {
            throw new BizException("500","数据库资源释放异常");
        }
    }

    /**
     * getCountSql
     * 查询结果的总数sql
     */
    public static String getCountSql(String sql)
    {
        sql = sql.replaceAll(";", "");
        sql = "select count(*) from (" + sql + ") alis_t";
        return sql;
    }

    /**
     * 处理sql中的参数
     */
    public static String processSqlParams(String sql,Map<String, Object> params)  {
        if(params != null)
        {
            try {
                sql = MybatisTemplateSqlExcutor.parseSql(sql, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sql;
    }

    /**
     *  sql添加分页条件
     */
    public static String getPaginationSql(String sql,int dataSourceType,int pageCount,int currentPage)  {
        sql = sql.replaceAll(";", "");
        if(SQLDriverEnum.MYSQL.getCode().intValue() == dataSourceType)
        {
            sql = sql + " limit " + (currentPage-1)*pageCount + "," + pageCount;
        }else if(SQLDriverEnum.ORACLE.getCode().intValue() == dataSourceType)
        {
            sql = "select tmp_page.*, rownum rowno from (" + sql + ") tmp_page";
            sql = "select * from (" + sql + ") where rowno <= " + currentPage*pageCount + " and rowno > " + (currentPage-1)*pageCount;
        }
        return sql;
    }

    public static String getPaginationSql(String sql,int dataSourceType,int pageCount,int startPage,int endPage)  {
        sql = sql.replaceAll(";", "");
        if(SQLDriverEnum.MYSQL.getCode().intValue() == dataSourceType)
        {
            sql = sql + " limit " + (endPage-startPage+1)*pageCount + " offset " + (startPage-1)*pageCount;
        }else if(SQLDriverEnum.ORACLE.getCode().intValue() == dataSourceType)
        {
            sql = "select tmp_page.*, rownum rowno from (" + sql + ") tmp_page";
            sql = "select * from (" + sql + ") where rowno <= " + (endPage-startPage+1)*pageCount + " and rowno > " + (startPage-1)*pageCount;
        }

        return sql;
    }

    /**
     * 处理sql中的动态参数，去掉空格
     */
    public static String processSqlDynamicParam(String sqlText)
    {
        List<String> matchGroup = new ArrayList<String>();
        Pattern paramPattern=Pattern.compile("\\$\\s*\\{(.*?)}");
        Matcher parammatcher=paramPattern.matcher(sqlText);
        while(parammatcher.find()){
            matchGroup.add(parammatcher.group());
        }
        if(!CollectionUtils.isEmpty(matchGroup))
        {
            for (int i = 0; i < matchGroup.size(); i++) {
                String original = matchGroup.get(i);
                String newParam = matchGroup.get(i).replaceAll(" ", "");
                sqlText = sqlText.replace(original, newParam);
            }
        }
        return sqlText;
    }

    /**
     * sql分页处理
     */
    public static String processSqlPage(String sqlText, int pageSize,int currentPage)
    {
        sqlText = StringUtils.stripEnd(sqlText.trim(), ";");
        final Pattern pattern = Pattern.compile("limit.*?$", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(sqlText);
        if (matcher.find()) {
            sqlText = matcher.replaceFirst("");
        }
        return sqlText + " limit " + String.valueOf((currentPage-1)*pageSize) + ","+ String.valueOf(pageSize);
    }

    /**
     * 处理页面传入的参数
     */
    public static Map<String, Object> processPageParams(Map<String, Object> pageParams,String tplParams)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(tplParams))
        {
            JSONArray jsonArray = JSONObject.parseArray(tplParams);
            if(!CollectionUtils.isEmpty(jsonArray))
            {
                for (int i = 0; i < jsonArray.size(); i++) {
                    if(jsonArray.getJSONObject(i).get("paramCode") != null && StringUtils.isNotEmpty(String.valueOf(jsonArray.getJSONObject(i).get("paramCode"))))
                    {
                        if(pageParams != null)
                        {
                            if(pageParams.containsKey(jsonArray.getJSONObject(i).get("paramCode")))
                            {
                                result.put(String.valueOf(jsonArray.getJSONObject(i).get("paramCode")), pageParams.get(jsonArray.getJSONObject(i).get("paramCode")));
                            }else {
                                if(jsonArray.getJSONObject(i).get("paramDefault") != null && StringUtils.isNotEmpty(String.valueOf(jsonArray.getJSONObject(i).get("paramDefault"))))
                                {
                                    result.put(String.valueOf(jsonArray.getJSONObject(i).get("paramCode")), String.valueOf(jsonArray.getJSONObject(i).get("paramDefault")));
                                }else {
                                    result.put(String.valueOf(jsonArray.getJSONObject(i).get("paramCode")), "");
                                }
                            }
                        }else if(jsonArray.getJSONObject(i).get("paramDefault") != null && StringUtils.isNotEmpty(String.valueOf(jsonArray.getJSONObject(i).get("paramDefault"))))
                        {
                            result.put(String.valueOf(jsonArray.getJSONObject(i).get("paramCode")), String.valueOf(jsonArray.getJSONObject(i).get("paramDefault")));
                        }
                    }
                }
            }
        }
        return result;
    }
}
