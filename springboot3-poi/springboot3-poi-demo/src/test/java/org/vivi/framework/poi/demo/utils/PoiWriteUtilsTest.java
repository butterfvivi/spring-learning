package org.vivi.framework.poi.demo.utils;



import cn.hutool.core.date.DateTime;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 测试工具
public class PoiWriteUtilsTest {

    public static void main(String[] args) throws IOException {
        PoiWriteUtils utils = new PoiWriteUtils();

        // 数据库字段
        List<String> row = new ArrayList<String>();
        row.add("姓名");
        row.add("年龄");
        row.add("家庭住址");

        // 数据库内容
        List<List<Object>> col = new ArrayList();
        List<Object> col1 = new ArrayList<Object>();
        col1.add("Ht");col1.add("20");col1.add("河北省廊坊市");
        List<Object> col2 = new ArrayList<Object>();
        col2.add("爱神");col2.add("18");col2.add("河北省唐山市");
        col.add(col1);col.add(col2);

        // 路径
        String path = "E:\\enviroment\\git\\cupid-study\\poi-easyexcel\\poi\\excel\\";

        utils.writeExcelByPoiHSSF("测试", "测试表", 0,row,col,path);
    }

    @Test
    public void testBigData() throws IOException {
        PoiWriteUtils utils = new PoiWriteUtils();
        // 大数据 写入
        List<String> row = new ArrayList<String>();
        row.add("姓名");
        row.add("年龄");
        row.add("家庭住址");

        // 文件内容 按集合存记录
        List<List<Object>> col = new ArrayList();
        for (int i = 0; i < 100000; i++){
            List<Object> temp = new ArrayList<Object>();
            temp.add("测试姓名" + i + 1);temp.add(i + 1);temp.add("测试地址" + i + 1);
            col.add(temp);
        }

        String path = "E:\\enviroment\\git\\cupid-study\\poi-easyexcel\\poi\\excel\\";
        utils.writeExcelByPoiSXSSFBigData("大数据测试","大数据测试",0,row,col,path);
        System.out.println("文件生成成功!");
    }

    @Test
    public void testWrite07() throws Exception {
        // 1.创建工作簿
        Workbook workbook = new XSSFWorkbook();
        // 2.创建sheet
        Sheet userSheet = workbook.createSheet("用户角色表");
        Sheet roleSheet = workbook.createSheet("用户权限表");

        // 第一行
        Row userRow1 = userSheet.createRow(0);
        userRow1.createCell(0).setCellValue("账号");
        userRow1.createCell(1).setCellValue("用户名");
        userRow1.createCell(2).setCellValue("角色类别");
        userRow1.createCell(3).setCellValue("操作时间");

        // 测试数据 10 行
        for (int i = 1; i < 11; i++){
            Row row = userSheet.createRow(i);
            row.createCell(0).setCellValue("root" + i);
            row.createCell(1).setCellValue("测试用户" + i);
            row.createCell(2).setCellValue((int)(Math.random()*5 + 1));
            row.createCell(3).setCellValue(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        }

        // 用户权限
        Row roleRow1 = roleSheet.createRow(0);
        roleRow1.createCell(0).setCellValue("权限类别");
        roleRow1.createCell(1).setCellValue("权限名称");

        for (int i = 1; i < 6 ; i++){
            Row row = roleSheet.createRow(i);
            row.createCell(0).setCellValue(i);
            row.createCell(1).setCellValue("测试权限" + i);
        }

        FileOutputStream fileOutputStream = new FileOutputStream( "//excel//用户权限表07.xlsx");
        // 生成文件
        workbook.write(fileOutputStream);

        fileOutputStream.close();
        System.out.println("文件生成成功");

    }
}
