package org.vivi.framework.ireport.demo.report.dto;

import lombok.Data;

import java.util.List;

/**
 * 文件打包成zip下载
 */

@Data
public class DownloadFileZipDto {

    //压缩包名称。可不填
    private String zipName;
    //必须存在值
    private List<DownloadFileDto> data;

}
