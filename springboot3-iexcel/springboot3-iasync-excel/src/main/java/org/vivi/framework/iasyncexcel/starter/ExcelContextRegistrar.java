package org.vivi.framework.iasyncexcel.starter;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.vivi.framework.iasyncexcel.core.annotation.ExcelHandle;

import java.util.Set;

public class ExcelContextRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware,
        EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        registerExcelHandle(importingClassMetadata,registry);
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }


    public void registerExcelHandle(AnnotationMetadata metadata,
                                    BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ExcelHandle.class));

        for (String basePackage : ExcelContextFactory.basePackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            candidateComponents.stream()
                .filter(AnnotatedBeanDefinition.class::isInstance)
                .map(AnnotatedBeanDefinition.class::cast)
                .forEach(beanDefinition -> {
                    String name = BeanDefinitionReaderUtils.generateBeanName(beanDefinition, registry);
                    BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, name);
                    BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
                });
        }
    }

}