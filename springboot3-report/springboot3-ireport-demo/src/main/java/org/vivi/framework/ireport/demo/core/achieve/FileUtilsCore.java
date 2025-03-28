package org.vivi.framework.ireport.demo.core.achieve;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.vivi.framework.ireport.demo.common.annotation.IToolKit;
import org.vivi.framework.ireport.demo.core.dto.DownloadFileDto;
import org.vivi.framework.ireport.demo.utils.AssertUtils;
import org.vivi.framework.ireport.demo.utils.EmptyUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static org.vivi.framework.ireport.demo.common.constant.Constants.buffer;
import static org.vivi.framework.ireport.demo.common.constant.Message.*;

public class FileUtilsCore {


    /**
     * 根据url下载文件
     * 把file转换成InputStream流，把InputStream流内容读取到BufferedOutputStream中。
     */
    @IToolKit
    public void downloadFile(HttpServletResponse response, DownloadFileDto dto) throws Exception {
        checkDownloadParams(dto);
        InputStream inputStream = getFileInputStream(dto);
        //开始下载操作
        setDownloadResponse(response, dto);
        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        writeFile(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }


    /**
     * read the file input stream under the resources directory
     * @param relativePath relative path of the file under the resources directory
     */

    public static InputStream getResourcesFile(String relativePath) throws IOException {
        Resource resource = new ClassPathResource(relativePath);
        InputStream inputStream = resource.getInputStream();
        return inputStream;
    }

    /**
     * 写入文件流
     **/
    private static void writeFile(InputStream inputStream, OutputStream outputStream) throws Exception {
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
    }

    /**
     * 根据路径获取到文件流，路径包含url、相对路径、绝对路径三种方式
     **/
    private static InputStream getFileInputStream(DownloadFileDto dto) throws Exception {
        InputStream inputStream = null;
        String path = dto.getPath();
        //url下载
        if ("0".equals(dto.getPathType()) || path.contains("http://") || path.contains("https://")) {
            URL url = new URL(path);
            URLConnection conn = url.openConnection();
            inputStream = conn.getInputStream();
        }
        //springboot项目下的 resources文件下载，比如/static/1.txt
        else if ("1".equals(dto.getPathType())) {
            inputStream = getResourcesFile(path);

        }
        //绝对路径下载，比如D:\home\1.txt
        else if ("2".equals(dto.getPathType())) {
            File file = new File(path);
            if (!file.exists()) AssertUtils.throwInnerException(NO_FILE + "请检查路径【" + path + "】下是否有所需文件！");
            inputStream = new FileInputStream(file);
        }
        if (inputStream == null) {
            AssertUtils.throwInnerException("无法根据路径【" + path + "】获取文件流！请检查有没有指定路径类型参数pathType值？0-url , 1-resources , 2-绝对路径");
        }
        return inputStream;
    }

    /**
     * 设置好文件
     **/
    private static void setDownloadResponse(HttpServletResponse response, DownloadFileDto dto) throws UnsupportedEncodingException {
        response.reset();
        //文件类型自动判断
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("UTF-8");
        //将Content-Disposition暴露给前端
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        String fileName = EmptyUtils.isEmpty(dto.getName()) ? System.currentTimeMillis() + "" : dto.getName();
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + "." + dto.getSuffix(), "UTF-8"));

    }

    /**
     * 检查下载参数
     **/
    private static void checkDownloadParams(DownloadFileDto dto) {
        if (EmptyUtils.isEmpty(dto.getPath())) AssertUtils.throwInnerException(NO_PATH);
        if (EmptyUtils.isEmpty(dto.getSuffix())) AssertUtils.throwInnerException(NO_SUFFIX);
    }
}
