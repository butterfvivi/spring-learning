package org.vivi.framework.ureport.store.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UreportFileVo {
    private String prefix;
    private String name;
    private String fileName;
    private byte[] content;
    private Date createTime;
    private Date updateTime;
    private Date updateDate;

}
