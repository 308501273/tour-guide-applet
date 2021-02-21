package com.guide.common.utils;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private Long total;
    private Long totalPage;
    private int page;
    private int rows;
    private List<T> list;

    public PageResult(Long total, int page, List<T> items,int rows) {
        this.total = total;
        this.list = items;
        if(page<=0) {
            page=1;
        }
        this.page = page;
        if (rows <= 0) {
            rows = 5;
        }
        this.rows = items.size();
        if (total % rows == 0) {
            this.totalPage = total / rows;
        } else {
            this.totalPage = total / rows+1;
        }
    }


}
