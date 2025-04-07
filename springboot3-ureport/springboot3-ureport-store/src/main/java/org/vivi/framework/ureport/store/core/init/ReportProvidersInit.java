package org.vivi.framework.ureport.store.core.init;

import org.vivi.framework.ureport.store.core.provider.report.ReportProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ReportProvidersInit implements ApplicationContextAware {

    private List<ReportProvider> reportProviders = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        reportProviders.addAll(applicationContext.getBeansOfType(ReportProvider.class).values());

    }

    public List<ReportProvider> getReportProviders() {
        return reportProviders;
    }

}
