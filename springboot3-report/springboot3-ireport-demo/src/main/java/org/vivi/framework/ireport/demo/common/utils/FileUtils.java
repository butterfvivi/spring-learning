package org.vivi.framework.ireport.demo.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.vivi.framework.ireport.demo.report.achieve.FileUtilsCore;
import org.vivi.framework.ireport.demo.report.dto.AnalysisZipFileDataDto;
import org.vivi.framework.ireport.demo.report.dto.DownloadFileDto;
import org.vivi.framework.ireport.demo.report.dto.DownloadFileZipDto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 *  文件相关
 */
public class FileUtils {
    private static FileUtilsCore fileUtilsCore = IocUtil.getBean(FileUtilsCore.class);

    /**
     * 根据url下载文件
     */
    public static void downloadFile(HttpServletResponse response, DownloadFileDto dto)  {
        try {
            fileUtilsCore.downloadFile(response, dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 把文件下载成zip
     */
    public static void downloadFileZip(HttpServletResponse response, DownloadFileZipDto dto){
        try {
            fileUtilsCore.downloadFileZip(response, dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从下载链接中提取，文件的zip中的json数据
     */
    public static Map<String, Object> analysisZipFileData(AnalysisZipFileDataDto dto) {
        try {
            return fileUtilsCore.analysisZipFileData(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取resources目录下文件输入流
     * 读取resources目录下文件输入流。relativePath@相对项目下resources目录的路径，比如 /static/1.txt
     */
    public static InputStream getResourcesFile(String relativePath) {
        try {
            return FileUtilsCore.getResourcesFile(relativePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * InputStream inputStream 转 File file对象。会在内存中创建一个临时变量
     */
    public static File inputStreamConversionFile(InputStream inputStream, String fileSuffix) {
        return FileUtilsCore.inputStreamConversionFile(inputStream, fileSuffix);
    }

    /**
     * 读取文件流中的数据，返回String
     */
    public static String readInputStreamAsString(InputStream inputStream) {
        try {
            return FileUtilsCore.readInputStreamAsString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
