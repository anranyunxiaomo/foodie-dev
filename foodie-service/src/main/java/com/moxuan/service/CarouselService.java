package com.moxuan.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.mapper.CarouselMapper;
import com.moxuan.pojo.Carousel;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.RedisOperator;
import com.moxuan.utils.ResultUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.moxuan.constant.RedisKeyConstant.CAROUSEL;

@Service
public class CarouselService extends ServiceImpl<CarouselMapper, Carousel> {

    @Resource
    private RedisOperator redisOperator;

    /**
     * 获取轮播图列表
     */
    public BaseResp<List<Carousel>> queryAll(Integer isShow) {
        String carousel = redisOperator.get(CAROUSEL.getName());
        List<Carousel> list;
        if (StrUtil.isBlankIfStr(carousel)) {
            list = this.lambdaQuery().eq(Carousel::getIsShow, isShow)
                    .orderByDesc(Carousel::getSort)
                    .list();
            redisOperator.set(CAROUSEL.getName(), JSONUtil.toJsonStr(list));
        } else {
            list = JSONUtil.toList(carousel, Carousel.class);
        }
        return ResultUtil.ok(list);
    }

}

