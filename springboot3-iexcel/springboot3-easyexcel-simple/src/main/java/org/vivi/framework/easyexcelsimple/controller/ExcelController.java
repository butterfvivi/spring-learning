package org.vivi.framework.easyexcelsimple.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
public class ExcelController {

    @RequestMapping(value = "/import")
    public void importExcel(MultipartFile file){

    }

    @RequestMapping(value = "/export")
    public void exportExcel(){

    }

    @RequestMapping(value = "/dowload")
    public void dowload(){

    }
}
