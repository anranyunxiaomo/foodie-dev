package com.moxuan.pojo.mapper;

import com.moxuan.pojo.Items;
import com.moxuan.pojo.ItemsImg;
import com.moxuan.pojo.ItemsSpec;
import com.moxuan.pojo.OrderItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrderItemsBeanMapper {

    @Mappings({
            @Mapping(target = "id", source = "orderItemsId"),
            @Mapping(target = "orderId", source = "orderId"),
            @Mapping(target = "itemId", source = "itemsSpec.itemId"),
            @Mapping(target = "itemName", source = "items.itemName"),
            @Mapping(target = "itemImg", source = "itemsImg.url"),
            @Mapping(target = "buyCounts", source = "buyCounts"),
            @Mapping(target = "itemSpecId", source = "itemSpecId"),
            @Mapping(target = "itemSpecName", source = "itemsSpec.name"),
            @Mapping(target = "price", source = "itemsSpec.priceDiscount"),
    })
    OrderItems combination(String orderItemsId,
                           String orderId,
                           ItemsSpec itemsSpec,
                           Items items,
                           ItemsImg itemsImg,
                           Integer realPayAmount,
                           int buyCounts,
                           String itemSpecId);

}
