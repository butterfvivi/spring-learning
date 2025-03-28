package org.vivi.framework.ireport.demo.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadFileDto {

    //文件地址。
    // URL(包含http://  或者https://)
    // 绝对路径("D:\\home...")
    // 相对路径(指定是项目resource目录下)。
    private String path;

    //下载的文件后缀
    private String suffix;

    //文件名称，不填就当前时间戳
    private String name;

    //0-url , 1-相对路径 , 2-绝对路径
    private String pathType;
}
