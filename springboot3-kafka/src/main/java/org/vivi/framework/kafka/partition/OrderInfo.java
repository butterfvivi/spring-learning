package org.vivi.framework.kafka.partition;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class OrderInfo {

    private long orderId;

    private long userId;

    private String number;

    private String address;

    private Date orderTime;
}
