package com.sayi.music;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MusicScanner {
    public static ArrayList<MediaItem> mediaItems;
    @SuppressLint("Range")
    public static void scanLocalMedia(Context context, MediaScanListener listener) {
        mediaItems = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ArrayList<MediaItem>> future = executor.submit(() -> {

            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DATA};
            String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

            try {
                // 查询媒体文件
                android.database.Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, sortOrder);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                        MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                                .setTitle(title)
                                .setArtist(artist)
                                .setAlbumTitle(album)
                                .build();

                        mediaItems.add(new MediaItem.Builder()
                                .setMediaMetadata(mediaMetadata)
                                .setUri(Uri.parse(data))
                                .build());
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mediaItems;
        });

        executor.shutdown();

        try {
            ArrayList<MediaItem> mediaItems = future.get();
            if (listener != null) {
                listener.onMediaScanComplete(mediaItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface MediaScanListener {
        void onMediaScanComplete(ArrayList<MediaItem> mediaItems);
    }
}
