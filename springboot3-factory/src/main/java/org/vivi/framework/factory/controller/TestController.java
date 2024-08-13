package org.vivi.framework.factory.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.factory.strategy.ExportFactory;
import org.vivi.framework.factory.strategy.ExportService;

import java.util.List;

@RestController
@RequestMapping
public class TestController {

    @PostMapping("/")
    public List testList(String code) {
        try {
            //获取对应的服务
            ExportService exportService = ExportFactory.getService(code);
            return exportService.exportData();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
