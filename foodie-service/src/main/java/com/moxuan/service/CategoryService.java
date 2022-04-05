package com.moxuan.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.mapper.CategoryMapper;
import com.moxuan.pojo.Category;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.RedisOperator;
import com.moxuan.utils.ResultUtil;
import com.moxuan.vo.CategoryVO;
import com.moxuan.vo.NewItemsVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.moxuan.constant.BasicConstant.CATEGORY_PRIMARY;
import static com.moxuan.constant.ErrorInfoConstant.SUB_CAT_CATEGORY_DOES_NOT_EXIST;
import static com.moxuan.constant.RedisKeyConstant.CATS;
import static com.moxuan.constant.RedisKeyConstant.SUB_CAT;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    @Resource
    private RedisOperator redisOperator;

    /**
     * 获取商品分类
     */
    public BaseResp<List<Category>> cats() {
        String cats = redisOperator.get(CATS.getName());
        List<Category> list;
        if (StrUtil.isBlankIfStr(cats)) {
            list = this.lambdaQuery().eq(Category::getType, CATEGORY_PRIMARY.getType()).list();
            redisOperator.set(CATS.getName(), JSONUtil.toJsonStr(list));
        } else {
            list = JSONUtil.toList(cats, Category.class);
        }
        return ResultUtil.ok(list);
    }

    /**
     * 获取商品分类子分类
     */
    public BaseResp<List<CategoryVO>> subCat(Integer rootCatId) {
        if (ObjectUtil.isNull(rootCatId)) {
            return ResultUtil.error(SUB_CAT_CATEGORY_DOES_NOT_EXIST.getErrorInfo());
        }
        String subCat = redisOperator.get(SUB_CAT.getName() + rootCatId);
        List<CategoryVO> subCatList;
        if (StrUtil.isBlankIfStr(subCat)) {
            subCatList = this.baseMapper.getSubCatList(rootCatId);
            redisOperator.set(SUB_CAT.getName() + ":" + rootCatId, JSONUtil.toJsonStr(subCatList));
        } else {
            subCatList = JSONUtil.toList(subCat, CategoryVO.class);
        }
        return ResultUtil.ok(subCatList);
    }

    /**
     * 查询每个一级分类下的最新6条商品
     */
    public BaseResp<List<NewItemsVO>> sixNewItems(String rootCatId) {
        if (ObjectUtil.isNull(rootCatId)) {
            return ResultUtil.error(SUB_CAT_CATEGORY_DOES_NOT_EXIST.getErrorInfo());
        }
        List<NewItemsVO> subCatList = this.baseMapper.sixNewItems(rootCatId);
        return ResultUtil.ok(subCatList);
    }
}