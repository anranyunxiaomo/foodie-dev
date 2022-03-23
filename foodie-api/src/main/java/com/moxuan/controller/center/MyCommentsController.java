package com.moxuan.controller.center;

import com.moxuan.bo.center.OrderItemsCommentBO;
import com.moxuan.page.PageInfo;
import com.moxuan.service.center.MyCommentsService;
import com.moxuan.utils.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("mycomments")
public class MyCommentsController {

    @Autowired
    private MyCommentsService myCommentsService;

    /**
     * 查询订单完成后，评价列表展示
     */
    @PostMapping("/pending")
    public BaseResp pending(@RequestParam String userId,
                            @RequestParam String orderId) {
        return myCommentsService.pending(userId, orderId);
    }

    /**
     * 保存评论列表
     */
    @PostMapping("/saveList")
    public BaseResp saveList(@RequestParam String userId,
                             @RequestParam String orderId,
                             @RequestBody List<OrderItemsCommentBO> commentList) {
        return myCommentsService.saveList(userId, orderId, commentList);
    }

    /**
     * 查询我的评价
     */
    @PostMapping("/query")
    public BaseResp queryMyComments(@RequestParam String userId,
                                    PageInfo pageInfo) {
        return myCommentsService.queryMyComments(userId, pageInfo);
    }


}
