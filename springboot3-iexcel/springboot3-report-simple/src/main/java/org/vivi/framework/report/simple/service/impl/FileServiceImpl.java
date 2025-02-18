package org.vivi.framework.report.simple.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.report.simple.common.exception.BusinessExceptionBuilder;
import org.vivi.framework.report.simple.entity.file.IFile;
import org.vivi.framework.report.simple.mapper.FileMapper;
import org.vivi.framework.report.simple.service.FileService;
import java.io.File;

import java.util.UUID;

/**
 * 文件管理服务实现
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${spring.gaea.subscribes.oss.downloadPath:''}")
    private String fileDownloadPath;

    //@Autowired
    //private GaeaOSSTemplate gaeaOSSTemplate;

    @Autowired
    private FileMapper fileMapper;

    /**
     * 文件上传
     *
     * @param multipartFile 文件
     * @return
     */
    @Override
    public IFile upload(MultipartFile multipartFile) {
        String originalFilename =  multipartFile.getOriginalFilename();

        if (StringUtils.isBlank(originalFilename)) {
            throw BusinessExceptionBuilder.build("404");
        }
        // 文件后缀 .png
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 生成文件唯一性标识
        String fileId = UUID.randomUUID().toString();

        // 生成在oss中存储的文件名 402b6193e70e40a9bf5b73a78ea1e8ab.png
        String fileObjectName = fileId + suffixName;
        // 生成链接通过fileId http访问路径 http://10.108.3.121:9089/meta/file/download/402b6193e70e40a9bf5b73a78ea1e8ab
        String urlPath = fileDownloadPath + "/" + fileId;

        // 上传文件
//        try{
//            gaeaOSSTemplate.uploadFileByInputStream(multipartFile, fileObjectName);
//        }catch (GaeaOSSTypeLimitedException e){
//            log.error("上传失败GaeaOSSTypeLimitedException", e);
//            throw BusinessExceptionBuilder.build(ResponseCode.FILE_SUFFIX_UNSUPPORTED, e.getMessage());
//        }catch (GaeaOSSException e){
//            log.error("上传失败GaeaOSSException", e);
//            throw BusinessExceptionBuilder.build(ResponseCode.FILE_UPLOAD_ERROR, e.getMessage());
//        }

        // 保存到文件管理中
        IFile iFile = new IFile();
        iFile.setFileId(fileId);
        iFile.setFilePath(fileObjectName);
        iFile.setUrlPath(urlPath);
        iFile.setFileType(suffixName.replace(".", ""));
        iFile.setFileInstruction(originalFilename);
        fileMapper.insert(iFile);

        return iFile;
    }

    /**
     * 文件上传
     *
     * @param file           文件
     * @return
     */
    @Override
    public IFile upload(File file) {
        return upload(file);
    }

}
