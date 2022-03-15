package com.moxuan.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.mapper.ItemsMapper;
import com.moxuan.pojo.Items;

@Service
public class ItemsService extends ServiceImpl<ItemsMapper, Items> {

}

