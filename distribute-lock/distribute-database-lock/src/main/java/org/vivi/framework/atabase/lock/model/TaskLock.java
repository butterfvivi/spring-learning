package org.vivi.framework.atabase.lock.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskLock {

    private long lockKey;

    private String ownerNode;

    private LocalDateTime expireTime;

    private int version;

    private LocalDateTime updateTime;
}
