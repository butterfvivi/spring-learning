package org.vivi.framework.ureport.store.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_ureport_file")
public class UreportFile {
    private String name;
    private String fileName;
    private byte[] content;
    private Date createTime;
    private Date updateTime;

}
