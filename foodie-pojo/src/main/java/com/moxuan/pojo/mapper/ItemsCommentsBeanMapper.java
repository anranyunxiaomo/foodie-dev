package com.moxuan.pojo.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxuan.utils.PagedGridResult;
import com.moxuan.vo.CommentLevelCountsVO;
import com.moxuan.vo.ItemCommentVO;
import com.moxuan.vo.SearchItemsVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ItemsCommentsBeanMapper {


    @Mappings({
            @Mapping(target = "totalCounts", source = "totalCounts"),
            @Mapping(target = "goodCounts", source = "goodCounts"),
            @Mapping(target = "normalCounts", source = "normalCounts"),
            @Mapping(target = "badCounts", source = "badCounts")
    })
    CommentLevelCountsVO toCommentLevelCountsVO(Integer totalCounts,
                                                Integer goodCounts,
                                                Integer normalCounts,
                                                Integer badCounts);

    @Mappings({
            @Mapping(target = "page", source = "page.current"),
            @Mapping(target = "rows", source = "page.records"),
            @Mapping(target = "total", source = "page.pages"),
            @Mapping(target = "records", source = "page.total")
    })
    PagedGridResult<ItemCommentVO> toItemCommentVO(Page<ItemCommentVO> page);

    @Mappings({
            @Mapping(target = "page", source = "page.current"),
            @Mapping(target = "rows", source = "page.records"),
            @Mapping(target = "total", source = "page.pages"),
            @Mapping(target = "records", source = "page.total")
    })
    PagedGridResult<SearchItemsVO> toSearchItemsVO(Page<SearchItemsVO> page);
}
