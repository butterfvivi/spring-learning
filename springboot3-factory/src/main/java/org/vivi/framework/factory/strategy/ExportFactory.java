package org.vivi.framework.factory.strategy;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.vivi.framework.factory.enums.ExportEnum;
import org.vivi.framework.factory.utils.IocUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExportFactory {

    /**
     * 类路径目录
     */
    private static final String CLASS_PATH = "org.vivi.framework.factory.service.";

    /**
     * 服务实现后缀
     */
    private static final String EXPORT_SERVICE_SUFFIX = "Export";

    private static final String EXPORT_METHOD_SUFFIX = "exportData";


    private static final Map<String, ExportService> HOTEL_SERVER_MAP = new ConcurrentHashMap<>();

    /**
     * 初始化实现类到COMPANY_SERVER_MAP中
     */
    static {
        ExportEnum.getList().forEach(v -> {
            String className = CLASS_PATH + v + EXPORT_SERVICE_SUFFIX;
            try {
                HOTEL_SERVER_MAP.put(v, (ExportService) Class.forName(className).newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 服务实现
     * @param code
     * @return
     */
    public static ExportService getService(String code) {
        return HOTEL_SERVER_MAP.get(code);
    }

    public static ExportService getServerImpl(String code) throws ClassNotFoundException {
        //Class clazz = Class.forName(CLASS_PATH + code + EXPORT_SERVICE_SUFFIX);
        //String className = EXPORT_METHOD_SUFFIX;
        //Object bean = IocUtil.getBean(code + EXPORT_SERVICE_SUFFIX);
        //Method method = ReflectionUtils.findMethod(bean.getClass(), className);
        //RefectionUtil.invokeMethod(bean, EXPORT_METHOD_SUFFIX);
        return IocUtil.getBean(code + EXPORT_SERVICE_SUFFIX);
    }
}
