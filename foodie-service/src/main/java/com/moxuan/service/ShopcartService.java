package com.moxuan.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.moxuan.bo.ShopcartBO;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.CookieUtils;
import com.moxuan.utils.RedisOperator;
import com.moxuan.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.moxuan.constant.RedisKeyConstant.FOODIE_SHOPCART;

@Service
public class ShopcartService {

    @Autowired
    private RedisOperator redisOperator;


    /**
     * 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
     *
     * @return
     */
    public BaseResp<List<ShopcartBO>> add(String userId, ShopcartBO shopcartBO, HttpServletRequest request, HttpServletResponse response) {
        List<ShopcartBO> shopcartBOS = new ArrayList<>();
        String shopcart = redisOperator.get(FOODIE_SHOPCART.getName() + ":" + userId);
        // 如何redis 不为空
        if (!StrUtil.isBlankIfStr(shopcart)) {
            shopcartBOS = JSONUtil.toList(shopcart, ShopcartBO.class);
            // 判断是否 存在相同的商品 specId 规格ID
            ShopcartBO existShopcartBO = shopcartBOS.stream().filter(a -> a.getSpecId().equals(shopcartBO.getSpecId())).findFirst().orElse(shopcartBOS.get(shopcartBOS.size() - 1));
            if (ObjectUtil.isNotEmpty(existShopcartBO)) {
                // 如果有 排除 相同的商品 specId 规格ID
                shopcartBOS = shopcartBOS.stream().filter(a -> !a.getSpecId().equals(shopcartBO.getSpecId())).collect(Collectors.toList());
                existShopcartBO.setBuyCounts(existShopcartBO.getBuyCounts() + shopcartBO.getBuyCounts());
                shopcartBOS.add(existShopcartBO);
            } else {
                shopcartBOS.add(shopcartBO);
            }
        } else {
            // redis 为空
            shopcartBOS.add(shopcartBO);
        }
        redisOperator.set(FOODIE_SHOPCART.getName() + ":" + userId, JSONUtil.toJsonStr(shopcartBOS));
        return ResultUtil.ok();
    }

    /**
     * 删除购物车中商品
     */
    public BaseResp del(String userId, String itemSpecId, HttpServletRequest request, HttpServletResponse response) {
        // TODO 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的商品
        String shopcart = redisOperator.get(FOODIE_SHOPCART.getName() + ":" + userId);
        if (StrUtil.isNotBlank(shopcart)) {
            List<ShopcartBO> shopcartBOS = JSONUtil.toList(shopcart, ShopcartBO.class);
            // 将当前商品规格ID 的商品排除
            shopcartBOS = shopcartBOS.stream().filter(a -> !a.getSpecId().equals(itemSpecId)).collect(Collectors.toList());
            redisOperator.set(FOODIE_SHOPCART.getName() + ":" + userId, JSONUtil.toJsonStr(shopcartBOS));
        }
        return ResultUtil.ok();
    }

    /**
     * 同步购物车数据
     */
    public void synchShopcartData(String userId, HttpServletRequest request,
                                  HttpServletResponse response) {
        List<ShopcartBO> shopcartBOS = new ArrayList<>();
        // 1  redis 中有  cookie 有  cookie 与redis 中有的要cookie 其他的要redis 合并即可
        // 2  redis 中没有 cookie 没有 直接 给空数组
        // 3  redis 中没有 cookie 有  要 cookie
        // 4  redis 中有  cookie 没有  要redis
        String shopcart = redisOperator.get(FOODIE_SHOPCART.getName() + ":" + userId);
        String cookieValue = CookieUtils.getCookieValue(request, FOODIE_SHOPCART.getName(), true);
        // redis 中没有
        if (StrUtil.isBlankIfStr(shopcart)) {
            // cookie 有  要 cookie
            if (StrUtil.isNotBlank(cookieValue)) {
                shopcartBOS = JSONUtil.toList(cookieValue, ShopcartBO.class);
            }
        } else {
            // redis 中有  cookie 没有  要redis
            if (StrUtil.isBlankIfStr(cookieValue)) {
                shopcartBOS = JSONUtil.toList(shopcart, ShopcartBO.class);
                // redis 中有  cookie 有  cookie 与redis 中有的要cookie 其他的要redis 合并即可
            } else {
                List<ShopcartBO> redisList = JSONUtil.toList(shopcart, ShopcartBO.class);
                List<ShopcartBO> cookieList = JSONUtil.toList(cookieValue, ShopcartBO.class);
                Iterator<ShopcartBO> iterator = redisList.iterator();
                while (iterator.hasNext()) {
                    ShopcartBO next = iterator.next();
                    List<ShopcartBO> shopcartBOList = cookieList.stream().filter(a -> a.getSpecId().equals(next.getSpecId())).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(shopcartBOList)) {
                        iterator.remove();
                        // 覆盖购买数量，不累加，参考京东 cookie
                        shopcartBOS.addAll(shopcartBOList);
                        cookieList.removeAll(shopcartBOList);
                    }
                }
                shopcartBOS.addAll(cookieList);
                shopcartBOS.addAll(redisList);
            }
        }
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART.getName(), JSONUtil.toJsonStr(shopcartBOS), true);
        redisOperator.set(FOODIE_SHOPCART.getName() + ":" + userId, JSONUtil.toJsonStr(shopcartBOS));

    }

}
