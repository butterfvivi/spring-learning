package org.vivi.framework.iexcelsimple.toolkit.process;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.vivi.framework.iexcelsimple.toolkit.cache.MCache;
import org.vivi.framework.iexcelsimple.common.enums.Constant;
import org.vivi.framework.iexcelsimple.common.utils.ConvertDataUtils;
import org.vivi.framework.iexcelsimple.common.utils.EmptyUtils;
import org.vivi.framework.iexcelsimple.common.utils.EnDeCryptUtils;
import org.vivi.framework.iexcelsimple.toolkit.achieve.ExcelInvokeCore;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: ImportBeanProcess
 * @Description:使用ImportBeanProcess动态的进行bean注册。控制工具类是否能正常使用
 */
public class ImportBeanProcess implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static Environment environment;
    //获取 spring.datasource.url配置
    private static final String MS_URL = "l2qCUl2t/Gwbdk5waICIQ0PDnY6iv4cGZN7fmKUjUKU=";
    //ms.config.safe-check=  true，抛出异常
    private static final String MS_SAFE_CHECK = "tfBAvZev/wVo+5VkX466UmynJI/g886VNfqavzH3lwE=";
    //allowUse：一旦在jdbc的url加上(allowUse = true)，那么utils.toolkit.achieve包下所有的实现的bean全部注入不到ioc容器。会抛出异常。
    private static final String MS_ENABLE = "pBdn2X4Qnb6HzXeEVJcWLA==";
    //allowEmpty：一旦在jdbc的url加上(allowEmpty = true)，那么utils.toolkit.achieve包下所有的处理数据的方法结合Aop，不进行任何数据处理
    private static final String MS_EMPTY1 = "cuk6oSeT0IW3DZ82v6F/6A==";
    //spring.datasource.hikari.maximum-idle-size = 3，此值只有为3时候，才会生效所有工具包的内容
    private static final String MS_EMPTY2 = "l2qCUl2t/Gwbdk5waICIQ/zrHg/X8rpVSBhXoBZBAz1hJbWjD59GxDfvlls5FZLd";
    private static final String BEAN_NAME_E = "excelInvokeCore";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (check()) register(registry);
    }

    /**
     * 检查是否允许使用jar全部包功能，默认是允许的，只有当allowUse=true或者ms.config.save-check!=false时候才会禁用
     **/
    private boolean check() {
        String safe = environment.getProperty(EnDeCryptUtils.decrypt(MS_SAFE_CHECK));
        if ("false".equals(safe)) {
            return true;
        }
        //把数据进行加密
        Map<String, String> newMap = ConvertDataUtils.parseURLParameters(environment.getProperty(EnDeCryptUtils.decrypt(MS_URL))).entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> EnDeCryptUtils.encrypt(entry.getKey()), // 对key添加字符串
                        Map.Entry::getValue // 保持原有的值
                ));
        //对值进行一定的判断
        MCache.othersCache.put(Constant.MS_UT_EMPTY2, EmptyUtils.ifNullSetDefVal(environment.getProperty(EnDeCryptUtils.decrypt(MS_EMPTY2)), "0"));
        if (newMap.size() != 0) {
            String msEnable = MapUtils.getString(newMap, MS_ENABLE, "false");
            MCache.othersCache.put(Constant.MS_UT_ENABLE, msEnable);
            MCache.othersCache.put(Constant.MS_UT_EMPTY1, MapUtils.getString(newMap, MS_EMPTY1, "false"));
            if ("true".equals(MapUtils.getString(newMap, MS_ENABLE))) return false;
        }
        return true;
    }

    private void register(BeanDefinitionRegistry registry) {
        registerExcel(registry);
    }

    private void registerExcel(BeanDefinitionRegistry registry) {
        registry.registerBeanDefinition(BEAN_NAME_E, new RootBeanDefinition(ExcelInvokeCore.class));
    }

    @Override
    public void setEnvironment(Environment environment) {

    }
}
