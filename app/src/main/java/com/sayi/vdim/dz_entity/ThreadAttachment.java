package com.sayi.vdim.dz_entity;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ThreadAttachment implements Serializable {

    @SerializedName("aid")
    private int aid;

    @SerializedName("tid")
    private int tid;

    @SerializedName("pid")
    private int pid;

    @SerializedName("uid")
    private int uid;


    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    @SerializedName("thumb")
    private int thumb;

    @SerializedName("picid")
    private int picid;

    @SerializedName("ext")
    private String ext;

    @SerializedName("imgalt")
    private String imgalt;

    @SerializedName("attachicon")
    private String attachicon;

    @SerializedName("attachsize")
    private String attachsize;

    /**
     * 表示以附件形式传递图片
     */
    @SerializedName("isimage")
    private boolean attachimg;

    @SerializedName("payed")
    private int payed;

    @SerializedName("attachment")
    private String url;

    @SerializedName("dbdateline")
    private int dbdateline;

    @SerializedName("aidencode")
    private String aidencode;

    @SerializedName("downloads")
    private int downloads;



    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *值为true表示以附件形式传递图片
     */
    public boolean getAttachimg() {
        return attachimg;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "ThreadAttachment{" +
                "aid=" + aid +
                ", tid=" + tid +
                ", pid=" + pid +
                ", uid=" + uid +
                ", width=" + width +
                ", height=" + height +
                ", thumb=" + thumb +
                ", picid=" + picid +
                ", ext='" + ext + '\'' +
                ", imgalt='" + imgalt + '\'' +
                ", attachicon='" + attachicon + '\'' +
                ", attachsize='" + attachsize + '\'' +
                ", attachimg=" + attachimg +
                ", payed=" + payed +
                ", url='" + url + '\'' +
                ", dbdateline=" + dbdateline +
                ", aidencode='" + aidencode + '\'' +
                ", downloads=" + downloads +
                '}';
    }
}
