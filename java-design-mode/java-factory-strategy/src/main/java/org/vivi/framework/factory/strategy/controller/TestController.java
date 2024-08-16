package org.vivi.framework.factory.strategy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.factory.strategy.strategy.ExportFactory;
import org.vivi.framework.factory.strategy.strategy.ExportService;
import org.vivi.framework.factory.strategy.strategy.ExportStategyContext;

import java.util.List;

@RestController
@RequestMapping
public class TestController {

    @Autowired
    private ExportStategyContext context;

    @PostMapping("/test1")
    public List testList1(String code) {
        try {
            //获取对应的服务
            ExportService exportService = ExportFactory.getService(code);
            return exportService.exportData();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/test2")
    public Object testList2(String code) {
        return context.useStrategy(code);
    }
}
