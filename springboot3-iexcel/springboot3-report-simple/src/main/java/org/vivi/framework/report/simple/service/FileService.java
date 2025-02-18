package org.vivi.framework.report.simple.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.report.simple.entity.file.IFile;

/**
 * (IFile)Service
 */
public interface FileService {

    /**
     * 文件上传
     *
     * @param multipartFile  文件
     * @return
     */
    IFile upload(MultipartFile multipartFile);


    /**
     * 文件上传
     *
     * @param file 二选一
     * @return
     */
    IFile upload(java.io.File file);

}
