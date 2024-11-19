package com.sayi.yi_garden.utils;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PicUIUploader {


    static String token = "Bearer 211|cZUJbFc4BJAT8RsgbyiHLQNgz94uRzGZakuY30N5";
    static String uploadUrl = "https://sa-yi.cn/wp-json/wp/v2/meida";
    static String filePath = "C:\\Users\\Administrator\\Pictures\\微信图片_20231104191108.jpg";



    public static void main(String[] args) {
        upload(filePath, new OnCompleteListener() {
            @Override
            public void onComplete(String url) {

            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }

    public static void upload(String filePath,OnCompleteListener listener){
        File file=new File(filePath);
        upload(file,listener);
    }


    public static void upload(File imageFile,OnCompleteListener listener){

        //File imageFile = new File(filePath);

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imageFile.getName(),
                        RequestBody.create(Objects.requireNonNull(MediaType.parse("image/*")), imageFile))
                .build();

        Request request = new Request.Builder()
                .url(uploadUrl)
                .addHeader("Authorization", token)
                .addHeader("Accept", "application/json")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                System.out.println("Upload Failed: " + e.getMessage());
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    responseBody=responseBody.replace("\\/","/");
                    System.out.println("Upload Successful!");
                    System.out.println(responseBody);
                    System.out.println("matched:"+match(responseBody));
                    listener.onComplete(match(responseBody));
                    /*try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String imageUrl = jsonResponse.getJSONObject("data").getString("url");
                        System.out.println("Image URL: " + imageUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Failed to parse response: " + e.getMessage());
                    }*/
                } else {
                    System.out.println("Upload Failed: " + response.code() + ", " + response.body().string());
                    listener.onFailure(response.body().string());
                }
            }
        });
    }


    public interface OnCompleteListener{
        void onComplete(String url);
        void onFailure(String errorMsg);
    }


    static String match(String group){
        // 正则表达式匹配 "links" 中的 "url"
        String regex = "\"url\":\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(group);


        String url="";
        if (matcher.find()) {
            url = matcher.group(1);
            System.out.println("URL: " + url);
        } else {
            System.out.println("URL not found");
        }
        return url;
    }
}
