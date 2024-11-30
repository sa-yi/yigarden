package com.sayi.vdim.dz_entity;

import com.google.gson.annotations.*;

public class BaseResponse {
    @SerializedName("Version")
    private String version;

    @SerializedName("Charset")
    private String charset;

    public BaseVariables getVariables() {
        return variables;
    }

    @Expose(serialize = false, deserialize = false)
    private BaseVariables variables;



    public static class BaseVariables {
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

        public static class Notice {

            @SerializedName("newpush")
            private String newpush;

            @SerializedName("newpm")
            private String newpm;

            @SerializedName("newprompt")
            private String newprompt;

            @SerializedName("newmypost")
            private String newmypost;

            @Override
            public String toString() {
                return "Notice{" +
                        "newpush='" + newpush + '\'' +
                        ", newpm='" + newpm + '\'' +
                        ", newprompt='" + newprompt + '\'' +
                        ", newmypost='" + newmypost + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "BaseVariables{" +
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
                    '}';
        }
    }


}
