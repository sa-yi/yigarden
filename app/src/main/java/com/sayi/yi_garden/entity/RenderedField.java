package com.sayi.yi_garden.entity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class RenderedField {

    @SerializedName("rendered")
    private String rendered="";

    public String getRendered() {
        return rendered;
    }

    @NonNull
    @Override
    public String toString() {
        boolean isProtected = false;
        return "RenderedField{" +
                "rendered='" + rendered + "'" +
                ", isProtected=" + isProtected +
                '}';
    }
}