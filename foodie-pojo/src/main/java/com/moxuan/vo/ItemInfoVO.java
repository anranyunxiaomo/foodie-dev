package com.moxuan.vo;

import com.moxuan.pojo.Items;
import com.moxuan.pojo.ItemsImg;
import com.moxuan.pojo.ItemsParam;
import com.moxuan.pojo.ItemsSpec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品详情VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfoVO {

    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;

}
