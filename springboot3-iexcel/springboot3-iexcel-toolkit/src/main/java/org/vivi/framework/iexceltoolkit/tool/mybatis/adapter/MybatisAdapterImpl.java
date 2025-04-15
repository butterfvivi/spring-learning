package org.vivi.framework.iexceltoolkit.tool.mybatis.adapter;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.iexceltoolkit.common.utils.SpringContextUtil;
import org.vivi.framework.iexceltoolkit.tool.mybatis.handler.ResultSetCallbackHandler;
import org.vivi.framework.iexceltoolkit.tool.mybatis.interfaces.IDAOAdapter;
import org.vivi.framework.iexceltoolkit.mapper.ISqlMapper;

import java.util.List;
import java.util.Map;

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
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) SpringContextUtil.getBean("sqlSessionFactory");
            SqlSession sqlSession = sqlSessionFactory.openSession();
            return resultSetCallbackHandler.getExportList(clazz, sqlSession, sql, params);
        }
    }


    @Override
    public List getListMapBySql(Map<String,List> sqlInfo) throws Exception {
        return sqlMapper.selectList(sqlInfo);
    }

    @Override
    public void disRegist() {

    }

}
