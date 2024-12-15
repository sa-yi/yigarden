package com.sayi.vdim.sayi_music_entity;

import com.google.gson.annotations.*;

public class MusicFully {
    @SerializedName("code")
    private int code = -1;

    @SerializedName("data")
    private Data data;

    public boolean isSuccessful() {
        return code == 1;
    }

    public String getPic() {
        return data.pic;
    }

    public String getUrl() {
        return data.url;
    }

    public String getArtist() {
        return data.artist;
    }

    public String getName() {
        return data.name;
    }

    public String getLrc() {
        return data.lrc;
    }

    public class Data {
        @SerializedName("name")
        private String name;
        @SerializedName("album")
        private String album;
        @SerializedName("artist")
        private String artist;
        @SerializedName("picid")
        private long picId;
        @SerializedName("url")
        private String url;
        @SerializedName("pic")
        private String pic;
        @SerializedName("lrc")
        private String lrc;
    }
}
