package com.sayi.vdim.entity;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFeed {
    @SerializedName("id")
    private int id = -1;
    @SerializedName("date")
    private String date = "114-5-14";

    @SerializedName("date_gmt")
    private String date_gmt = "11-45-14";

    @SerializedName("guid")
    private RenderedField guid;

    @SerializedName("modified")
    private String modified = "no";

    @SerializedName("modified_gmt")
    private String modified_gmt = "yet";

    @SerializedName("slug")//简介
    private String slug = "what's this";

    @SerializedName("status")
    private String status = "safe";

    @SerializedName("type")
    private String type = "2333";

    @SerializedName("link")
    private String link = "null";

    @SerializedName("title")
    private RenderedField title = new RenderedField();

    @SerializedName("content")
    private RenderedField content = new RenderedField();

    @SerializedName("excerpt")
    private RenderedField excerpt = new RenderedField();

    @SerializedName("author")
    private int author = -1;

    @SerializedName("featured_media")
    private int featured_media = 0;

    @SerializedName("comment_status")
    private String comment_status = "???";

    @SerializedName("ping_status")
    private String ping_status = "loss";

    @SerializedName("sticky")
    private boolean sticky = false;

    @SerializedName("template")
    private String template = "no template";

    @SerializedName("format")
    private String format = "gogogo";

    @SerializedName("meta")
    private MetaData metaData;

    @SerializedName("categories")
    private int[] categories = new int[]{};

    @SerializedName("tags")
    private int[] tags = new int[]{};

    public int getId() {
        return id;
    }


    public String getDate() {
        return date;
    }


    public int getAuthor() {
        return author;
    }


    public void getAvatarUrl(OnGetAvatarUrl onGetAvatarUrl) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<User> callUser = apiService.getUser(id);
        callUser.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    Log.d("User", user.toString());
                    onGetAvatarUrl.onSuccess(user.getAvatorUrls().max);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {

            }
        });
    }

    public RenderedField getTitle() {
        return title;
    }

    public RenderedField getContent() {
        return content;
    }

    public RenderedField getExcerpt() {
        return excerpt;
    }

    @NonNull
    @Override
    public String toString() {
        return "PostFeed{" +
                "id=" + id +
                ", date='" + date + "'" +
                ", date_gmt='" + date_gmt + "'" +
                ", guid=" + guid +
                ", modified='" + modified + "'" +
                ", modified_gmt='" + modified_gmt + "'" +
                ", slug='" + slug + "'" +
                ", status='" + status + "'" +
                ", type='" + type + "'" +
                ", link='" + link + "'" +
                ", title=" + title +
                ", content=" + content +
                ", excerpt=" + excerpt +
                ", author=" + author +
                ", featured_media=" + featured_media +
                ", comment_status='" + comment_status + "'" +
                ", ping_status='" + ping_status + "'" +
                ", sticky=" + sticky +
                ", template='" + template + "'" +
                ", format='" + format + "'" +
                ", metaData=" + metaData +
                ", categories=" + Arrays.toString(categories) +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }


    public interface OnGetAvatarUrl {
        void onSuccess(String url);
    }

    public static class MetaData {
        private String footnotes;

        public String getFootnotes() {
            return footnotes;
        }

        public void setFootnotes(String footnotes) {
            this.footnotes = footnotes;
        }

        @NonNull
        @Override
        public String toString() {
            return "MetaData{" +
                    "footnotes='" + footnotes + "'" +
                    '}';
        }
    }
}
