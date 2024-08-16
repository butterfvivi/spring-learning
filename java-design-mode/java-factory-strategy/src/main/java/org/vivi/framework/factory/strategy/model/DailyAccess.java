package org.vivi.framework.factory.strategy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyAccess {

    private Long id;

    private Long userId;

    private Long accessId;

    private String nickName;

    private String userEmail;

    private String userSex;
}
