package org.vivi.framework.excel.dynamic4.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.write.handler.WriteHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vivi.framework.excel.dynamic4.service.IEasyExcelService;

import java.net.URLEncoder;
import java.util.List;

@Service
@Slf4j
public class EasyExcelServiceImpl implements IEasyExcelService {

    private static final String ENCODING_UTF_8 = "UTF-8";

    private static final String FILE_END = ".xlsx";

    private static final String CONTENT_DISPOSITION = "Content-Disposition";

    private static final String CONTENT_DISPOSITION_VALUE = "attachment;filename=";

    private static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


    /**
     * 本地转：response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileNameEncoder + ".xlsx");
     * @param exportData 需要导出的数据
     * @param response   response
     * @param tClass     导出excel的字段实体类
     * @param fileName   文件名字
     * @param sheetName  sheet名字
     * @param <T> T
     */
    @Override
    public <T> void exportExcel(List<T> exportData, HttpServletResponse response, Class<T> tClass, String fileName, String sheetName){
        // 使用swagger 会导致各种问题，直接用浏览器或者用postman
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(ENCODING_UTF_8);
        try{
            // fileName encoder
            String fileNameEncoder = URLEncoder.encode(fileName, ENCODING_UTF_8).replaceAll("\\+", "%20");
            response.setHeader(CONTENT_DISPOSITION, CONTENT_DISPOSITION_VALUE + fileNameEncoder + FILE_END);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            // write to excel
            EasyExcelFactory.write(response.getOutputStream(), tClass)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet(sheetName)
                    .doWrite(exportData);
        }catch (Exception e){
            log.error("EasyExcelServiceImpl->exportExcel error, message is :{}", e.getMessage());
        }
    }

    @Override
    public <T> void exportExcelWithHandler(List<T> exportData, HttpServletResponse response, Class<T> tClass,
                                           String fileName, String sheetName, WriteHandler writeHandler){
        // 使用swagger 会导致各种问题，直接用浏览器或者用postman
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(ENCODING_UTF_8);
        try{
            // fileName encoder
            String fileNameEncoder = URLEncoder.encode(fileName, ENCODING_UTF_8).replaceAll("\\+", "%20");
            response.setHeader(CONTENT_DISPOSITION, CONTENT_DISPOSITION_VALUE + fileNameEncoder + FILE_END);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            // write to excel
            EasyExcelFactory.write(response.getOutputStream(), tClass)
                    .autoCloseStream(Boolean.FALSE)
                    // 自定义策略(支持扩展多个)
                    .registerWriteHandler(writeHandler)
                    .sheet(sheetName)
                    .doWrite(exportData);
        }catch (Exception e){
            log.error("EasyExcelServiceImpl->exportExcel error, message is :{}", e.getMessage());
        }
    }

    @Override
    public <T> void exportExcelWithDynamicsHead(HttpServletResponse response, List<List<String>> head, List<List<Object>> data,
                                                String fileName, String sheetName, WriteHandler writeHandler){
        // 使用swagger 会导致各种问题，直接用浏览器或者用postman
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(ENCODING_UTF_8);
        try {
            // fileName encoder
            String fileNameEncoder = URLEncoder.encode(fileName, ENCODING_UTF_8).replaceAll("\\+", "%20");
            response.setHeader(CONTENT_DISPOSITION, CONTENT_DISPOSITION_VALUE + fileNameEncoder + FILE_END);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            // write to excel
            EasyExcelFactory.write(response.getOutputStream())
                    .head(head)
                    .sheet(sheetName)
                    // handle
                    .registerWriteHandler(writeHandler)
                    .doWrite(data);
        } catch (Exception e) {
            log.error("EasyExcelServiceImpl->exportExcelWithHandlerAndHead error, message is :{}", e.getMessage());
        }
    }

}
