package org.vivi.framework.lucky.mongodb.service;

import cn.hutool.json.JSONObject;


public interface IMessageProcess {


    /**
     * 对updateurl发来的信息进行处理
     * @param message
     * @return
     */
    void process(String gridKey,JSONObject message);
}
