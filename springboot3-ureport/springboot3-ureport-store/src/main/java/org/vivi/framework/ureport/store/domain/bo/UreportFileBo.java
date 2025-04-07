package org.vivi.framework.ureport.store.domain.bo;

import lombok.Data;

import java.util.Date;

@Data
public class UreportFileBo {

    private String prefix;

    private String name;
    private String fileName;
    private byte[] content;
    private Date createTime;
    private Date updateTime;


    private Integer page;
    private Integer limit;
}
