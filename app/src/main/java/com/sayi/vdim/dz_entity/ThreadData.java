package com.sayi.vdim.dz_entity;

import androidx.annotation.*;

import com.google.gson.annotations.*;

public class ThreadData extends BaseResponse {

    @SerializedName("Variables")
    private Variables variables;

    @Override
    public Variables getVariables() {
        return variables;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + variables;
    }

    public static class Variables extends BaseVariables {



            @SerializedName("tid")
            private String tid;
            @SerializedName("fid")
            private String fid;
            @SerializedName("posttableid")
            private String posttableid;
            @SerializedName("typeid")
            private String typeid;
            @SerializedName("sortid")
            private String sortid;
            @SerializedName("readperm")
            private String readperm;
            @SerializedName("price")
            private String price;
            @SerializedName("author")
            private String author;
            @SerializedName("authorid")
            private String authorId;
            @SerializedName("subject")
            private String subject;
            @SerializedName("dateline")
            private String dateline;
            @SerializedName("lastpost")
            private String lastpost;
            @SerializedName("lastposter")
            private String lastposter;
            @SerializedName("views")
            private String views;
            @SerializedName("replies")
            private String replies;
            @SerializedName("displayorder")
            private String displayorder;
            @SerializedName("highlight")
            private String highlight;
            @SerializedName("digest")
            private String digest;
            @SerializedName("rate")
            private String rate;
            @SerializedName("special")
            private String special;
            @SerializedName("attachment")
            private String attachment;
            @SerializedName("moderated")
            private String moderated;
            @SerializedName("closed")
            private String closed;
            @SerializedName("stickreply")
            private String stickreply;
            @SerializedName("recommends")
            private String recommends;
            @SerializedName("recommend_add")
            private String recommendAdd;
            @SerializedName("recommend_sub")
            private String recommendSub;
            @SerializedName("heats")
            private String heats;
            @SerializedName("status")
            private String status;
            @SerializedName("isgroup")
            private String isgroup;
            @SerializedName("favtimes")
            private String favtimes;
            @SerializedName("sharetimes")
            private String sharetimes;
            @SerializedName("stamp")
            private String stamp;
            @SerializedName("icon")
            private String icon;
            @SerializedName("pushedaid")
            private String pushedaid;
            @SerializedName("cover")
            private String cover;
            @SerializedName("replycredit")
            private String replycredit;
            @SerializedName("relatebytag")
            private String relatebytag;
            @SerializedName("maxposition")
            private String maxposition;
            @SerializedName("bgcolor")
            private String bgcolor;
            @SerializedName("comments")
            private Object comments;
            @SerializedName("hidden")
            private String hidden;
            @SerializedName("lastposterenc")
            private String lastposterenc;
            @SerializedName("multipage")
            private String multipage;
            @SerializedName("recommendicon")
            private String recommendicon;
            @SerializedName("new")
            private String isNew;
            @SerializedName("heatlevel")
            private String heatlevel;
            @SerializedName("moved")
            private String moved;
            @SerializedName("icontid")
            private String icontid;
            @SerializedName("folder")
            private String folder;
            @SerializedName("dbdateline")
            private String dbdateline;
            @SerializedName("weeknew")
            private String weeknew;
            @SerializedName("istoday")
            private String isToday;
            @SerializedName("dblastpost")
            private String dblastpost;
            @SerializedName("id")
            private String id;
            @SerializedName("rushreply")
            private String rushreply;
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("attachmentImageNumber")
            private String attachmentImageNumber;
            @SerializedName("message")
            private String message;

            public String getTid() {
                return tid;
            }

            public String getAuthor() {
                return author;
            }

            public String getAuthorId() {
                return authorId;
            }

            public String getSubject() {
                return subject;
            }

            public String getLastpost() {
                return lastpost;
            }

            //@SerializedName("attachmentImagePreviewList")
            //private List<String> attachmentImagePreviewList;

            public String getMessage() {
                return message;
            }


            @Override
            public String toString() {
                return "Variables{" +
                        "tid='" + tid + '\'' +
                        ", fid='" + fid + '\'' +
                        ", posttableid='" + posttableid + '\'' +
                        ", typeid='" + typeid + '\'' +
                        ", sortid='" + sortid + '\'' +
                        ", readperm='" + readperm + '\'' +
                        ", price='" + price + '\'' +
                        ", author='" + author + '\'' +
                        ", authorId='" + authorId + '\'' +
                        ", subject='" + subject + '\'' +
                        ", dateline='" + dateline + '\'' +
                        ", lastpost='" + lastpost + '\'' +
                        ", lastposter='" + lastposter + '\'' +
                        ", views='" + views + '\'' +
                        ", replies='" + replies + '\'' +
                        ", displayorder='" + displayorder + '\'' +
                        ", highlight='" + highlight + '\'' +
                        ", digest='" + digest + '\'' +
                        ", rate='" + rate + '\'' +
                        ", special='" + special + '\'' +
                        ", attachment='" + attachment + '\'' +
                        ", moderated='" + moderated + '\'' +
                        ", closed='" + closed + '\'' +
                        ", stickreply='" + stickreply + '\'' +
                        ", recommends='" + recommends + '\'' +
                        ", recommendAdd='" + recommendAdd + '\'' +
                        ", recommendSub='" + recommendSub + '\'' +
                        ", heats='" + heats + '\'' +
                        ", status='" + status + '\'' +
                        ", isgroup='" + isgroup + '\'' +
                        ", favtimes='" + favtimes + '\'' +
                        ", sharetimes='" + sharetimes + '\'' +
                        ", stamp='" + stamp + '\'' +
                        ", icon='" + icon + '\'' +
                        ", pushedaid='" + pushedaid + '\'' +
                        ", cover='" + cover + '\'' +
                        ", replycredit='" + replycredit + '\'' +
                        ", relatebytag='" + relatebytag + '\'' +
                        ", maxposition='" + maxposition + '\'' +
                        ", bgcolor='" + bgcolor + '\'' +
                        ", comments='" + comments + '\'' +
                        ", hidden='" + hidden + '\'' +
                        ", lastposterenc='" + lastposterenc + '\'' +
                        ", multipage='" + multipage + '\'' +
                        ", recommendicon='" + recommendicon + '\'' +
                        ", isNew='" + isNew + '\'' +
                        ", heatlevel='" + heatlevel + '\'' +
                        ", moved='" + moved + '\'' +
                        ", icontid='" + icontid + '\'' +
                        ", folder='" + folder + '\'' +
                        ", dbdateline='" + dbdateline + '\'' +
                        ", weeknew='" + weeknew + '\'' +
                        ", isToday='" + isToday + '\'' +
                        ", dblastpost='" + dblastpost + '\'' +
                        ", id='" + id + '\'' +
                        ", rushreply='" + rushreply + '\'' +
                        ", avatar='" + avatar + '\'' +
                        ", attachmentImageNumber='" + attachmentImageNumber + '\'' +
                        ", message='" + message + '\'' +
                        '}';
            }

    }
}