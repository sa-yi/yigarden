package com.sayi.vdim.dz_entity;

import com.google.gson.annotations.*;

import java.util.*;

public class DzUser extends BaseResponse {

    @SerializedName("Variables")
    private Variables variables;
    @SerializedName("uid")
    private int uid;
    @SerializedName("username")
    private String username;
    @SerializedName("secmobicc")
    private int secmobicc;
    @SerializedName("secmobile")
    private int secmobile;
    @SerializedName("status")
    private int status;
    @SerializedName("emailstatus")
    private int emailstatus;
    @SerializedName("avatarstatus")
    private int avatarstatus;
    @SerializedName("secmobilestatus")
    private int secmobilestatus;
    @SerializedName("adminid")
    private int adminid;
    @SerializedName("groupid")
    private int groupid;
    @SerializedName("groupexpiry")
    private int groupexpiry;
    @SerializedName("extgroupids")
    private String extgroupids;
    @SerializedName("regdate")
    private String regdate;
    @SerializedName("credits")
    private int credits;//积分
    @SerializedName("notifysound")
    private String notifysound;
    @SerializedName("timeoffset")
    private int timeoffset;
    @SerializedName("newpm")
    private String newpm;
    @SerializedName("newprompt")
    private String newprompt;
    @SerializedName("accessmasks")
    private String accessmasks;
    @SerializedName("allowadmincp")
    private String allowadmincp;
    @SerializedName("onlyacceptfriendpm")
    private String onlyacceptfriendpm;
    @SerializedName("conisbind")
    private String conisbind;
    @SerializedName("freeze")
    private String freeze;
    @SerializedName("self")
    private int self;
    @SerializedName("extcredits1")
    private String extcredits1;
    @SerializedName("extcredits2")
    private String extcredits2;
    @SerializedName("extcredits3")
    private String extcredits3;
    @SerializedName("extcredits4")
    private String extcredits4;
    @SerializedName("extcredits5")
    private String extcredits5;
    @SerializedName("extcredits6")
    private String extcredits6;
    @SerializedName("extcredits7")
    private String extcredits7;
    @SerializedName("extcredits8")
    private String extcredits8;
    @SerializedName("friends")
    private String friends;
    @SerializedName("posts")
    private String posts;
    @SerializedName("threads")
    private String threads;
    @SerializedName("digestposts")
    private String digestposts;
    @SerializedName("doings")
    private String doings;
    @SerializedName("blogs")
    private String blogs;
    @SerializedName("albums")
    private String albums;
    @SerializedName("sharings")
    private String sharings;
    @SerializedName("attachsize")
    private String attachsize;
    @SerializedName("views")
    private String views;
    @SerializedName("oltime")
    private String oltime;
    @SerializedName("todayattachs")
    private String todayattachs;
    @SerializedName("todayattachsize")
    private String todayattachsize;
    @SerializedName("feeds")
    private String feeds;
    @SerializedName("follower")
    private String follower;
    @SerializedName("following")
    private String following;
    @SerializedName("newfollower")
    private String newfollower;
    @SerializedName("blacklist")
    private String blacklist;
    @SerializedName("spacename")
    private String spacename;
    @SerializedName("spacedescription")
    private String spacedescription;
    @SerializedName("domain")
    private String domain;
    @SerializedName("addsize")
    private String addsize;
    @SerializedName("addfriend")
    private String addfriend;
    @SerializedName("allowasfriend")
    private String allowasfriend;
    @SerializedName("allowasfollow")
    private String allowasfollow;
    @SerializedName("menunum")
    private String menunum;
    @SerializedName("theme")
    private String theme;
    @SerializedName("spacecss")
    private String spacecss;
    @SerializedName("blockposition")
    private String blockposition;
    @SerializedName("recentnote")
    private String recentnote;
    @SerializedName("spacenote")
    private String spacenote;
    @SerializedName("privacy")
    private Privacy privacy;
    @SerializedName("admingroup")
    private Admingroup admingroup;
    @SerializedName("group")
    private Group group;
    @SerializedName("extcredits")
    private Map<String, Credits> extraCredits;
    @SerializedName("wsq")
    private Wsq wsq;

    public DzUser getSpace() {
        return variables.space;
    }

    private Variables getVariables() {
        return variables;
    }

