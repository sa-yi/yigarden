package com.sayi.vdim.dz_entity;


import com.google.gson.annotations.SerializedName;

public class FollowingClassification {
    @SerializedName("id")
    private int id=-1;
    @SerializedName("tag")
    private String tag="全部";

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }
}
