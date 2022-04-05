package com.moxuan.controller;

import cn.hutool.core.util.StrUtil;
import com.moxuan.bo.ShopcartBO;
import com.moxuan.service.ShopcartService;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("shopcart")
public class ShopcartController {

    @Autowired
    private ShopcartService shopcartService;


    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    public BaseResp<List<ShopcartBO>> add(@RequestParam String userId,
                                          @RequestBody ShopcartBO shopcartBO,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        if (StrUtil.isBlankIfStr(userId)) {
            return ResultUtil.error(null);
        }
        return shopcartService.add(userId, shopcartBO, request, response);

    }

    /**
     * 删除购物车中商品
     */
    @PostMapping("/del")
    public BaseResp del(@RequestParam String userId,
                        @RequestParam String itemSpecId,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        if (StrUtil.isBlankIfStr(userId) || StrUtil.isBlankIfStr(itemSpecId)) {
            return ResultUtil.error("参数不能为空");
        }
        return shopcartService.del(userId, itemSpecId, request, response);
    }

}
