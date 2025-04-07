package org.vivi.framework.ureport.store.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.vivi.framework.ureport.store.domain.bo.UreportFileBo;
import org.vivi.framework.ureport.store.service.IUreportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ureportfile")
public class UreportFileController {

    @Autowired
    private IUreportService iUreportService;

    @GetMapping("getUreportFileList")
    public Map getUreportFileList(UreportFileBo bo){
//        {
//            "code": 0,
//                "msg": "",
//                "count": 1000,
//                "data": [{}, {}]
//        }

        Page ureportFileList = iUreportService.getUreportFileList(bo);

        Map result =new HashMap();
        result.put("code",0);
        result.put("msg","");
        result.put("count",ureportFileList.getTotal());
        result.put("data",ureportFileList.getRecords());

        return result;
    }
}
