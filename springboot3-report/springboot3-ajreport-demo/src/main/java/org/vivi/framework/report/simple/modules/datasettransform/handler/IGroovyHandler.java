package org.vivi.framework.report.simple.modules.datasettransform.handler;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface IGroovyHandler {

    List<JSONObject> transform(List<JSONObject> data);
}
