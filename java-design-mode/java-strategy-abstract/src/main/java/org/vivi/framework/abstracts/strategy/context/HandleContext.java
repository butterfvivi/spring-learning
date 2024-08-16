package org.vivi.framework.abstracts.strategy.context;

import org.vivi.framework.abstracts.strategy.model.RequestDto;
import org.vivi.framework.abstracts.strategy.utils.BeanTool;

import java.util.Map;

/**
 * 抽象出一个公共handle对象
 *  抽象出来一个处理所有产品的公共HandlerContext对象，此对象提供一个获取具体类的方法，
 *  该方法有个入参用于表明是具体的类型，同时该HandlerContext对象还具有Map类型的属性变量
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
