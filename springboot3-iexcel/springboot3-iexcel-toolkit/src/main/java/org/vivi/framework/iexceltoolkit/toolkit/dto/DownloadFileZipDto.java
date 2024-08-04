package org.vivi.framework.iexceltoolkit.toolkit.dto;

import lombok.Data;

import java.util.List;

@Data
public class DownloadFileZipDto {

    //压缩包名称。可不填
    private String zipName;
    //必须存在值
    private List<DownloadFileDto> data;
}
