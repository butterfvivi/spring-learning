package org.vivi.framework.report.bigdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.vivi.framework.report.bigdata.entity.Demo;
import org.vivi.framework.report.bigdata.paging1.service.GenericExportService;
import org.vivi.framework.report.bigdata.paging1.service.LambdaExportService;

public interface DemoService1 extends IService<Demo>, LambdaExportService<Demo>, GenericExportService<Demo> {


}
