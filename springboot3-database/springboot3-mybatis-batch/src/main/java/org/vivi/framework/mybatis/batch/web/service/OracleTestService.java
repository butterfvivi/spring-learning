package org.vivi.framework.mybatis.batch.web.service;

import java.util.Date;

public interface OracleTestService {

    Date getSysdate();

    int count();

    int batch();

    int batch2();

    String unique1();

    String unique2();

    String unique3();

}

