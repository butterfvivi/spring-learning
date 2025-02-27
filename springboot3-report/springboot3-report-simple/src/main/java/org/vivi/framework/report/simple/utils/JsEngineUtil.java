package org.vivi.framework.report.simple.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class JsEngineUtil {

    final Set<String> blackList = Sets.newHashSet(
            "java.lang.ProcessBuilder", "java.lang.Runtime", "java.lang.ProcessImpl");
    ThreadLocal<Context> engineHolder = new NamedInheritableThreadLocal<Context>("jsEngine") {
        @Override
        protected Context initialValue() {
            return Context.newBuilder("js")
                    .allowAllAccess(false)
                    .build();
        }
    };

    public Context getEngine() {
        return engineHolder.get();
    }

    private String filter(String input) {
        // Replace blacklisted classes with empty strings
        for (String clz : blackList) {
            input = input.replace(clz, "");
        }
        return input;
    }

    public List<JSONObject> eval(String js, List<JSONObject> data) throws Exception {
        Context context = getEngine();
        context.eval("js", filter(js));
        Value dataTransform = context.getBindings("js").getMember("dataTransform");
        if (dataTransform.canExecute()) {
            Value result = dataTransform.execute(data);
            List<JSONObject> resultList = new ArrayList<>();
            for (Object value : result.as(List.class)) {
                resultList.add(JSONObject.parseObject(value.toString()));
            }
            return resultList;
        }
        return null;
    }

    public Object verification(String validationRules, Object dataSetParamDto) throws Exception {
        Context context = getEngine();
        context.eval("js", filter(validationRules));
        Value verification = context.getBindings("js").getMember("verification");
        if (verification.canExecute()) {
            Value exec = verification.execute(dataSetParamDto);
            ObjectMapper objectMapper = new ObjectMapper();
            if (exec.isBoolean()) {
                return objectMapper.convertValue(exec.asBoolean(), Boolean.class);
            } else {
                return objectMapper.convertValue(exec.asString(), String.class);
            }
        }
        return null;
    }
}