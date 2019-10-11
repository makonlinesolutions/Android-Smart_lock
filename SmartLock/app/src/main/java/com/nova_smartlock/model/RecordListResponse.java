package com.nova_smartlock.model;

import java.util.List;

public class RecordListResponse {

    private int total;
    private int pages;
    private int pageNo;
    private int pageSize;
    private List<RecordListItems> list = null;

    public RecordListResponse(int total, int pages, int pageNo, int pageSize, List<RecordListItems> list) {
        this.total = total;
        this.pages = pages;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.list = list;
    }

    public RecordListResponse() {
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public java.util.List<RecordListItems> getList() {
        return list;
    }

    public void setList(java.util.List<RecordListItems> list) {
        this.list = list;
    }

}
