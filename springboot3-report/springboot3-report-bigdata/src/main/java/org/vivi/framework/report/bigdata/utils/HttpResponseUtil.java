package org.vivi.framework.report.bigdata.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * http响应实效
 */
@UtilityClass
public class HttpResponseUtil {

    public static OutputStream excelOutput(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileNameEncode = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        String datetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''${fileNameEncode + '_' + datetime}.xlsx");
        return response.getOutputStream();
    }


}
