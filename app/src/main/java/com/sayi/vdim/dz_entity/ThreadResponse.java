package com.sayi.vdim.dz_entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ThreadResponse {

    @SerializedName("Version")
    private String version;

    @SerializedName("Charset")
    private String charset;

    @SerializedName("Variables")
    private Variables variables;

    @Override
    public String toString() {
        return "ThreadResponse{" +
                "version='" + version + '\'' +
                ", charset='" + charset + '\'' +
                ", variables=" + variables +
                '}';
    }

    public Variables getVariables(){
        return variables;
    }

    public static class Variables {

        @SerializedName("cookiepre")
        private String cookiepre;

        @SerializedName("auth")
        private String auth;

        @SerializedName("saltkey")
        private String saltkey;

        @SerializedName("member_uid")
        private String memberUid;

        @SerializedName("member_username")
        private String memberUsername;

        @SerializedName("member_avatar")
        private String memberAvatar;

        @SerializedName("groupid")
        private String groupid;

        @SerializedName("formhash")
        private String formhash;

        @SerializedName("ismoderator")
        private String ismoderator;

        @SerializedName("readaccess")
        private String readaccess;

        @SerializedName("notice")
        private Notice notice;

        public List<ThreadData> getData() {
            return data;
        }

        @SerializedName("data")
        private List<ThreadData> data;

        @Override
        public String toString() {
            return "Variables{" +
                    "cookiepre='" + cookiepre + '\'' +
                    ", auth='" + auth + '\'' +
                    ", saltkey='" + saltkey + '\'' +
                    ", memberUid='" + memberUid + '\'' +
                    ", memberUsername='" + memberUsername + '\'' +
                    ", memberAvatar='" + memberAvatar + '\'' +
                    ", groupid='" + groupid + '\'' +
                    ", formhash='" + formhash + '\'' +
                    ", ismoderator='" + ismoderator + '\'' +
                    ", readaccess='" + readaccess + '\'' +
                    ", notice=" + notice +
                    ", data=" + data +
                    '}';
        }
    }
}
