package org.vivi.framework.iasyncexcel.starter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Factory for creating and managing the Excel context
 */
public class ExcelContextFactory implements DisposableBean, ApplicationContextAware {

    private final String propertySourceName;
    private final String propertyName;
    private final String contextName = "excelContext";
    private final Class<?> defaultConfigType;

    public static Set<String> basePackages;

    private volatile AnnotationConfigApplicationContext context;
    private ApplicationContext parent;
    private final ReentrantLock contextLock = new ReentrantLock();

    public ExcelContextFactory(Class<?> defaultConfigType, String propertySourceName,
                               String propertyName) {
        this.defaultConfigType = Objects.requireNonNull(defaultConfigType, "Config type cannot be null");
        this.propertySourceName = propertySourceName;
        this.propertyName = propertyName;
    }

    @Override
    public void setApplicationContext(ApplicationContext parent) throws BeansException {
        this.parent = parent;
    }

    @Override
    public void destroy() {
        if (this.context != null) {
            this.context.close();
        }
    }

    /**
     * Get the context, creating it if necessary
     */
    protected AnnotationConfigApplicationContext getContext() {
        if (this.context == null) {
            contextLock.lock();
            try {
                if (this.context == null) {
                    this.context = createContext();
                }
            } finally {
                contextLock.unlock();
            }
        }
        return this.context;
    }

    /**
     * Create a new annotation config application context
     */
    protected AnnotationConfigApplicationContext createContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(PropertyPlaceholderAutoConfiguration.class, this.defaultConfigType);

        context.getEnvironment().getPropertySources().addFirst(new MapPropertySource(
                this.propertySourceName,
                Collections.singletonMap(this.propertyName, this.contextName)));

        if (this.parent != null) {
            ExcelHandleBasePackages ehbp = this.parent.getBean(ExcelHandleBasePackages.class);
            basePackages = ehbp.getBasePackages();
            context.setParent(this.parent);
            context.setClassLoader(this.parent.getClassLoader());
        }
        context.setDisplayName(generateDisplayName(this.contextName));
        context.refresh();
        return context;
    }

    protected String generateDisplayName(String name) {
        return this.getClass().getSimpleName() + "-" + name;
    }

    /**
     * Get an instance of the specified type from the context
     */
    public <T> T getInstance(Class<T> type) {
        AnnotationConfigApplicationContext context = getContext();
        if (BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context, type).length > 0) {
            return context.getBean(type);
        }
        return null;
    }
}