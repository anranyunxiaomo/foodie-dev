package com.moxuan.controller;

import cn.hutool.core.util.StrUtil;
import com.moxuan.bo.ShopcartBO;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.ResultUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("shopcart")
public class ShopcatController {

    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    public BaseResp add(@RequestParam String userId,
                        @RequestBody ShopcartBO shopcartBO,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        if (StrUtil.isBlankIfStr(userId)) {
            return ResultUtil.error(null);
        }
        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        return ResultUtil.ok();
    }

    /**
     * 删除购物车中商品
     */
    @PostMapping("/del")
    public BaseResp del(@RequestParam String userId,
                        @RequestBody String itemSpecId,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        if (StrUtil.isBlankIfStr(userId) || StrUtil.isBlankIfStr(itemSpecId)) {
            return ResultUtil.error("参数不能为空");
        }
        // TODO 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的商品
        return ResultUtil.ok();
    }

}
