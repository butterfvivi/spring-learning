package org.vivi.framework.lucky.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class EchartsUtil {
    /**
     * 临时文件夹路径
     */
    public static final String TEMP_FILE_PATH = "F:/data/echart/";
    private static final String SUCCESS_CODE = "1";

    private static final String path = EchartsUtil.class.getClassLoader().getResource("json").getPath();


    public static void main(String[] args) throws IOException, TemplateException {

        // 获取option字符串
        String option = getStringByFile(path + "/test.json");
        JSONObject object = JSONObject.parseObject(option);
        option = object.toJSONString();

        // 根据option参数,width和height生成base64编码
        String base64 = EchartsUtil.generateEchartsBase64(option, "1360", "800");

        System.out.println("BASE64:" + base64);
        generateImage(base64, TEMP_FILE_PATH, "test.png");

    }


    public static String generateEchartsBase64(String option, String width, String height) throws IOException {
        String base64 = "";
        if (option == null) {
            return base64;
        }
        option = option.replaceAll("\\s+", "");//.replaceAll("\"", "");
        JSONObject object = JSON.parseObject(option);
        object.getJSONArray("series").getJSONObject(0).put("data", Arrays.asList(120, 200, 150, 80, 70, 110, 130));
        String json = JSONObject.toJSONString(object);
        object = JSON.parseObject(json);
        // 将option字符串作为参数发送给echartsConvert服务器
        if (width == null || "".equals(width)) {
            width = "800";
        }
        if (height == null || "".equals(height)) {
            height = "400";
        }
        Map<String, String> params = new HashMap<>();
        params.put("opt", JSON.toJSONString(object));
        params.put("width", width);
        params.put("height", height);
        String response = HttpUtils.post("http://localhost:6666", params, "utf-8");
        System.err.println(response);
        // 解析echartsConvert响应
        JSONObject responseJson = JSON.parseObject(response);
        String code = responseJson.getString("code");

        // 如果echartsConvert正常返回
        if (SUCCESS_CODE.equals(code)) {
            base64 = responseJson.getString("data");
        }
        // 未正常返回
        else {
            String string = responseJson.getString("msg");
            throw new RuntimeException(string);
        }
        return base64;
    }

    /**
     * 将base64转化为图片
     * @param base64
     * @param path
     * @param fileName
     * @throws IOException
     */
    public static void generateImage(String base64, String path, String fileName) throws IOException {
        Base64.Decoder decoder = Base64.getDecoder();
        try (OutputStream out = new FileOutputStream(path + fileName)){
            // 解密
            byte[] b = decoder.decode(base64);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            out.flush();
        }
    }

    /**
     * 将文件转化为字符串
     * @param fileName
     * @return
     */
    public static String getStringByFile(String fileName) {
        File file = new File("D:/Java/Echart/test.json");
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String line = null;
            while (((line = br.readLine()) != null)) {
                sb.append(line);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return sb.toString();
        }
    }



}