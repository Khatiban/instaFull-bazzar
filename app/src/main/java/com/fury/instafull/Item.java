package com.fury.instafull;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("l")
    private String f7l;
    @SerializedName("t")
    private String f8t;
    @SerializedName("p")
    private String f9t;
    public String getT() {
        return this.f8t;
    }

    public void setT(String t) {
        this.f8t = t;
    }

    public String getL() {
        return this.f7l;
    }

    public void setL(String l) {
        this.f7l = l;
    }
}
