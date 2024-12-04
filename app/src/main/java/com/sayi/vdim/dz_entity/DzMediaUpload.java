package com.sayi.vdim.dz_entity;

import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;

import retrofit2.Response;

public class DzMediaUpload {
    public static void upload(File file, MediaUploadCallback callback) throws IOException {


        Tika tika=new Tika();
        String mimeType=tika.detect(file);
        if (mimeType == null) {
            callback.onParsedError();
            return;
        }



    }

    public interface MediaUploadCallback {
        void onSuccess(Response<WpMediaItem> response);
        void onFailure(Throwable throwable);
        void onParsedError();
    }
}
