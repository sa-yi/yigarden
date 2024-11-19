package com.sayi.yi_garden.entity;

import androidx.annotation.NonNull;

import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaUpload {
    public static void upload(File file, MediaUploadCallback callback) throws IOException {


        Tika tika=new Tika();
        String mimeType=tika.detect(file);
        if (mimeType == null) {
            callback.onParsedError();
            return;
        }

        RequestBody requestBody = RequestBody.create(Objects.requireNonNull(MediaType.parse(mimeType)), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // 调用接口
        Call<ResponseBody> call = apiService.uploadMedia(body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
                callback.onFailure(throwable);
            }
        });

    }

    public interface MediaUploadCallback {
        void onSuccess(Response response);
        void onFailure(Throwable throwable);
        void onParsedError();
    }
}
