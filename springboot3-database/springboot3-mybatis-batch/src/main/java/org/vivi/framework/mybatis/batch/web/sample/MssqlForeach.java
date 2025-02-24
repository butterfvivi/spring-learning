package org.vivi.framework.mybatis.batch.web.sample;

import org.apache.ibatis.session.SqlSessionFactory;
import org.vivi.framework.mybatis.batch.utils.SimpleSampleUtils;

public class MssqlForeach {

    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = SimpleSampleUtils.getSqlSessionFactory("mssql");
        SimpleSampleUtils.stressForeach(sqlSessionFactory);
    }

}
