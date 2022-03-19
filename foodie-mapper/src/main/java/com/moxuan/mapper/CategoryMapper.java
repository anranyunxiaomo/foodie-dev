package com.moxuan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moxuan.pojo.Category;
import com.moxuan.vo.CategoryVO;
import com.moxuan.vo.NewItemsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    List<CategoryVO> getSubCatList(@Param("rootCatId") Integer rootCatId);

    List<NewItemsVO> sixNewItems(@Param("rootCatId") String rootCatId);
}