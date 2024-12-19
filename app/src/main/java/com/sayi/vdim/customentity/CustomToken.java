package com.sayi.vdim.customentity;

import com.google.gson.annotations.*;

public class CustomToken {

    public String getToken() {
        return token;
    }

    @SerializedName("token")
    private String token;
}
