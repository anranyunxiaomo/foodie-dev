package com.moxuan.task;

import com.moxuan.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderTask {


    @Autowired
    private OrderStatusService orderStatusService;

    //   @Scheduled(cron = "")
    public void closeOrder() {
        orderStatusService.closeOrder();
    }


}
