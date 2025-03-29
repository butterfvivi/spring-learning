package org.vivi.framework.ireport.demo.process;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.vivi.framework.ireport.demo.common.constant.Constants;
import org.vivi.framework.ireport.demo.common.enums.SQLDriverEnum;
import org.vivi.framework.ireport.demo.common.exception.BizException;
import org.vivi.framework.ireport.demo.common.mybatis.MybatisTemplateSqlExcutor;
import org.vivi.framework.ireport.demo.common.utils.DateUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcProcess {

    /**
     * 解析sql并获取sql中的列
     */
    public static List<Map<String, Object>> parseMetaDataColumns(DataSource dataSource, String sqlText, int dataSourceType, String sqlParams) {
        List<Map<String, Object>> result = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
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
            //JdbcUtils.releaseJdbcResource(conn, stmt, rs);
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
}
