package org.vivi.framework.abstracts.strategy.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.vivi.framework.abstracts.strategy.annotation.HandleType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 加载所有产品类的全局process类，用于扫描加了注解@HandlerType的所有实现产品,
 * 给存储key 和 value产品对象Map赋值，初始化HandlerContext 将其注册到spring容器中
 */
@Component
public class HandleProcessor implements BeanFactoryPostProcessor {

    private static final String HANDLE_PACKAGE = "org.vivi.framework.abstracts.strategy";

    /**
     * 扫描HandleType，初始化HandleContext，将其注册到spring容器中
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String,Class> handleMap = new HashMap<>();

        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(HandleType.class));
        Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents(HANDLE_PACKAGE);

        beanDefinitions.forEach(beanClass -> {
            try {
                Class<?> clazz = Class.forName(beanClass.getBeanClassName());
                //获取注解中的类型值
                String type = clazz.getAnnotation(HandleType.class).value();
                //将注解中的类型作为key，对应的类作为value保存在Map中
                handleMap.put(type,clazz);
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        });

        //初始化HandleContext类，将其注入到spring容器中
        HandleContext handleContext = new HandleContext(handleMap);
        beanFactory.registerSingleton(HandleContext.class.getName(),handleContext);
    }
}
