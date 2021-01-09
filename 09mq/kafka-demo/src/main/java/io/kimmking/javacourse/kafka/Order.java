package io.kimmking.javacourse.kafka;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order { // 此类型为需要使用的消息内容

    private Long id;
    private Long ts;
    private String symbol;
    private Double price;

}
