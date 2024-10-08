package org.vivi.framework.abstracts.strategy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyUser {

    private Long id;

    private String userName;

    private String nickName;

    private String userEmail;

    private String userSex;
}
