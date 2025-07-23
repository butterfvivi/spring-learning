package org.vivi.framework.atabase.lock.model;

import lombok.Data;

import java.util.Date;

@Data
public class LockDo {

    private String lockName;

    private String ownerId;

    private Date acquireTime;

    private Date expireTime;

    private Integer version;
}
