package com.sayi.vdim.dz_entity;

import com.google.gson.annotations.*;

import java.util.*;

public class Forum extends BaseResponse{


    public ArrayList<Forum> getForums(){
        return variables.getForums();
    }

    @Override
    public Variables getVariables() {
        return variables;
    }

    @SerializedName("Variables")
    private Variables variables;

    @Override
    public String toString() {
        return "Forum{" +
                "variables=" + variables +
                ", fid=" + fid +
                ", name='" + name + '\'' +
                ", threadCount=" + threadCount +
                ", postCount=" + postCount +
                ", toadyPosts=" + toadyPosts +
                ", description='" + description + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }

    public static class Variables extends BaseVariables {
        public ArrayList<Forum> getForums() {
            return forums;
        }

        @SerializedName("forumlist")
        private ArrayList<Forum> forums;

        @SerializedName("catlist")
        private ArrayList<Category> categories;

        @Override
        public String toString() {
            return "Variables{" +
                    "forums=" + forums +
                    ", categories=" + categories +
                    '}';
        }
    }

    @SerializedName("fid")
    private int fid;
    @SerializedName("name")
    private String name;
    @SerializedName("threads")
    private int threadCount;
    @SerializedName("posts")
    private int postCount;
    @SerializedName("todayposts")
    private int toadyPosts;
    @SerializedName("description")
    private String description;
    @SerializedName("icon")
    private String iconUrl;




    public class Category{
        @SerializedName("fid")
        private int fid;
        @SerializedName("name")
        private String name;
        @SerializedName("forums")
        private ArrayList<Integer> forums;
    }
}
