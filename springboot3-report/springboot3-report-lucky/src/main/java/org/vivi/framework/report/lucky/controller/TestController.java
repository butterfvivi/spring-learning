package org.vivi.framework.report.lucky.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.report.lucky.common.api.ResponseVO;
import org.vivi.framework.report.lucky.common.config.redis.RedisCacheService;
import org.vivi.framework.report.lucky.common.utils.JsonUtil;
import org.vivi.framework.report.lucky.entity.GridRecordDataModel;
import org.vivi.framework.report.lucky.poiutils.XlsUtil;
import org.vivi.framework.report.lucky.server.JfGridFileGetService;
import org.vivi.framework.report.lucky.server.JfGridUpdateService;
import org.vivi.framework.report.lucky.utils.TimeUtil;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * jar启动
 * java -jar luckysheet.jar
 * 测试类
 * http://localhost:9004/luckysheet/doc.html#/home
 * http://luckysheet.lashuju.com/demo/qkIndex.html
 * http://127.0.0.1:85/luckysheet/demo/
 * http://localhost:9004/luckysheet/test/constant?param=123
 * http://localhost:9004/luckysheet/test/down?listId=xc79500%23-8803%237c45f52b7d01486d88bc53cb17dcd2c3&fileName=list.xls
 * http://localhost:9004/luckysheet/test/down?listId=1079500%23-8803%237c45f52b7d01486d88bc53cb17dcd2xc&fileName=list.xls
 */
@Slf4j
@RestController
@Tag(name = "测试接口")
@RequestMapping("test")
public class TestController {

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private JfGridUpdateService jfGridUpdateService;

    @Autowired
    private JfGridFileGetService jfGridFileGetService;

    @Autowired
    JfGridUpdateService postgresJfGridUpdateService;

    @GetMapping("constant")
    public String getConstant(String param){
        Map<String,String> map=new HashMap<>();
        map.put("threadName",Thread.currentThread().getName());
        map.put("SUCCESS","true");
        map.put("param",param);

        log.info(JsonUtil.toJson(map));
        return JsonUtil.toJson(map);
    }

    @Operation(summary = "redis添加",description = "保存到redis")
    @Parameters({
            @Parameter(name = "key",required = true),
            @Parameter(name = "value", required = true)
    })
    @GetMapping("redis/addCache")
    public ResponseVO addCache(String key, String value){
        redisCacheService.addCache(key,value);
        return ResponseVO.successInstance("ok");
    }
    @Operation(summary = "redis查询",description = "从redis获取")
    @Parameters({
            @Parameter(name = "key", required = true)
    })
    @GetMapping("redis/getCache")
    public ResponseVO getCache(String key){
        return ResponseVO.successInstance(redisCacheService.getCache(key));
    }

    @Operation(summary = "初始化db",description = "初始化db")
    @GetMapping("dbInit")
    public ResponseVO dbInit(){
        jfGridUpdateService.initTestData();
        return ResponseVO.successInstance("success");
    }

    @Operation(summary = "初始化db单个",description = "初始化db单个")
    @GetMapping("dbInit/one")
    public ResponseVO dbInit(String listId){
        List<String> listName=new ArrayList<String>();
        listName.add(listId);
        jfGridUpdateService.initTestData(listName);
        return ResponseVO.successInstance("success");
    }

    @Operation(summary = "获取整个xls结构",description = "初始化db单个")
    @GetMapping("get/LuckySheetJson")
    public ResponseVO getLuckySheetJson(String listId){
        List<JSONObject> list=jfGridFileGetService.getAllSheetByGridKey(listId);
        return ResponseVO.successInstance(list);
    }

    @Operation(summary = "下载xls",description = "下载xls")
    @Parameters({
            @Parameter(name = "fileName", description = "文档名称", required = true),
            @Parameter(name = "listId", description = "文档id",  required = true)
    })
    @GetMapping("down")
    public ResponseVO down(HttpServletResponse response, String fileName, String listId){
        if(fileName==null||listId==null){
            return ResponseVO.errorInstance("参数错误");
        }
        if(!fileName.endsWith(".xls")&&!fileName.endsWith(".xlsx")){
            return ResponseVO.errorInstance("文件扩展名错误");
        }
        boolean isXlsx=false;
        String zipFileName="";
        if(fileName.endsWith(".xlsx")){
            isXlsx=true;
            zipFileName=fileName.substring(0,fileName.length()-5);
        }else{
            zipFileName=fileName.substring(0,fileName.length()-4);
        }
        List<JSONObject> lists=jfGridFileGetService.getAllSheetByGridKey(listId);
        //输出的文件名
        String _fileName= null;
        try {
            _fileName = new String(zipFileName.getBytes("gb2312"), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            return ResponseVO.errorInstance(e.getMessage());
        }
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment;fileName=" +_fileName+".zip" );


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            XlsUtil.exportXlsFile(baos,isXlsx,lists);
        } catch (FileNotFoundException e) {
            return ResponseVO.errorInstance(e.getMessage());
        } catch (IOException e) {
            return ResponseVO.errorInstance(e.getMessage());
        }
        byte [] data = baos.toByteArray();
        try{
            ZipOutputStream out=new ZipOutputStream(response.getOutputStream());
            ZipEntry zipEntry=new ZipEntry(fileName);
            zipEntry.setSize(data.length);
            zipEntry.setTime(System.currentTimeMillis());
            out.putNextEntry(zipEntry);
            //out.setEncoding("GBK");
            out.write(data);
            out.closeEntry();
            out.finish();
            log.info("down ok");
            return null;
        }catch (Exception ex){
            return ResponseVO.errorInstance(ex.getMessage());
        }


    }

    /**
     * @param file EXCEL文件
     * @description 导入EXCEL
     * @author zhouhang
     * @date 2021/4/25
     */
    @PostMapping("/import_excel")
    public ResponseVO importExcel(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        try {
            InputStream inputStream = file.getInputStream();
            if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx")) {
                Workbook workbook = WorkbookFactory.create(inputStream);
                //读取EXCEL
                List<GridRecordDataModel> modelList = XlsUtil.readExcel(workbook);
                String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
                String docCode = TimeUtil.getTodayBeginTime() + "#-" + (int) (Math.random() * 100) + "#-" + UUID.randomUUID().toString().replace("-", "");
                //插入文档数据
                postgresJfGridUpdateService.initImportExcel(modelList, docCode);
                Map<String, String> map = new HashMap<>(2);
                map.put("docName", fileName);
                map.put("docCode", docCode);
                return ResponseVO.successInstance(map);
            } else {
                return ResponseVO.errorInstance("无效文件");
            }
        } catch (Exception e) {
            log.error("", e);
            return ResponseVO.errorInstance(e.getMessage());
        }
    }




}
