package org.vivi.framework.minio.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.minio.common.enums.ExceptionEnums;
import org.vivi.framework.minio.common.exception.CustomException;
import org.vivi.framework.minio.common.utils.MinioUtils;

import java.util.List;
import java.util.Map;

@RestController
public class MinioController {

    @Autowired
    private MinioUtils minioUtils;

    /**
     * 获取桶中文件名和大小列表
     *
     * @return
     */
    @GetMapping("/getFileList")
    public List<Object> getFileList() {
        return minioUtils.getFileList("test", true);
    }

    /**
     * 判断文件是否存在
     *
     * @param bucketName
     * @param fileName
     * @return
     */
    @GetMapping("/doFileNameExist")
    public boolean doFileNameExist(String bucketName, String fileName) {
        return minioUtils.doFolderExist(bucketName, fileName);
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadFiles")
    public Map<String, Object> uploadFiles(String bucketName, @RequestParam(name = "file", required = false) MultipartFile[] file) {
        if (file == null || file.length == 0) {
            throw new CustomException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        if (StringUtils.isEmpty(bucketName)) {
            throw new CustomException(ExceptionEnums.BUCKET_NAME_NOT_NULL.getMsg());
        }
        return minioUtils.uploadFile(bucketName, file);
    }

    /**
     * 获取上传文件的完整浏览路径
     *
     * @param filename
     * @return
     */
    @GetMapping("/getPresignedObjectUrl")
    public String getPresignedObjectUrl(@RequestParam(name = "filename") String filename) {
        return minioUtils.getPresignedObjectUrl("test", filename, null);
    }

    /**
     * 文件下载
     *
     * @param response
     * @param fileName
     */
    @GetMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response, @RequestParam("fileName") String fileName) {
        minioUtils.downloadFile(response, "test", fileName);
    }

    /**
     * 删除单个文件
     *
     * @param fileName 完整路径（不包含bucket）
     */
    @DeleteMapping("/deleteFile")
    public void deleteFile(String bucketName, String fileName) {
        minioUtils.deleteFile(bucketName, fileName);
    }

    /**
     * 批量删除文件
     *
     * @param fileNames 完整路径（不包含bucket）
     */
    @DeleteMapping("/deleteBatchFile")
    public void deleteBatchFile(String bucketName, @RequestParam("fileNames") List<String> fileNames) {
        minioUtils.deleteBatchFile(bucketName, fileNames);
    }
}

