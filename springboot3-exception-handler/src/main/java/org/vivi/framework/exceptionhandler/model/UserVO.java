package org.vivi.framework.exceptionhandler.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class UserVO {

    private Long userId;

    private String username;

    private String email;

    private String sex;

    private LocalDateTime createTime;
}
