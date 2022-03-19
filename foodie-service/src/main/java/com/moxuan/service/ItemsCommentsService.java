package com.moxuan.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.bo.ItemsCommentsCountBO;
import com.moxuan.page.PageInfo;
import com.moxuan.pojo.ItemsComments;
import com.moxuan.pojo.mapper.ItemsCommentsBeanMapper;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.DesensitizationUtil;
import com.moxuan.utils.PagedGridResult;
import com.moxuan.utils.ResultUtil;
import com.moxuan.vo.CommentLevelCountsVO;
import com.moxuan.vo.ItemCommentVO;
import com.moxuan.vo.SearchItemsVO;
import com.moxuan.vo.ShopcartVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemsCommentsService extends ServiceImpl<com.moxuan.mapper.ItemsCommentsMapper, ItemsComments> {

    @Resource
    private ItemsCommentsBeanMapper itemsCommentsBeanMapper;

    /**
     * 获取商品评价等级
     */
    public BaseResp<CommentLevelCountsVO> commentLevel(String itemId) {
        if (StrUtil.isBlank(itemId)) {
            return ResultUtil.error(null);
        }
        List<ItemsCommentsCountBO> itemsCommentsCountBOList = this.baseMapper.commentLevel(itemId);
        // 获取评论总数
        int TotalCounts = itemsCommentsCountBOList.stream().mapToInt(ItemsCommentsCountBO::getCommentCount).sum();
        // 获取好评数
        Integer goodCounts = getLevelCount(itemsCommentsCountBOList, 1);
        // 中评数
        Integer normalCounts = getLevelCount(itemsCommentsCountBOList, 2);
        // 差评数
        Integer badCounts = getLevelCount(itemsCommentsCountBOList, 3);
        CommentLevelCountsVO commentLevelCountsVO = itemsCommentsBeanMapper.toCommentLevelCountsVO(TotalCounts, goodCounts, normalCounts, badCounts);
        return ResultUtil.ok(commentLevelCountsVO);
    }

    public Integer getLevelCount(List<ItemsCommentsCountBO> itemsCommentsCountBOList, Integer commentLevel) {
        return itemsCommentsCountBOList.stream()
                .filter(itemsCommentsCountBO -> itemsCommentsCountBO.getCommentLevel() == commentLevel)
                .mapToInt(ItemsCommentsCountBO::getCommentCount).sum();
    }

    /**
     * 获取商品评论列表详情数据
     */
    public BaseResp comments(String itemId, Integer level, PageInfo pageInfo) {
        if (StrUtil.isBlankIfStr(itemId)) {
            return ResultUtil.error(null);
        }
        Page<ItemCommentVO> page = new Page<>(pageInfo.getPage(), pageInfo.getPageSize());
        page = this.baseMapper.getCommentsInfoPage(page, itemId, level);
        List<ItemCommentVO> records = page.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            // 用户名称脱敏
            for (ItemCommentVO record : records) {
                String desensitized = DesensitizationUtil.commonDisplay(record.getNickname());
                record.setNickname(desensitized);
            }
        }
        PagedGridResult<ItemCommentVO> myCommentVOPagedGridResult = itemsCommentsBeanMapper.toItemCommentVO(page);
        return ResultUtil.ok(myCommentVOPagedGridResult);
    }

    /**
     * 搜索商品列表
     */
    public BaseResp search(String keywords, Integer catId, String sort, PageInfo pageInfo) {
        Page<SearchItemsVO> page = new Page<>(pageInfo.getPage(), pageInfo.getPageSize());
        page = this.baseMapper.searchItems(page, keywords, catId, sort);
        PagedGridResult<SearchItemsVO> searchItemsVOPagedGridResult = itemsCommentsBeanMapper.toSearchItemsVO(page);
        return ResultUtil.ok(searchItemsVOPagedGridResult);
    }

    /**
     * 根据商品规格ids查找最新的商品数据  // 用于用户长时间未登录网站，刷新购物车中的数据（主要是商品价格），类似京东淘宝
     */
    public BaseResp queryItemsBySpecIds(String itemSpecIds) {
        if (StrUtil.isBlankIfStr(itemSpecIds)) {
            return ResultUtil.ok();
        }
        List<String> itemSpecIdList = Arrays.stream(itemSpecIds.split(",")).collect(Collectors.toList());
        List<ShopcartVO> shopcartVOS = this.baseMapper.queryItemsBySpecIds(itemSpecIdList);
        return ResultUtil.ok(shopcartVOS);
    }
}

