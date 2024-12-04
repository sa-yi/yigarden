package com.sayi.vdim.dz_entity;

import com.google.gson.annotations.*;

import java.util.*;

public class ForumNav extends BaseResponse{

    @Override
    public Variables getVariables() {
        return variables;
    }

    @SerializedName("Variables")
    private Variables variables;

    @Override
    public String toString() {
        return "ForumNav{" +
                "fid=" + fid +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", fup=" + fup +
                ", status=" + status +
                ", threadTypes=" + threadTypes +
                ", viewPerm='" + viewPerm + '\'' +
                ", postPerm='" + postPerm + '\'' +
                '}';
    }

    public static class Variables extends BaseVariables {
        public ArrayList<ForumNav> getForumNavs() {
            return forumNavs;
        }

        @SerializedName("forums")
        private ArrayList<ForumNav> forumNavs;
    }

    @SerializedName("fid")
    private int fid;
    @SerializedName("type")
    private String type;
    @SerializedName("name")
    private String name;
    @SerializedName("fup")
    private int fup;
    @SerializedName("status")
    private int status;
    @SerializedName("threadypes")
    private ThreadType threadTypes;

    @SerializedName("viewperm")
    private String viewPerm;

    @SerializedName("postperm")
    private String postPerm;



    class ThreadType {
        @SerializedName("required")
        private int required;
        @SerializedName("types")
        private HashMap<Integer, String> types;
    }
}
