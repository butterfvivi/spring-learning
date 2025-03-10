package org.vivi.framework.dynamic.sqlbatis3.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class UserBaseDO {


    private Long id;

    private String code;

    private String userName;
    private String  mobilePhone;

    private Integer isDelete;

    private LocalDateTime createTime;

    private Date updateTime;

    private byte[] headImageData;
    public UserBaseDO(){

    }




}
