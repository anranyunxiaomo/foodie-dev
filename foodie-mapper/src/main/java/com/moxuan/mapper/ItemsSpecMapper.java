package com.moxuan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moxuan.pojo.ItemsSpec;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ItemsSpecMapper extends BaseMapper<ItemsSpec> {
    int decreaseItemSpecStock(@Param("itemSpecId") String itemSpecId, @Param("buyCounts") int buyCounts);
}