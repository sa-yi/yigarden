package com.sayi.vdim.sayi_music_entity;

import com.google.gson.annotations.*;

public class Music {
    public int getId() {
        return id;
    }

    @SerializedName("id")
    private int id;

    public String getName() {
        return name;
    }

    @SerializedName("name")
    private String name;
    @SerializedName("picId")
    private int picId;

    @SerializedName("picUrl")
    private String picUrl;


    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picId=" + picId +
                ", picUrl='" + picUrl + '\'' +
                '}';
    }
}
