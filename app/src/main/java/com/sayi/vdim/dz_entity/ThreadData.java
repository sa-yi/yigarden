package com.sayi.vdim.dz_entity;

import androidx.annotation.*;

import com.google.gson.annotations.*;

import java.util.*;

public class ThreadData extends BaseResponse {
    @SerializedName("Variables")
    private Variables variables;

    public String getSubject() {
        return variables.subject;
    }

    public String getTid() {
        return variables.tid;
    }

    public String getAuthor() {
        return variables.author;
    }

    public String getAuthorId() {
        return variables.authorId;
    }

    public String getLastpost() {
        return variables.lastpost;
    }

    public ArrayList<Post> getPost() {
        return variables.post;
    }

    public Variables getSingleVariable() {
        return variables.getSingleVariable();
    }


    @NonNull
    @Override
    public String toString() {
        return super.toString() + variables;
    }


    public static class Variables extends BaseVariables {
        @SerializedName("thread")
        private Variables singleVariable;
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
        @SerializedName("postlist")
        private ArrayList<ThreadData.Post> post;


        //@SerializedName("attachmentImagePreviewList")
        //private List<String> attachmentImagePreviewList;
        @SerializedName("special_poll")
        private SpecialPoll specialPoll;
        @SerializedName("expirations")
        private long expirations;
        @SerializedName("multiple")
        private int multiple;
        @SerializedName("maxchoices")
        private int maxchoices;
        @SerializedName("voterscount")
        private int voterscount;
        @SerializedName("visiblepoll")
        private int visiblepoll;
        @SerializedName("allowvote")
        private int allowvote;
        @SerializedName("remaintime")
        private List<Integer> remaintime;

        public Variables getSingleVariable() {
            return singleVariable;
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

        public ArrayList<ThreadData.Post> getPost() {
            return post;
        }

        public SpecialPoll getSpecialPoll() {
            return specialPoll;
        }


        public String getSubject() {
            return subject;
        }

        public String getTid() {
            return tid;
        }

        public String getAuthor() {
            return author;
        }

        public String getAuthorId() {
            return authorId;
        }

        public String getLastpost() {
            return lastpost;
        }
        public String getMessage() {
            return message;
        }
    }
    public String getMessage() {
        return variables.message;
    }

    public static class SpecialPoll {
        @SerializedName("polloptions")
        private Map<Integer, PollOption> pollOptions;

        public static class PollOption {
            @SerializedName("polloptionid")
            private int polloptionId;
            @SerializedName("polloption")
            private String polloption;
            @SerializedName("votes")
            private int votes;
            @SerializedName("width")
            private String width;
            @SerializedName("percent")
            private float percent;
            @SerializedName("color")
            private String color;
            @SerializedName("imginfo")
            private List imginfo;
        }
    }

    public static class Post {
        @SerializedName("pid")
        private int pid;
        @SerializedName("tid")
        private int tid;
        @SerializedName("first")
        private int first;
        @SerializedName("author")
        private String author;
        @SerializedName("authorid")
        private int authorid;
        @SerializedName("dateline")
        private String dateline;
        @SerializedName("message")
        private String message;
        @SerializedName("anonymous")
        private int anonymous;
        @SerializedName("attachment")
        private int attachment;
        @SerializedName("status")
        private int status;
        @SerializedName("replycredit")
        private int replycredit;
        @SerializedName("position")
        private int position;
        @SerializedName("username")
        private String username;
        @SerializedName("adminid")
        private int adminid;
        @SerializedName("groupid")
        private int groupid;
        @SerializedName("memberstatus")
        private int memberstatus;
        @SerializedName("number")
        private int number;
        @SerializedName("dbdateline")
        private int dbdateline;
        @SerializedName("attachments")
        private Map<Integer, ThreadAttachment> attachments;
        @SerializedName("imagelist")
        private ArrayList<Integer> imagelist;
        @SerializedName("groupiconid")
        private String groupiconid;

        public String getAuthor() {
            return author;
        }

        public int getAuthorid() {
            return authorid;
        }

        public String getDateline() {
            return dateline;
        }

        public String getMessage() {
            return message;
        }

        public Map<Integer, ThreadAttachment> getAttachments() {
            return attachments;
        }

        public ArrayList<Integer> getImagelist() {
            return imagelist;
        }

        @Override
        public String toString() {
            return "Post{" +
                    "pid=" + pid +
                    ", tid=" + tid +
                    ", first=" + first +
                    ", author='" + author + '\'' +
                    ", authorid=" + authorid +
                    ", dateline='" + dateline + '\'' +
                    ", message='" + message + '\'' +
                    ", anonymous=" + anonymous +
                    ", attachment=" + attachment +
                    ", status=" + status +
                    ", replycredit=" + replycredit +
                    ", position=" + position +
                    ", username='" + username + '\'' +
                    ", adminid=" + adminid +
                    ", groupid=" + groupid +
                    ", memberstatus=" + memberstatus +
                    ", number=" + number +
                    ", dbdateline=" + dbdateline +
                    ", attachments=" + attachments +
                    ", imagelist=" + imagelist +
                    ", groupiconid='" + groupiconid + '\'' +
                    '}';
        }
    }
}