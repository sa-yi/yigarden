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

    @SerializedName("dateline")
    private String dateline;

    @SerializedName("filename")
    private String filename;

    @SerializedName("filesize")
    private int filesize;

    @SerializedName("attachment")
    private String attachment;

    @SerializedName("remote")
    private int remote;

    @SerializedName("description")
    private String description;

    @SerializedName("readperm")
    private int readperm;

    @SerializedName("price")
    private int price;

    @SerializedName("isimage")
    private int isimage;

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
    @SerializedName("attachimg")
    private int attachimg;

    @SerializedName("payed")
    private int payed;

    @SerializedName("url")
    private String url;

    @SerializedName("dbdateline")
    private int dbdateline;

    @SerializedName("aidencode")
    private String aidencode;

    @SerializedName("downloads")
    private int downloads;

    // Getters and Setters for each field
    public int getAid() {
        return aid;
    }

    public int getTid() {
        return tid;
    }

    public int getPid() {
        return pid;
    }

    public int getUid() {
        return uid;
    }

    public String getDateline() {
        return dateline;
    }

    public String getFilename() {
        return filename;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public String getAttachment() {
        return attachment;
    }

    public int getRemote() {
        return remote;
    }

    public String getDescription() {
        return description;
    }

    public int getReadperm() {
        return readperm;
    }

    public int getPrice() {
        return price;
    }

    public int getIsImage() {
        return isimage;
    }

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

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }

    public int getPicid() {
        return picid;
    }

    public void setPicid(int picid) {
        this.picid = picid;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getImgalt() {
        return imgalt;
    }

    public void setImgalt(String imgalt) {
        this.imgalt = imgalt;
    }

    public String getAttachicon() {
        return attachicon;
    }

    public void setAttachicon(String attachicon) {
        this.attachicon = attachicon;
    }

    public String getAttachsize() {
        return attachsize;
    }


    /**
     *值为1表示以附件形式传递图片
     */
    public int getAttachimg() {
        return attachimg;
    }

    public int getPayed() {
        return payed;
    }

    public void setPayed(int payed) {
        this.payed = payed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDbdateline() {
        return dbdateline;
    }

    public void setDbdateline(int dbdateline) {
        this.dbdateline = dbdateline;
    }

    public String getAidencode() {
        return aidencode;
    }

    public void setAidencode(String aidencode) {
        this.aidencode = aidencode;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    @Override
    public String toString() {
        return "ThreadAttachment{" +
                "aid=" + aid +
                ", tid=" + tid +
                ", pid=" + pid +
                ", uid=" + uid +
                ", dateline='" + dateline + '\'' +
                ", filename='" + filename + '\'' +
                ", filesize=" + filesize +
                ", attachment='" + attachment + '\'' +
                ", remote=" + remote +
                ", description='" + description + '\'' +
                ", readperm=" + readperm +
                ", price=" + price +
                ", isimage=" + isimage +
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
