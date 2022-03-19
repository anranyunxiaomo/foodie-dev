package com.moxuan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxuan.bo.ItemsCommentsCountBO;
import com.moxuan.pojo.ItemsComments;
import com.moxuan.vo.ItemCommentVO;
import com.moxuan.vo.SearchItemsVO;
import com.moxuan.vo.ShopcartVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemsCommentsMapper extends BaseMapper<ItemsComments> {

    /**
     * 商品评论数汇总
     */
    List<ItemsCommentsCountBO> commentLevel(@Param("itemId") String itemId);

    Page<ItemCommentVO> getCommentsInfoPage(Page<ItemCommentVO> objectPage, @Param("itemId") String itemId, @Param("level") Integer level);

    Page<SearchItemsVO> searchItems(Page<SearchItemsVO> page, @Param("keywords") String keywords, @Param("catId") Integer catId, @Param("sort") String sort);

    List<ShopcartVO> queryItemsBySpecIds(@Param("itemSpecIdList") List<String> itemSpecIdList);
}