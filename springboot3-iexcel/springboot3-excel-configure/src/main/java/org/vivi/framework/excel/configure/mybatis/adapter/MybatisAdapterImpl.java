package org.vivi.framework.excel.configure.mybatis.adapter;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.excel.configure.common.utils.SpringContextUtil;
import org.vivi.framework.excel.configure.base.interfaces.IDAOAdapter;
import org.vivi.framework.excel.configure.mybatis.handler.ResultSetCallbackHandler;
import org.vivi.framework.excel.configure.mybatis.mapper.ISqlMapper;

import java.util.List;

@Service
public class MybatisAdapterImpl implements IDAOAdapter {

    @Autowired
    private ISqlMapper sqlMapper;

    @Autowired
    private ResultSetCallbackHandler resultSetCallbackHandler;


    @Override
    public void registerDAO(Object daoObject) {

    }

    @Override
    public int getCount(String s, Object... param) throws Exception {
        String sql = getNewSql(s, param);
        return sqlMapper.getCount(sql);
    }

    private String getNewSql(String sql,Object... objects){
        if (objects == null) {
            return sql;
        }
        String newSql = sql;
        for (Object object : objects) {
            if (object instanceof String){
                String strParams = "'" + object.toString()+"'";
                newSql = newSql.replaceFirst("\\?",strParams);
            }else {
                newSql = newSql.replaceFirst("\\?",object.toString());
            }
        }
        return null;
    }

    @Override
    public List getListBySql(Class<?> clazz, String sql, Object... params) throws Exception {
        if (clazz == null) {
            return sqlMapper.selectList(sql);
        } else {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory)SpringContextUtil.getBean("sqlSessionFactory");
            SqlSession sqlSession = sqlSessionFactory.openSession();
            return resultSetCallbackHandler.getExportList(clazz, sqlSession, sql, params);
        }
    }

    @Override
    public void disRegist() {

    }

}
