package com.moxuan.utils;

import lombok.Data;

import java.util.List;

@Data
public class PagedGridResult<T> {

    private int page;            // 当前页数
    private int total;            // 总页数
    private long records;        // 总记录数
    private List<T> rows;        // 每行显示的内容

}
