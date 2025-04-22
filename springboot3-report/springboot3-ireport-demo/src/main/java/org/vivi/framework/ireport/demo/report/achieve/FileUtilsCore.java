package org.vivi.framework.ireport.demo.report.achieve;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.vivi.framework.ireport.demo.common.constant.Message;
import org.vivi.framework.ireport.demo.common.utils.AssertUtils;
import org.vivi.framework.ireport.demo.common.utils.EmptyUtils;
import org.vivi.framework.ireport.demo.report.dto.AnalysisZipFileDataDto;
import org.vivi.framework.ireport.demo.report.dto.DownloadFileDto;
import org.vivi.framework.ireport.demo.report.dto.DownloadFileZipDto;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 文件相关
 * <p>
 * 1.根据url下载文件
 * 2.读取resources目录下文件输入流
 * 3.读取绝对路径下文件输入流
 * 4.读取相对路径下文件输入流
 */
public class FileUtilsCore {


    private static byte[] buffer = new byte[1024 * 1024 * 10];

    /**
     * 根据url下载文件
     * 把file转换成InputStream流，把InputStream流内容读取到BufferedOutputStream中。
     */
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

    /***
     * 打包下载Zip
     */
    public void downloadFileZip(HttpServletResponse response, DownloadFileZipDto zipDto) throws Exception {
        List<DownloadFileDto> dtoList = zipDto.getData();
        AssertUtils.objIsNull(dtoList, Message.NO_FILE_LIST);
        //文件后缀
        if (dtoList.stream().filter(t -> EmptyUtils.isEmpty(t.getSuffix())).collect(Collectors.toList()).size() != 0) {
            AssertUtils.throwInnerException(Message.NO_FILE_LIST_SUFFIX);
        }
        //创建压缩流关联压缩包  ZipOutputStream类：完成文件或文件夹的压缩
        String zipName = EmptyUtils.isNotEmpty(zipDto.getZipName()) ? zipDto.getZipName() : System.currentTimeMillis() + "";
        setDownloadResponse(response, new DownloadFileDto(zipName, "zip"));
        ZipOutputStream zipStream = new ZipOutputStream(response.getOutputStream());
        for (DownloadFileDto dto : dtoList) {
            InputStream inputStream = getFileInputStream(dto);
            //创建ZipEntry对象，并为每个文件命名，放入压缩包当中
            String fileName = (EmptyUtils.isEmpty(dto.getName()) ? System.currentTimeMillis() + "" : dto.getName()) + "." + dto.getSuffix();
            ZipEntry ze = new ZipEntry(fileName);
            zipStream.putNextEntry(ze);
            writeFile(inputStream, zipStream);
            inputStream.close();
            zipStream.closeEntry();
        }
        zipStream.close();
    }

    public Map<String, Object> analysisZipFileData(AnalysisZipFileDataDto dto) throws Exception {
        String path = dto.getPath();
        AssertUtils.objIsNull(path, Message.NO_PATH);
        String analysisType = dto.getAnalysisType();
        AssertUtils.objIsNull(analysisType, Message.NO_ANALYSIS_TYPE);
        //返回值
        Map<String, Object> resultMap = new HashMap<>();
        //获取文件流
        InputStream inputStream = getFileInputStream(new DownloadFileDto(dto.getPath(), dto.getPathType(), null, null));
        File fileTemp = inputStreamConversionFile(inputStream, "zip");
//        ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(fileTemp);
//        //是否需密码提取
//        String zipPassword = dto.getPassword();
//        if (MsEmptyUtils.isNotEmpty(zipPassword)) zipFile.setPassword(zipPassword);
//        // 获取zip文件中所有文件的信息
//        for (Object fileHeader : zipFile.getFileHeaders()) {
//            if (fileHeader instanceof FileHeader) {
//                FileHeader header = (FileHeader) fileHeader;
//                String fileName = header.getFileName();
//                String suffix = MsConvertDataUtils.getSplitStrEnd(fileName, ".");
//                if (analysisType.contains(suffix)) {
//                    InputStream fileInputStream = zipFile.getInputStream(header);
//                    //解析文件内容
//                    String fileContent = readInputStreamAsString(fileInputStream);
//                    //如果是json，转换
//                    if (suffix.equals("json")) {
//                        resultMap.put(fileName, MsConvertDataUtils.strToMap(fileContent));
//                    } else {
//                        resultMap.put(fileName, fileContent);
//                    }
//                }
//            }
//        }
        return resultMap;
    }


    /**
     * 读取resources目录下文件输入流
     *
     * @param relativePath 相对项目下resources目录的路径，比如 /static/1.txt
     */

    public static InputStream getResourcesFile(String relativePath) throws IOException {
        Resource resource = new ClassPathResource(relativePath);
        InputStream inputStream = resource.getInputStream();
        return inputStream;
    }

    /**
     * InputStream inputStream 转 File file对象。会在内存中创建一个临时变量
     *
     * @param fileSuffix  文件后缀
     */
    public static File inputStreamConversionFile(InputStream inputStream, String fileSuffix) {
        FileOutputStream outputStream = null;
        try {
            // 创建临时文件
            File tempFile = File.createTempFile("msTemp", "." + fileSuffix);
            // 使用临时文件的路径创建 FileOutputStream
            outputStream = new FileOutputStream(tempFile);
            // 从 InputStream 读取数据，并写入到临时文件
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 读取流中的文件数据
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String readInputStreamAsString(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
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
            if (!file.exists()) AssertUtils.throwInnerException(Message.NO_FILE + "请检查路径【" + path + "】下是否有所需文件！");
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
        if (EmptyUtils.isEmpty(dto.getPath())) AssertUtils.throwInnerException(Message.NO_PATH);
        if (EmptyUtils.isEmpty(dto.getSuffix())) AssertUtils.throwInnerException(Message.NO_SUFFIX);
    }

}
