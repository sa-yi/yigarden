package com.sayi.vdim.dz_entity;

import android.util.*;

import com.google.gson.annotations.*;

import java.util.*;

public class ForumDetailed extends BaseResponse{
    public Forum getForum() {
        return variables.forum;
    }
    public ArrayList<ThreadData> getThreadData() {
        return variables.threadData;
    }

    @SerializedName("Variables")
    private Variables variables;

    class Variables extends BaseVariables{

        @SerializedName("forum")
        private Forum forum;
        @SerializedName("group")
        private Group group;

        @SerializedName("forum_threadlist")
        private ArrayList<ThreadData> threadData;

        @SerializedName("groupiconid")
        private ArrayMap<Integer,String> groupIconIdList;

        @SerializedName("sublist")
        private ArrayList subList;

        @SerializedName("tpp")
        private int tpp;

        @SerializedName("page")
        private int page;

        @SerializedName("reward_unit")
        private String reward_unit;

        @SerializedName("threadtypes")
        private Object threadtypes;
    }


    public class Forum {
        @SerializedName("fid")
        private int fid;

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }

        public String getName() {
            return name;
        }

        public int getThreadCount() {
            return threadCount;
        }

        @SerializedName("description")
        private String description;
        @SerializedName("icon")
        private String icon;
        @SerializedName("picstyle")
        private int picStyle;;
        @SerializedName("fup")
        private int fup;
        @SerializedName("name")
        private String name;
        @SerializedName("threads")
        private int threads;
        @SerializedName("posts")
        private int posts;
        @SerializedName("autoclose")
        private int autoClose;
        @SerializedName("threadcount")
        private int threadCount;
        @SerializedName("password")
        private int password;
    }

    private class Group{
        @SerializedName("groupid")
        private int groupId;
        @SerializedName("grouptitle")
        private String groupTitle;
    }
}
