package com.fury.instafull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Schema2 {
    @SerializedName("count")
    private Integer count;
    @SerializedName("items")
    private List<Item> items;
    @SerializedName("msg")
    private String msg;
    @SerializedName("status")
    private String status;

    public Schema2() {
        this.items = null;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Item> getItems() {
        return this.items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

