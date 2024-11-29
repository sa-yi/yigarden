package com.sayi.vdim.entity;

import com.google.gson.annotations.SerializedName;

public class User {
    public AvatarUrls getAvatarUrls() {
        return avatarUrls;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @SerializedName("id")
    private int id=-1;

    @SerializedName("name")
    private String name="";

    @SerializedName("url")
    private String url="";

    @SerializedName("description")
    private String description="";

    @SerializedName("link")
    private String link="";

    @SerializedName("slug")
    private String slug="";

    @SerializedName("avatar_urls")
    private AvatarUrls avatarUrls =null;

    public String getName(){
        return name;
    }
    public AvatarUrls getAvatorUrls(){
        return avatarUrls;
    }

    public class AvatarUrls {
        @SerializedName("24")
        public String min="";

        @SerializedName("48")
        public String midium="";

        @SerializedName("96")
        public String max="";

        @Override
        public String toString() {
            return "AvatarUrls{" +
                    "min='" + min + '\'' +
                    ", midium='" + midium + '\'' +
                    ", max='" + max + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", slug='" + slug + '\'' +
                ", avatarUrls=" + avatarUrls +
                '}';
    }
}
