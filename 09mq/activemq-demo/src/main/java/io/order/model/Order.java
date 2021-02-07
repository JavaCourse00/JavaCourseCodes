package io.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 订单表
 *
 * @author boyunkai <boyunkai@kuaishou.com>
 * Created on 2021-02-07
 */
@Data
@AllArgsConstructor
public class Order {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 状态 0-未处理 1-已处理
     */
    private Integer statusId;
}