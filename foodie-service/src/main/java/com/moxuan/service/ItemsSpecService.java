package com.moxuan.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.mapper.ItemsSpecMapper;
import com.moxuan.pojo.ItemsSpec;
import com.moxuan.utils.CustomException;
import com.moxuan.utils.ResultUtil;
import org.springframework.stereotype.Service;

@Service
public class ItemsSpecService extends ServiceImpl<ItemsSpecMapper, ItemsSpec> {

    // 规格表中需要扣除库存
    public void decreaseStock(String itemSpecId, int buyCounts) {
        // 目前采用乐观锁的方法进行数据 不会出现超卖超买的现象
        // todo 后续会进行分布式锁进行库存的绑定 zookeeper redis 分布式锁
        Integer integer = this.baseMapper.decreaseItemSpecStock(itemSpecId, buyCounts);
        if (integer != 1) {
            throw new CustomException(ResultUtil.error("当前商品已经没有库存"));
        }

    }
}

