package org.vivi.framework.abstracts.strategy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.abstracts.strategy.model.RequestDto;
import org.vivi.framework.abstracts.strategy.service.ExportService;

import java.util.List;

@RestController
@RequestMapping
public class TestController {

    @Autowired
    private ExportService exportService;

    @PostMapping(value = "/export")
    public Object exportTest(@RequestBody RequestDto dto){
        return exportService.exportData(dto);
    }

}
