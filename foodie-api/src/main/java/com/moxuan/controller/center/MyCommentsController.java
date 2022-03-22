package com.moxuan.controller.center;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mycomments")
public class MyCommentsController {
//
//    @PostMapping("/pending")
//    public BaseResp pending(
//
//            @RequestParam String userId,
//            @ApiParam(name = "orderId", value = "订单id", required = true)
//            @RequestParam String orderId) {
//
//        // 判断用户和订单是否关联
//        IMOOCJSONResult checkResult = checkUserOrder(userId, orderId);
//        if (checkResult.getStatus() != HttpStatus.OK.value()) {
//            return checkResult;
//        }
//        // 判断该笔订单是否已经评价过，评价过了就不再继续
//        Orders myOrder = (Orders)checkResult.getData();
//        if (myOrder.getIsComment() == YesOrNo.YES.type) {
//            return IMOOCJSONResult.errorMsg("该笔订单已经评价");
//        }
//
//        List<OrderItems> list = myCommentsService.queryPendingComment(orderId);
//
//        return IMOOCJSONResult.ok(list);
//    }


}
