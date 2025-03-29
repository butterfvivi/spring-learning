package org.vivi.framework.ireport.demo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.ireport.demo.common.response.R;
import org.vivi.framework.ireport.demo.service.DataSetService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/dataSet")
public class DataSetController {

    @Autowired
    private DataSetService dataSetService;

    @GetMapping
    public R getDatas(){
        String querySql = "SELECT * FROM user \n" +
                "<where>\n" +
                "<if test=\"gender !=null and gender !=''\">\n" +
                "  gender = #{gender}\n" +
                "  </if>\n" +
                "</where>";
        Map<String, Object> params = new HashMap<>();
        params.put("gender","1");

        List<Map<String, Object>> datas = dataSetService.getData(querySql, params);
        return R.success(datas);
    }
}
