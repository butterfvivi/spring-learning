package org.vivi.framework.excel.configure.common.utils;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.sql.Connection;
import java.util.List;

public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(Class T) {
        try {
            return applicationContext.getBean(T);
        } catch (BeansException e) {
            return null;
        }
    }

    public static Object getBean(String name) {
        try {
            return applicationContext.getBean(name);
        } catch (BeansException e) {
            return null;
        }
    }

    public int updateBatch(List<Object> list){
        if (list == null || list.size() <= 0) {
            return -1;
        }

        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) SpringContextUtil.getBean("sqlSessionFactory");
        SqlSession sqlSession = null;

        try{
            sqlSession = sqlSessionFactory.openSession(ExecutorType.SIMPLE);
            Connection connection = sqlSession.getConnection();
            return 0;
        }catch (Exception e){
            sqlSession.rollback();
            return -2;
        }finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
