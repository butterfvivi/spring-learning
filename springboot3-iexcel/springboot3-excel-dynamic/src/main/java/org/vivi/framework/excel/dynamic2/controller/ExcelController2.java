package org.vivi.framework.excel.dynamic2.controller;

import com.alibaba.fastjson.JSONArray;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.excel.dynamic2.utils.ExcelUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
@Controller()
@RequestMapping("/excel2")
public class ExcelController2 {

    @PostMapping("/import")
    public JSONArray importUser(@RequestPart("file") MultipartFile file) throws Exception {
        JSONArray array = ExcelUtils.readMultipartFile(file);
        System.out.println("导入数据为:" + array);
        return array;
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        // 表头数据
        List<Object> head = Arrays.asList("姓名","年龄","性别","头像");
        // 用户1数据
        List<Object> user1 = new ArrayList<>();
        user1.add("诸葛亮");
        user1.add(60);
        user1.add("男");
        user1.add("https://profile.csdnimg.cn/A/7/3/3_sunnyzyq");
        // 用户2数据
        List<Object> user2 = new ArrayList<>();
        user2.add("大乔");
        user2.add(28);
        user2.add("女");
        user2.add("https://profile.csdnimg.cn/6/1/9/0_m0_48717371");
        // 将数据汇总
        List<List<Object>> sheetDataList = new ArrayList<>();
        sheetDataList.add(head);
        sheetDataList.add(user1);
        sheetDataList.add(user2);
        // 导出数据
        ExcelUtils.export(response,"用户表", sheetDataList);
    }
}
