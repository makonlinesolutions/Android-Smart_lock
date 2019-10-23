package com.nova_smartlock.model;

import java.util.List;

public class PasscodeListResponse {
    public List<PasscodeListItem> mPasscodeListItem = null;
    public int pageNo;
    public int pageSize;
    public int pages;
    public int total;


    public PasscodeListResponse(List<PasscodeListItem> mPasscodeListItem, int pageNo, int pageSize, int pages, int total) {
        this.mPasscodeListItem = mPasscodeListItem;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.pages = pages;
        this.total = total;
    }

    public PasscodeListResponse() {
    }

    public List<PasscodeListItem> getmPasscodeListItem() {
        return mPasscodeListItem;
    }

    public void setmPasscodeListItem(List<PasscodeListItem> mPasscodeListItem) {
        this.mPasscodeListItem = mPasscodeListItem;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
