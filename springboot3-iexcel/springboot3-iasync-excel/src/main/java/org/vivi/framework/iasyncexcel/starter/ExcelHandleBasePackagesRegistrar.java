package org.vivi.framework.iasyncexcel.starter;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExcelHandleBasePackagesRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        Set<String> basePackages = getBasePackages(importingClassMetadata);
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(ExcelHandleBasePackages.class);
        definition.addConstructorArgValue(basePackages);
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        String name = BeanDefinitionReaderUtils.generateBeanName(beanDefinition, registry);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition,name);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableAsyncExcel.class.getCanonicalName());
        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }
}
