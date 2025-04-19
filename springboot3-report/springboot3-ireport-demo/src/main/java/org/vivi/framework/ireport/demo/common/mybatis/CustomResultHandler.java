package org.vivi.framework.ireport.demo.common.mybatis;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class CustomResultHandler implements ResultHandler<Map<String, Object>> {

    private final List<Map<String, Object>> resultList = new ArrayList<>();

    @Override
    public void handleResult(ResultContext<? extends Map<String, Object>> resultContext) {
        Map<String, Object> row = new LinkedHashMap<>();
        resultContext.getResultObject().forEach((key, value) -> {
            row.put(key, value != null ? value : ""); // 将 null 替换为空字符串
        });
        resultList.add(row);
    }

    public List<Map<String, Object>> getResultList() {
        return resultList;
    }
}