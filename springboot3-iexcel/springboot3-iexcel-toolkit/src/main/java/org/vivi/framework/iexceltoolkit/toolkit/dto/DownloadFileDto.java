package org.vivi.framework.iexceltoolkit.toolkit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

    public DownloadFileDto(String name, String suffix) {
        this.suffix = suffix;
        this.name = name;
    }

    public DownloadFileDto(String path, String pathType,String suffix, String name) {
        this.path = path;
        this.suffix = suffix;
        this.name = name;
        this.pathType = pathType;
    }
}
