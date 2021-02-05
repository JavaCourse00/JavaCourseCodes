package io.byk.queue.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author boyunkai <boyunkai@kuaishou.com>
 * Created on 2021-02-05
 */
@Data
@AllArgsConstructor
public class Order {
    // 订单编号
    private String orderId;
    // 状态 0-未处理 1-已处理
    private Integer statusId;
}
