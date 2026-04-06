package org.vivi.framework.drools.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.drools.model.dto.OrderDiscount;
import org.vivi.framework.drools.service.OrderDiscountService;
import org.vivi.framework.drools.web.request.OrderRequest;

@RestController
public class DroolsController {

    @Autowired
    private OrderDiscountService orderDiscountService;

    /**
     * 网上购物，需要根据不同的规则计算商品折扣，比如VIP客户增加5%的折扣，
     * 购买金额超过1000元的增加10%的折扣等，而且这些规则可能随时发生变化，甚至增加新的规则
     * @param orderRequest
     * @return
     */
    @PostMapping("/get-discount")
    public ResponseEntity<OrderDiscount> getDiscount(@RequestBody OrderRequest orderRequest) {
        OrderDiscount discount = orderDiscountService.getDiscount(orderRequest);
        return new ResponseEntity<>(discount, HttpStatus.OK);
    }

}