    public int getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "DzUser{" +
                "variables=" + variables +
                ", uid=" + uid +
                ", username='" + username + '\'' +
                ", secmobicc=" + secmobicc +
                ", secmobile=" + secmobile +
                ", status=" + status +
                ", emailstatus=" + emailstatus +
                ", avatarstatus=" + avatarstatus +
                ", secmobilestatus=" + secmobilestatus +
                ", adminid=" + adminid +
                ", groupid=" + groupid +
                ", groupexpiry=" + groupexpiry +
                ", extgroupids='" + extgroupids + '\'' +
                ", regdate='" + regdate + '\'' +
                ", credits=" + credits +
                ", notifysound='" + notifysound + '\'' +
                ", timeoffset=" + timeoffset +
                ", newpm='" + newpm + '\'' +
                ", newprompt='" + newprompt + '\'' +
                ", accessmasks='" + accessmasks + '\'' +
                ", allowadmincp='" + allowadmincp + '\'' +
                ", onlyacceptfriendpm='" + onlyacceptfriendpm + '\'' +
                ", conisbind='" + conisbind + '\'' +
                ", freeze='" + freeze + '\'' +
                ", self=" + self +
                ", extcredits1='" + extcredits1 + '\'' +
                ", extcredits2='" + extcredits2 + '\'' +
                ", extcredits3='" + extcredits3 + '\'' +
                ", extcredits4='" + extcredits4 + '\'' +
                ", extcredits5='" + extcredits5 + '\'' +
                ", extcredits6='" + extcredits6 + '\'' +
                ", extcredits7='" + extcredits7 + '\'' +
                ", extcredits8='" + extcredits8 + '\'' +
                ", friends='" + friends + '\'' +
                ", posts='" + posts + '\'' +
                ", threads='" + threads + '\'' +
                ", digestposts='" + digestposts + '\'' +
                ", doings='" + doings + '\'' +
                ", blogs='" + blogs + '\'' +
                ", albums='" + albums + '\'' +
                ", sharings='" + sharings + '\'' +
                ", attachsize='" + attachsize + '\'' +
                ", views='" + views + '\'' +
                ", oltime='" + oltime + '\'' +
                ", todayattachs='" + todayattachs + '\'' +
                ", todayattachsize='" + todayattachsize + '\'' +
                ", feeds='" + feeds + '\'' +
                ", follower='" + follower + '\'' +
                ", following='" + following + '\'' +
                ", newfollower='" + newfollower + '\'' +
                ", blacklist='" + blacklist + '\'' +
                ", spacename='" + spacename + '\'' +
                ", spacedescription='" + spacedescription + '\'' +
                ", domain='" + domain + '\'' +
                ", addsize='" + addsize + '\'' +
                ", addfriend='" + addfriend + '\'' +
                ", allowasfriend='" + allowasfriend + '\'' +
                ", allowasfollow='" + allowasfollow + '\'' +
                ", menunum='" + menunum + '\'' +
                ", theme='" + theme + '\'' +
                ", spacecss='" + spacecss + '\'' +
                ", blockposition='" + blockposition + '\'' +
                ", recentnote='" + recentnote + '\'' +
                ", spacenote='" + spacenote + '\'' +
                ", privacy=" + privacy +
                ", admingroup=" + admingroup +
                ", group=" + group +
                ", extraCredits=" + extraCredits +
                ", wsq=" + wsq +
                '}';
    }

    // Nested classes for complex objects
    public static class Privacy {
        @SerializedName("feed")
        private Map<String, String> feed;
        @SerializedName("view")
        private Map<String, String> view;
        @SerializedName("profile")
        private Map<String, String> profile;

        // Getters and setters for privacy fields
    }

    public static class Admingroup {
        @SerializedName("icon")
        private String icon;

        // Getters and setters for admingroup fields
    }

    public static class Group {
        @SerializedName("type")
        private String type;
        @SerializedName("grouptitle")
        private String grouptitle;
        @SerializedName("creditshigher")
        private String creditshigher;
        @SerializedName("creditslower")
        private String creditslower;
        @SerializedName("stars")
        private String stars;

        public String getGrouptitle() {
            return grouptitle;
        }
    }

    public static class Credits {
        @SerializedName("img")
        private String img;
        @SerializedName("title")
        private String title;
        @SerializedName("unit")
        private String unit;
        @SerializedName("ratio")
        private float ratio;
        @SerializedName("showinthread")
        private Object showinthread;
        @SerializedName("allowexchangein")
        private Object allowexchangein;
        @SerializedName("allowexchangeout")
        private Object allowexchangeout;
    }

    public static class Wsq {
        @SerializedName("wsq_apicredit")
        Object wsq_apicredit;
    }

    public class Variables extends BaseVariables {
        @SerializedName("space")
        private DzUser space;
    }
}
