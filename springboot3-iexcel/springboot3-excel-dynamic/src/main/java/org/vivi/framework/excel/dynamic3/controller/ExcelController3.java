package org.vivi.framework.excel.dynamic3.controller;

import com.alibaba.excel.util.IoUtils;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.excel.dynamic3.utils.DynamicEasyExcelExportUtils;

import java.io.*;
import java.util.*;

import static org.vivi.framework.excel.dynamic3.utils.DynamicEasyExcelImportUtils.parseExcelToView;

/**
 * 基于EasyExcel提供的动态参数化生成文件和动态监听器读取文件方法，单独封装一套动态导出导出工具类，减少需要重新编写大量重复工作
 */
@RestController
@RequestMapping("/excel3")
public class ExcelController3 {

    @PostMapping("/import")
    public void importExcel() throws IOException {
        FileInputStream inputStream = new FileInputStream(new File("E:\\IdeaProjects\\springboot3-learning\\springboot3-iexcel\\file\\easyexcel-export-user5.xlsx"));
        byte[] stream = IoUtils.toByteArray(inputStream);
        List<Map<String,String>> dataList = parseExcelToView(stream, 2);
        System.out.println(JSONArray.toJSONString(dataList));
        inputStream.close();
    }


    @GetMapping("/export")
    public void export() throws IOException {
        //导出包含数据内容的文件（方式一）
        LinkedHashMap<String, String> headColumnMap = Maps.newLinkedHashMap();
        headColumnMap.put("className","班级");
        headColumnMap.put("name","学生信息,姓名");
        headColumnMap.put("sex","学生信息,性别");
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> dataMap = Maps.newHashMap();
            dataMap.put("className", "一年级");
            dataMap.put("name", "张三" + i);
            dataMap.put("sex", "男");
            dataList.add(dataMap);
        }
        byte[] stream1 = DynamicEasyExcelExportUtils.exportExcelFile(headColumnMap, dataList);
        FileOutputStream outputStream1 = new FileOutputStream(new File("E:\\IdeaProjects\\springboot3-learning\\springboot3-iexcel\\file\\easyexcel-export-user5.xlsx"));
        outputStream1.write(stream1);
        outputStream1.close();

        //导出包含数据内容的文件（方式二）
        //头部，第一层
        List<String> head1 = new ArrayList<>();
        head1.add("第一行头部列1");
        head1.add("第一行头部列1");
        head1.add("第一行头部列1");
        head1.add("第一行头部列1");
        //头部，第二层
        List<String> head2 = new ArrayList<>();
        head2.add("第二行头部列1");
        head2.add("第二行头部列1");
        head2.add("第二行头部列2");
        head2.add("第二行头部列2");
        //头部，第三层
        List<String> head3 = new ArrayList<>();
        head3.add("第三行头部列1");
        head3.add("第三行头部列2");
        head3.add("第三行头部列3");
        head3.add("第三行头部列4");

        //封装头部
        List<List<String>> allHead = new ArrayList<>();
        allHead.add(head1);
        allHead.add(head2);
        allHead.add(head3);

        //封装数据体
        //第一行数据
        List<Object> data1 = Lists.newArrayList(1,1,1,1);
        //第二行数据
        List<Object> data2 = Lists.newArrayList(2,2,2,2);
        List<List<Object>> allData = Lists.newArrayList(data1, data2);

        byte[] stream2 = DynamicEasyExcelExportUtils.customerExportExcelFile(allHead, allData);
        FileOutputStream outputStream2 = new FileOutputStream(new File("E:\\IdeaProjects\\springboot3-learning\\springboot3-iexcel\\file\\easyexcel-export-user6.xlsx"));
        outputStream2.write(stream2);
        outputStream2.close();


    }
}
