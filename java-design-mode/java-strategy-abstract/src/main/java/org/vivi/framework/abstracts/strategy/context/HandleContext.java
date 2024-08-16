package org.vivi.framework.abstracts.strategy.context;

import org.vivi.framework.abstracts.strategy.model.RequestDto;
import org.vivi.framework.abstracts.strategy.utils.BeanTool;

import java.util.Map;

/**
 * 抽象出一个公共handle对象
 */
public class HandleContext {

    private Map<String, Class> handlerMap;

    public HandleContext(Map<String, Class> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public AbstractExportHandle getInstance(RequestDto param) {
        Class clazz = handlerMap.get(param.getType());
        if (clazz == null) {
            throw new IllegalArgumentException("not found handler for type : " + param.getType());
        }
        return (AbstractExportHandle) BeanTool.getBean(clazz);
    }

}
