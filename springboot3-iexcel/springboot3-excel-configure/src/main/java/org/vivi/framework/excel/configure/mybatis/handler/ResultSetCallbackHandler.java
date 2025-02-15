package org.vivi.framework.excel.configure.mybatis.handler;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.vivi.framework.excel.configure.common.enums.FieldTypeEnum;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ResultSetCallbackHandler {

    /**
     * 自定义实现将数据库返回字段映射为javaEntity
     * @param clazz
     * @param sqlSession
     * @param sql
     * @param objects
     * @return
     * @throws Exception
     */
    public List<Object> getExportList(Class<?> clazz, SqlSession sqlSession, String sql, Object... objects) throws Exception {
        PreparedStatement preparedStatement = sqlSession.getConnection().prepareStatement(sql);

        if(objects != null && objects.length > 0 ){
            int i = 1;
            for (Object object : objects) {
                if (object instanceof String) {
                    preparedStatement.setString(i, object.toString());
                } else if (object instanceof Date) {
                    preparedStatement.setObject(i, object);
                } else if (object instanceof Long) {
                    preparedStatement.setLong(i, (Long) object);
                } else if (object instanceof Integer) {
                    preparedStatement.setInt(i, (Integer) object);
                }
                i++;
            }
        }

        ResultSet set = preparedStatement.executeQuery();

        List<Object> list = new ArrayList<>();

        Map<String, Integer> typeMap = FieldTypeEnum.getMap();

        Field[] fields = clazz.getDeclaredFields();
        while (set.next()) {
            Object t = clazz.getConstructor(new Class[]{}).newInstance();
            for (int j = 0; j < fields.length; ++j) {
                Field field = fields[j];

                String getMethodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                int index = typeMap.get(field.getGenericType().getTypeName());
                Method setMethod = clazz.getMethod(getMethodName,
                        new Class[]{FieldTypeEnum.getType(index)});
                Object value = getResultSetValue(set,index,field.getName());
                // 调用对象的setXXX方法
                setMethod.invoke(t, new Object[]{value});
            }
            list.add(t);
        }
        sqlSession.close();
        return list;
    }

    private Object getResultSetValue(ResultSet rs, int colType, String fieldName) throws SQLException {
        Object value = null;

        switch (colType) {
            //int --- Integer
            case 3:
                value = rs.getInt(fieldName);
                break;
            case 4:
                value = rs.getInt(fieldName);
                break;

            //String
            case 5:
                value = rs.getString(fieldName);
                break;
            //long --- Long
            case 10:
                value = rs.getLong(fieldName);
                break;
            case 11:
                value = rs.getLong(fieldName);
                break;
            //Date
            case 14:
                value = rs.getDate(fieldName);
                break;
            default:
                System.out.println("error......");

        }
        return value;
    }
}
