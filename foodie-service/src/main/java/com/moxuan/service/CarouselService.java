package com.moxuan.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.mapper.CarouselMapper;
import com.moxuan.pojo.Carousel;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.ResultUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarouselService extends ServiceImpl<CarouselMapper, Carousel> {


    public BaseResp<List<Carousel>> queryAll(Integer isShow) {
        List<Carousel> list = this.lambdaQuery().eq(Carousel::getIsShow, isShow)
                .orderByDesc(Carousel::getSort)
                .list();
        return ResultUtil.ok(list);
    }

}

