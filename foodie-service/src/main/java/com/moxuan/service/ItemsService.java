package com.moxuan.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.mapper.ItemsMapper;
import com.moxuan.pojo.Items;
import com.moxuan.pojo.ItemsImg;
import com.moxuan.pojo.ItemsParam;
import com.moxuan.pojo.ItemsSpec;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.ResultUtil;
import com.moxuan.vo.ItemInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemsService extends ServiceImpl<ItemsMapper, Items> {

    @Autowired
    private ItemsImgService itemsImgService;
    @Autowired
    private ItemsSpecService itemsSpecService;
    @Autowired
    private ItemsParamService itemsParamService;

    /**
     * 获取商品查询的信息
     */
    public BaseResp info(String itemId) {
        if (ObjectUtil.isNull(itemId)) {
            return ResultUtil.error(null);
        }
        // 商品基本信息
        Items items = this.lambdaQuery()
                .eq(Items::getId, itemId).
                one();
        // 商品图片列表
        List<ItemsImg> itemsImgList = this.itemsImgService.lambdaQuery()
                .eq(ItemsImg::getItemId, itemId)
                .list();
        //  商品规格信息
        List<ItemsSpec> itemsSpecList = this.itemsSpecService.lambdaQuery()
                .eq(ItemsSpec::getItemId, itemId)
                .list();
        // 商品参数信息
        ItemsParam itemsParam = this.itemsParamService.lambdaQuery()
                .eq(ItemsParam::getItemId, itemId)
                .one();
        ItemInfoVO itemInfoVO = new ItemInfoVO(items, itemsImgList, itemsSpecList, itemsParam);
        return ResultUtil.ok(itemInfoVO);
    }

}
