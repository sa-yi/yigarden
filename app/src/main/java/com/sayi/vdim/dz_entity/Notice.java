package com.sayi.vdim.dz_entity;

import com.google.gson.annotations.*;

public class Notice {

    @SerializedName("newpush")
    private String newpush;

    @SerializedName("newpm")
    private String newpm;

    @SerializedName("newprompt")
    private String newprompt;

    @SerializedName("newmypost")
    private String newmypost;

    @Override
    public String toString() {
        return "Notice{" +
                "newpush='" + newpush + '\'' +
                ", newpm='" + newpm + '\'' +
                ", newprompt='" + newprompt + '\'' +
                ", newmypost='" + newmypost + '\'' +
                '}';
    }
}