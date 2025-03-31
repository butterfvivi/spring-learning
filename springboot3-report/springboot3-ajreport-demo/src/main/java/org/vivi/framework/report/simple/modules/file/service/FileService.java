package org.vivi.framework.report.simple.modules.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.report.simple.modules.file.entity.IFile;

/**
 * (IFile)Service
 */
public interface FileService extends IService<IFile> {

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

    /**
     * 根据fileId显示图片或者下载文件
     */
    ResponseEntity<byte[]> download(HttpServletRequest request, HttpServletResponse response, String fileId);

    /**
     * 获取文件
     * @param fileId
     * @return
     */
    byte[] getFile(String fileId);
}
