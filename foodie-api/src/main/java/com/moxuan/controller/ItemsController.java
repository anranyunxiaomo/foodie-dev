package com.moxuan.controller;

import com.moxuan.page.PageInfo;
import com.moxuan.service.ItemsCommentsService;
import com.moxuan.service.ItemsService;
import com.moxuan.utils.BaseResp;
import com.moxuan.vo.CommentLevelCountsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("items")
public class ItemsController {

    @Autowired
    private ItemsService itemsService;
    @Autowired
    private ItemsCommentsService itemsCommentsService;

    /**
     * 获取商品查询的信息
     */
    @GetMapping("/info/{itemId}")
    public BaseResp info(@PathVariable String itemId) {
        return itemsService.info(itemId);
    }

    /**
     * 获取商品评价等级
     */
    @GetMapping("/commentLevel")
    public BaseResp<CommentLevelCountsVO> commentLevel(@RequestParam(value = "itemId") String itemId) {
        return itemsCommentsService.commentLevel(itemId);
    }

    /**
     * 获取商品评论列表详情数据
     */
    @GetMapping("/comments")
    public BaseResp comments(@RequestParam String itemId,
                             @RequestParam Integer level,
                             PageInfo pageInfo) {
        return itemsCommentsService.comments(itemId, level, pageInfo);
    }

    /**
     * 搜索商品列表
     */
    @GetMapping("/search")
    public BaseResp search(@RequestParam String keywords,
                           @RequestParam String sort,
                           PageInfo pageInfo) {
        return itemsCommentsService.search(keywords, null, sort, pageInfo);
    }

    /**
     * 通过分类id搜索商品列表
     */
    @GetMapping("/catItems")
    public BaseResp catItems(@RequestParam Integer catId,
                             @RequestParam String sort,
                             PageInfo pageInfo) {
        return itemsCommentsService.search(null, catId, sort, pageInfo);
    }


    /**
     * 根据商品规格ids查找最新的商品数据  // 用于用户长时间未登录网站，刷新购物车中的数据（主要是商品价格），类似京东淘宝
     */
    @GetMapping("/refresh")
    public BaseResp refresh(String itemSpecIds) {
        return itemsCommentsService.queryItemsBySpecIds(itemSpecIds);
    }


}
