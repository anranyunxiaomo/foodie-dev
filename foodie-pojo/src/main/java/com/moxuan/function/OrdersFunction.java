package com.moxuan.function;


import com.moxuan.pojo.UserAddress;

import java.util.function.Function;


public class OrdersFunction {

    /**
     * 拼接订单的完整地址
     */
    public static String spliceAddress(UserAddress userAddress) {
        String apply = ((Function<UserAddress, String>) T -> {
            // 省份
            return new StringBuffer(T.getProvince())
                    .append(" ")
                    // 市
                    .append(T.getCity())
                    .append(" ")
                    // 区县
                    .append(T.getDistrict())
                    .append(" ")
                    // 详细地址
                    .append(T.getDetail())
                    .toString();
        }).apply(userAddress);
        return apply;
    }
}