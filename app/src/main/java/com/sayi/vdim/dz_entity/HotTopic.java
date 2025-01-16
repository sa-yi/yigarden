package com.sayi.vdim.dz_entity;

import android.annotation.*;

import com.google.gson.annotations.*;


public class HotTopic {
    @SerializedName("topic")
    private String topic;
    @SerializedName("hotpot")
    private int hotpot;

    public HotTopic(String topic, int hotpot) {
        this.topic = topic;
        this.hotpot = hotpot;
    }

    public String getTopic() {
        return topic;
    }

    public int getHotpot() {
        return hotpot;
    }

    @SuppressLint("DefaultLocale")
    public String getFormattedHotpot() {
        if (hotpot < 1000) {
            return String.valueOf(hotpot);
        } else if (hotpot < 10000) {
            double hotpotInK = hotpot / 1000.0;
            return String.format("%.1fk", hotpotInK);
        } else if(hotpot < 10000000){
            double hotpotInWan = hotpot / 10000.0;
            return String.format("%.2f万", hotpotInWan);
        } else {
            double hotpotInWan = hotpot / 10000000.0;
            return String.format("%.2f千万", hotpotInWan);
        }
    }

}
