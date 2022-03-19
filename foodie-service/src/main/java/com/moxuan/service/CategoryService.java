package com.moxuan.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.mapper.CategoryMapper;
import com.moxuan.pojo.Category;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.ResultUtil;
import com.moxuan.vo.CategoryVO;
import com.moxuan.vo.NewItemsVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    /**
     * 获取商品分类
     */
    public BaseResp<List<Category>> cats() {
        List<Category> list = this.lambdaQuery().eq(Category::getType, 1).list();
        return ResultUtil.ok(list);
    }

    /**
     * 获取商品分类子分类
     */
    public BaseResp<List<CategoryVO>> subCat(Integer rootCatId) {
        if (ObjectUtil.isNull(rootCatId)) {
            return ResultUtil.error("分类不存在");
        }
        List<CategoryVO> subCatList = this.baseMapper.getSubCatList(rootCatId);
        return ResultUtil.ok(subCatList);
    }

    /**
     * 查询每个一级分类下的最新6条商品
     */
    public BaseResp<List<NewItemsVO>> sixNewItems(String rootCatId) {
        if (ObjectUtil.isNull(rootCatId)) {
            return ResultUtil.error("分类不存在");
        }
        List<NewItemsVO> subCatList = this.baseMapper.sixNewItems(rootCatId);
        return ResultUtil.ok(subCatList);
    }
}