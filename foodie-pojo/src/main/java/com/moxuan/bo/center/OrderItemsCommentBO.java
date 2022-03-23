package com.moxuan.bo.center;

import lombok.Data;

/**
 * 评论数据存储的json
 */
@Data
public class OrderItemsCommentBO {

    private String commentId;
    private String itemId;
    private String itemName;
    private String itemSpecId;
    private String itemSpecName;
    private Integer commentLevel;
    private String content;

}
