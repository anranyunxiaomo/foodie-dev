package com.moxuan.controller;

import com.moxuan.enums.YesOrNo;
import com.moxuan.pojo.Carousel;
import com.moxuan.pojo.Category;
import com.moxuan.service.CarouselService;
import com.moxuan.service.CategoryService;
import com.moxuan.utils.BaseResp;
import com.moxuan.vo.CategoryVO;
import com.moxuan.vo.NewItemsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;


    /**
     * 获取轮播图列表
     */
    @GetMapping("/carousel")
    public BaseResp<List<Carousel>> queryAll() {
        return carouselService.queryAll(YesOrNo.YES.type);
    }

    /**
     * 获取商品分类
     */
    @GetMapping("/cats")
    public BaseResp<List<Category>> cats() {
        return categoryService.cats();
    }

    /**
     * 获取商品分类子分类
     */
    @GetMapping("/subCat/{rootCatId}")
    public BaseResp<List<CategoryVO>> subCat(@PathVariable Integer rootCatId) {
        return categoryService.subCat(rootCatId);
    }

    /**
     * 查询每个一级分类下的最新6条商品
     */
    @GetMapping("/sixNewItems/{rootCatId}")
    public BaseResp<List<NewItemsVO>> sixNewItems(@PathVariable String rootCatId) {
        return categoryService.sixNewItems(rootCatId);
    }


}
