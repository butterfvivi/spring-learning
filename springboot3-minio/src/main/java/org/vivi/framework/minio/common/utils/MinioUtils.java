package org.vivi.framework.minio.common.utils;

import com.alibaba.fastjson.JSON;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.minio.common.MinioProperties;
import org.vivi.framework.minio.common.enums.ExceptionEnums;
import org.vivi.framework.minio.common.exception.CustomException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Minio 工具类
 */
@Component
@Slf4j
public class MinioUtils {


    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;
    /**
     * 初始化Bucket
     */
    private void createBucket(String bucketName) {
        try {
            // 判断 BucketName 是否存在
            if (bucketExists(bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证bucketName是否存在
     *
     * @return boolean true:存在
     */
    public boolean bucketExists(String bucketName) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new CustomException(ExceptionEnums.BUCKET_NAME_NOT_NULL.getMsg());
        }
        boolean flag = true;
        try {
            flag = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 获取全部bucket
     * <p>
     */
    public List<String> getAllBuckets() {
        List<String> list = null;
        try {
            final List<Bucket> buckets = minioClient.listBuckets();
            list = new ArrayList<>(buckets.size());
            for (Bucket bucket : buckets) {
                list.add(bucket.name());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据bucketName获取信息
     *
     * @param bucketName bucket名称
     * @return
     */
    public String getBucket(String bucketName) throws Exception {
        final Optional<Bucket> first = minioClient.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
        String name = null;
        if (first.isPresent()) {
            name = first.get().name();
        }
        return name;
    }

    /**
     * 获取桶中文件名和大小列表
     *
     * @param bucketName bucket名称
     * @param recursive  查询是否递归
     * @return
     */
    public List<Object> getFileList(String bucketName, boolean recursive) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new CustomException(ExceptionEnums.BUCKET_NAME_NOT_NULL.getMsg());
        }
        List<Object> items = new ArrayList<>();
        try {
            Iterable<Result<Item>> myObjects = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix("/2022-08-03/4674a894-abaf-48cb-9ea9-40a4e8560af9/Desktop").recursive(recursive).build());
            Iterator<Result<Item>> iterator = myObjects.iterator();
            String format = "{'fileName':'%s','fileSize':'%s'}";
            for (Result<Item> myObject : myObjects) {
                System.out.println(myObject.get().objectName());
            }
            while (iterator.hasNext()) {
                Item item = iterator.next().get();
                items.add(JSON.parse(String.format(format, item.objectName(), formatFileSize(item.size()))));
//                items.add(JSON.parse(String.format(format, "/".concat("test").concat("/").concat(item.objectName()), formatFileSize(item.size()))));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        items.remove(0);
        return items;
    }


    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public Map<String, Object> uploadFile(String bucketName, MultipartFile[] file) {
        if (file == null || file.length == 0) {
            throw new CustomException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        if (StringUtils.isEmpty(bucketName)) {
            throw new CustomException(ExceptionEnums.BUCKET_NAME_NOT_NULL.getMsg());
        }

        List<String> orgfileNameList = new ArrayList<>(file.length);
        for (MultipartFile multipartFile : file) {
            String orgfileName = multipartFile.getOriginalFilename();
            orgfileNameList.add(orgfileName);
            try {
                //文件上传
                InputStream in = multipartFile.getInputStream();
                minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(orgfileName).stream(in, multipartFile.getSize(), -1).contentType(multipartFile.getContentType()).build());
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("bucketName", bucketName);
        data.put("fileName", orgfileNameList);
        return data;
    }

    /**
     * 获取上传文件的完整路径
     *
     * @param bucketName 桶名称
     * @param fileName   文件名
     * @param expire     地址过期时间
     * @return
     */
    public String getPresignedObjectUrl(String bucketName, String fileName, Integer expire) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new CustomException(ExceptionEnums.BUCKET_NAME_NOT_NULL.getMsg());
        }
        if (StringUtils.isEmpty(fileName)) {
            throw new CustomException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        expire = Objects.isNull(expire) ? minioProperties.getExpire() : expire;
        // 验证桶是否存在在
        final boolean validationBucket = bucketExists(bucketName);
        if (!validationBucket) {
            throw new CustomException(ExceptionEnums.BUCKET_NOT_EXIST.getMsg());
        }
        // 验证文件是否存在
        final boolean validationFileName = doFileNameExist(bucketName, fileName);
        if (!validationFileName) {
            throw new CustomException(ExceptionEnums.FILE_NOT_EXIST.getMsg());
        }
        String url = null;
        try {
            // 获取桶和文件的完整路径
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(fileName).method(Method.GET).expiry(expire).build());
        } catch (MinioException e) {
            log.error("Error occurred: " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 创建文件夹或目录
     *
     * @param bucketName 存储桶
     * @param objectName 目录路径
     */
    public Map<String, String> putDirObject(String bucketName, String objectName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        // 判断桶是否存在
        if (!bucketExists(bucketName)) {
            throw new CustomException(ExceptionEnums.BUCKET_NAME_NOT_EXIST.getMsg());
        }
        final ObjectWriteResponse response = minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                                new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());
        Map<String, String> map = new HashMap<>();
        map.put("etag", response.etag());
        map.put("versionId", response.versionId());
        return map;
    }

    /**
     * 判断桶是否存在
     *
     * @param bucketName 存储桶
     * @param objectName 文件夹名称（去掉/）
     * @return true：存在
     */
    public boolean doFolderExist(String bucketName, String objectName) {
        boolean exist = false;
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).prefix(objectName).recursive(false).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.isDir()) {
                    exist = true;
                }
            }
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName 对象
     * @return true：存在
     */
    public boolean doFileNameExist(String bucketName, String fileName) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new CustomException(ExceptionEnums.BUCKET_NAME_NOT_NULL.getMsg());
        }
        if (StringUtils.isEmpty(fileName)) {
            throw new CustomException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

    /**
     * 文件下载
     *
     * @param response
     * @param fileName
     */
    public void downloadFile(HttpServletResponse response, String bucketName, String fileName) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new CustomException(ExceptionEnums.BUCKET_NAME_NOT_NULL.getMsg());
        }
        if (StringUtils.isEmpty(fileName)) {
            throw new CustomException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        // 判断文件是否存在
        final boolean flag = doFileNameExist(bucketName, fileName);
        if (!flag) {
            throw new CustomException(ExceptionEnums.FILE_NOT_EXIST.getMsg());
        }
        InputStream in = null;
        try {
            // 获取对象信息
            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build());
            response.setContentType(stat.contentType());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            //文件下载
            in = minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }
    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param fileName   文件名称
     *                   说明：当前方法不能真正删除，需要验证
     */
    public void deleteFile(String bucketName, String fileName) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new CustomException(ExceptionEnums.BUCKET_NAME_NOT_NULL.getMsg());
        }
        if (StringUtils.isEmpty(fileName)) {
            throw new CustomException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 批量文件删除
     *
     * @param bucketName bucket名称
     * @param fileNames  文件名
     */
    public void deleteBatchFile(String bucketName, List<String> fileNames) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new CustomException(ExceptionEnums.BUCKET_NAME_NOT_NULL.getMsg());
        }
        if (CollectionUtils.isEmpty(fileNames)) {
            throw new CustomException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        try {
            List<DeleteObject> objects = new LinkedList<>();
            for (String fileName : fileNames) {
                objects.add(new DeleteObject(fileName));
            }
            Iterable<Result<DeleteError>> results =
                    minioClient.removeObjects(
                            RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                log.error("Error occurred: " + error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("批量删除失败！error:{}", e);
        }
    }

    /**
     * 文件大小
     *
     * @param fileS
     * @return
     */
    private static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + " B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + " KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + " MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + " GB";
        }
        return fileSizeString;
    }
}
