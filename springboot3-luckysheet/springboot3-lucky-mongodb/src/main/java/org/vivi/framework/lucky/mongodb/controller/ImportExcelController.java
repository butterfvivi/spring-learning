
/** 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.vivi.framework.lucky.mongodb.controller;


import cn.hutool.json.JSONUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vivi.framework.lucky.mongodb.common.AjaxResult;
import org.vivi.framework.lucky.mongodb.entity.WorkBookEntity;
import org.vivi.framework.lucky.mongodb.entity.WorkSheetEntity;
import org.vivi.framework.lucky.mongodb.repository.WorkBookRepository;
import org.vivi.framework.lucky.mongodb.repository.WorkSheetRepository;
import org.vivi.framework.lucky.mongodb.utils.SheetUtil;

import java.io.IOException;

@Controller
public class ImportExcelController {

    @Autowired
    private WorkBookRepository workBookRepository;

    @Autowired
    private WorkSheetRepository workSheetRepository;

    // 接受前端传来的exceldata",存到数据库中 并返回dataid



    @ResponseBody
    @PostMapping("/excel/importFile")
    public AjaxResult importExcelFile(@RequestParam(value = "exceldata") String exceldata, HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {
        // 去除luckysheet中 &#xA的换行
        exceldata = exceldata.replace("&#xA;", "\\r\\n");
//实例化一个Gson对象
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(exceldata);
//        cn.hutool.json.JSONArray jsonArray = JSONUtil.parseArray(exceldata);

        cn.hutool.json.JSONObject info= JSONUtil.parseObj(jsonObject.get("info"));
        cn.hutool.json.JSONArray sheetJsonarry= JSONUtil.parseArray(jsonObject.get("sheets"));
        WorkBookEntity wb = new WorkBookEntity();
        wb.setName(info.get("name").toString().replace(".xlsx", ""));
        wb.setOption(SheetUtil.getDefautOption(info));
        WorkBookEntity saveWb = workBookRepository.save(wb);
        sheetJsonarry.forEach(sheet -> {
            WorkSheetEntity ws = new WorkSheetEntity();
            ws.setWbId(saveWb.getId());
            cn.hutool.json.JSONObject jsonObjects = JSONUtil.parseObj(sheet);
            ws.setData(jsonObjects);
            ws.setDeleteStatus(0);
            workSheetRepository.save(ws);
        });
//        System.out.println(jsonObject);
//
////            JSONObject jsonObj = JSONObject.fromObject(exceldata);




            
//转为相应的实体对象

//        JsonRootBean jsonRootBean = gson.fromJson(exceldata, JsonRootBean.class);
           //字符串去除 .xlsx


          // 将json数据转为jsonArray


        //转换为json格式
//        WorkBookEntity wb = new WorkBookEntity();
//        wb.setName(jsonRootBean.getInfo().getName().replace(".xlsx", ""));
//        wb.setOption(SheetUtil.getDefautOption(jsonRootBean));
//        WorkBookEntity saveWb = workBookRepository.save(wb);
//        //生成sheet数据
//        jsonRootBean.getSheets().forEach(sheet -> {
//            WorkSheetEntity ws = new WorkSheetEntity();
//            ws.setWbId(saveWb.getId());
//            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(sheet);
//            ws.setData(jsonObject);
//            ws.setDeleteStatus(0);
//            workSheetRepository.save(ws);
//        });
//        return new AjaxResult(200, "success", saveWb.getId().toString());
        return new AjaxResult(200, "success", saveWb.getId().toString());



    }
//    @PostMapping("/excel/importFile")
//    public JSONObject importExcelFile(@RequestParam(value = "exceldata") String exceldata) {
//                Gson gson = new Gson();
//        JSONObject jsonObj = JSONObject.fromObject(exceldata);
////转为相应的实体对象
//
//        JsonRootBean jsonRootBean = gson.fromJson(jsonObj.toString(), JsonRootBean.class);
//           //字符串去除 .xlsx
//
//
//          // 将json数据转为jsonArray
//
//
//        //转换为json格式
//        WorkBookEntity wb = new WorkBookEntity();
//        wb.setName(jsonRootBean.getInfo().getName().replace(".xlsx", ""));
//        wb.setOption(SheetUtil.getDefautOption(jsonRootBean));
//        WorkBookEntity saveWb = workBookRepository.save(wb);
//        //生成sheet数据
//        jsonRootBean.getSheets().forEach(sheet -> {
//            WorkSheetEntity ws = new WorkSheetEntity();
//            ws.setWbId(saveWb.getId());
//            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(sheet);
//            ws.setData(jsonObject);
//            ws.setDeleteStatus(0);
//            workSheetRepository.save(ws);
//        });
//
//        //写一个json字符串，返回给前端
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("id", saveWb.getId());
//
//        return jsonObject;
//
//
//
//
//
//    }


    @GetMapping("/import")
    public String importExcel() {
        return "import";
    }


}
