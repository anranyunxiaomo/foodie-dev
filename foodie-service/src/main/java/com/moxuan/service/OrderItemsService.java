package com.moxuan.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.ao.MountAO;
import com.moxuan.mapper.OrderItemsMapper;
import com.moxuan.pojo.Items;
import com.moxuan.pojo.ItemsImg;
import com.moxuan.pojo.ItemsSpec;
import com.moxuan.pojo.OrderItems;
import com.moxuan.pojo.mapper.OrderItemsBeanMapper;
import com.moxuan.utils.CustomException;
import com.moxuan.utils.ResultUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderItemsService extends ServiceImpl<OrderItemsMapper, OrderItems> {

    @Autowired
    private ItemsSpecService itemsSpecService;
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private ItemsImgService itemsImgService;
    @Autowired
    private Sid sid;
    @Resource
    private OrderItemsBeanMapper orderItemsBeanMapper;


    /**
     * 根据订单 创建对应的订单商品列表
     */
    public MountAO add(String orderId, String itemSpecIds) {
        if (StrUtil.isBlankIfStr(itemSpecIds)) {
            throw new CustomException(ResultUtil.error("商品提交列表无效"));
        }
        Integer totalAmount = 0;
        Integer realPayAmount = 0;
        for (String itemSpecId : itemSpecIds.split(",")) {
            // todo 整合redis后，商品购买的数量重新从redis的购物车中获取
            String orderItemsId = sid.nextShort();
            int buyCounts = 1;
            // 通过规格ID 获取对应的商品原价和优惠价格
            ItemsSpec itemsSpec = this.itemsSpecService.lambdaQuery().eq(ItemsSpec::getId, itemSpecId).one();
            // 总金额 累计
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            // 优惠价格 累计
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;
            // 根据商品id，获得商品信息以及商品图片
            Items items = itemsService.lambdaQuery().eq(Items::getId, itemsSpec.getItemId()).one();
            ItemsImg itemsImg = itemsImgService.lambdaQuery().eq(ItemsImg::getItemId, itemsSpec.getItemId()).eq(ItemsImg::getIsMain, 1).one();
            // 整合对象 订单关联表 orderItems
            OrderItems orderItems = orderItemsBeanMapper.combination(orderItemsId, orderId, itemsSpec, items, itemsImg, realPayAmount, buyCounts, itemSpecId);
            this.save(orderItems);
            // 在用户提交订单以后，规格表中需要扣除库存
            itemsSpecService.decreaseStock(itemSpecId, buyCounts);
        }
        return new MountAO(totalAmount, realPayAmount);

    }
}

