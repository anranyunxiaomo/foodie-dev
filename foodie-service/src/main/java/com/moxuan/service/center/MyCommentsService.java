package com.moxuan.service.center;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.bo.center.OrderItemsCommentBO;
import com.moxuan.mapper.ItemsCommentsMapper;
import com.moxuan.mapper.OrderItemsMapper;
import com.moxuan.page.PageInfo;
import com.moxuan.pojo.ItemsComments;
import com.moxuan.pojo.OrderItems;
import com.moxuan.pojo.Orders;
import com.moxuan.pojo.mapper.center.CenterCommentsBeanMapper;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.PagedGridResult;
import com.moxuan.utils.ResultUtil;
import com.moxuan.vo.center.MyCommentVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MyCommentsService extends ServiceImpl<ItemsCommentsMapper, ItemsComments> {

    @Autowired
    private MyOrdersService myOrdersService;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private Sid sid;
    @Resource
    private CenterCommentsBeanMapper centerCommentsBeanMapper;
    @Autowired
    private CenterOrderStatusService centerOrderStatusService;

    /**
     * 查询订单完成后，评价列表展示
     */
    public BaseResp pending(String userId, String orderId) {
        // 判断用户和订单是否关联
        if (myOrdersService.checkUserOrder(orderId, userId)) {
            return ResultUtil.error("该订单属于无效订单");
        }
        // 判断该笔订单是否已经评价过，评价过了就不再继续
        Orders orders = myOrdersService.lambdaQuery().eq(Orders::getId, orderId).one();
        if (orders.getIsComment() == 1) {
            return ResultUtil.error("该笔订单已经评价");
        }
        List<OrderItems> list = this.orderItemsMapper.selectList(Wrappers.<OrderItems>lambdaQuery()
                .eq(OrderItems::getOrderId, orderId));
        return ResultUtil.ok(list);
    }

    /**
     * 保存评论列表
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public BaseResp saveList(String userId, String orderId, List<OrderItemsCommentBO> commentList) {
        // 判断用户和订单是否关联
        if (myOrdersService.checkUserOrder(orderId, userId)) {
            return ResultUtil.error("该订单属于无效订单");
        }
        // 判断评论内容list不能为空
        if (CollUtil.isEmpty(commentList)) {
            return ResultUtil.error("评论内容不能为空！");
        }
        // 1. 保存评价 items_comments
        saveComments(userId, commentList);
        // 2. 修改订单表改已评价 orders
        this.myOrdersService.updateOrdersComments(orderId);
        // 3. 修改订单状态表的留言时间 order_status
        centerOrderStatusService.updateOrdersStatusCommentTime(orderId);
        return ResultUtil.ok();
    }

    /**
     * 查询我的评价
     */
    public BaseResp queryMyComments(String userId, PageInfo pageInfo) {
        Page<MyCommentVO> page = new Page<>(pageInfo.getPage(), pageInfo.getPageSize());
        page = this.baseMapper.queryMyComments(page, userId);
        PagedGridResult<MyCommentVO> myCommentVOPagedGridResult = centerCommentsBeanMapper.toMyCommentVO(page);
        return ResultUtil.ok(myCommentVOPagedGridResult);
    }


    /**
     * 保存评价
     */
    private void saveComments(String userId, List<OrderItemsCommentBO> commentList) {
        ArrayList<ItemsComments> itemsComments = new ArrayList<>();
        for (OrderItemsCommentBO orderItemsCommentBO : commentList) {
            ItemsComments combination = centerCommentsBeanMapper.combination(userId, sid.nextShort(), orderItemsCommentBO);
            itemsComments.add(combination);
        }
        this.saveBatch(itemsComments);
    }

}
