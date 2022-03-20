package com.moxuan.ao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MountAO {

    /**
     * 商品原价累计
     */
    private Integer totalAmount;
    /**
     * 优惠后的实际支付价格累计
     */
    private Integer realPayAmount;
}
