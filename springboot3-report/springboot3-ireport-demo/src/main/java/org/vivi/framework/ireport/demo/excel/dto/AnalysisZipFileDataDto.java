package org.vivi.framework.ireport.demo.excel.dto;

import lombok.Data;

/**
 * 解析zip文件的json
 */
@Data
public class AnalysisZipFileDataDto {

    //*文件地址。
    // URL(包含http://  或者https://)
    // 绝对路径("D:\\home...")
    // 相对路径(指定是项目resource目录下)。
    private String path;
    //*0-url , 1-相对路径 , 2-绝对路径
    private String pathType;
    //*解析文件的类型逗号拼接：json,txt,xml
    private String analysisType;
    //如果zip无提取密码，就为空
    private String password;

}
