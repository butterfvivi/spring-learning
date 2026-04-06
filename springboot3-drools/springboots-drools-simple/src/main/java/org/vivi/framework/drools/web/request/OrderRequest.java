package org.vivi.framework.drools.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vivi.framework.drools.enums.CustomerType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    /**
     * 客户号
     */
    private String customerNumber;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 订单金额
     */
    private Integer amount;
    /**
     * 客户类型
     */
    private CustomerType customerType;


}