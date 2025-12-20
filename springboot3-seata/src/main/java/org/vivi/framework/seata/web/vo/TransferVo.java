package org.vivi.framework.seata.web.vo;

import lombok.Data;

@Data
public class TransferVo {

    private String from;

    private String to;

    double account;
}
