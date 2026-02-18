package org.vivi.framework.minio.controller;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.minio.common.config.MinioProper;
import org.vivi.framework.minio.common.response.R;
import org.vivi.framework.minio.common.utils.MinIOUtils1;

@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private MinioProper minioProper;

    @PostMapping("uploadFace")
    public R upload(@RequestParam MultipartFile file,
                    String userId) throws Exception {

        if (StringUtils.isBlank(userId)) {

            return R.failed("文件上传失败");
        }

        String filename = file.getOriginalFilename();

        if (StringUtils.isBlank(filename)) {

            return R.failed("文件上传失败");
        }

        //filename = "face" +  "/" + userId + "/" + filename;

        MinIOUtils1.uploadFile(minioProper.getBucketName(), filename, file.getInputStream());

        String faceUrl = minioProper.getFileHost()
                + "/"
                + minioProper.getBucketName()
                + "/"
                + filename;

        //return R.success(faceUrl);
        return R.success(filename);
    }

    @PostMapping("isFolderExist")
    public R isFolderExist(String bucketName, String fileName) throws Exception {
        boolean folderExist = MinIOUtils1.isObjectExist(bucketName, fileName);
        return R.success(folderExist);
    }
}
