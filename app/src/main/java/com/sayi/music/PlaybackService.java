package com.sayi.music;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;

import androidx.annotation.*;
import androidx.media3.common.*;
import androidx.media3.exoplayer.*;
import androidx.media3.session.*;

import com.sayi.*;
import com.sayi.vdim.sayi_music_entity.*;

import retrofit2.*;


public class PlaybackService extends MediaSessionService implements Player.Listener {

    //TODO 切换夜间/日间模式会触发歌曲切换
    static MediaItem.Builder mediaItemBuilder = new MediaItem.Builder();
    static MediaMetadata.Builder mediaMetadataBuilder = new MediaMetadata.Builder();
    static String TAG = "PlaybackService";
    SyService syService = SyClient.getRetrofitInstance().create(SyService.class);
    ExoPlayer player;
    private MediaSession mediaSession = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, MusicActivity.class);
        player = new ExoPlayer.Builder(this).build();
        player.addListener(this);
        mediaSession = new MediaSession.Builder(this, player).setSessionActivity(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE)).build();
    }

    @Override
    public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
        if (mediaItem == null) {
            Log.d(TAG, "nullTransition");
            return;
        }
        Log.d(TAG, "onMediaItemTransition: " + mediaItem.mediaMetadata.title);
        MediaMetadata metadata = mediaItem.mediaMetadata;
        Bundle extras = metadata.extras;
        if (extras != null) {
            int id = extras.getInt("id");
            Log.d("PlaybackService", "id:" + id);
            if (id == -1) {
                return;
            }
            Call<MusicFully> call = syService.getInfo(id);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<MusicFully> call, Response<MusicFully> response) {
                    if (response.isSuccessful()) {
                        MusicFully musicFully = response.body();
                        if (musicFully != null) {
                            if (musicFully.isSuccessful()) {
                                MediaMetadata mediaMetadata = mediaMetadataBuilder
                                        .setArtist(musicFully.getArtist())
                                        .setTitle(musicFully.getName())
                                        .setArtworkUri(Uri.parse(musicFully.getPic()))
                                        .build();
                                MediaItem newMediaItem = mediaItemBuilder.setMediaId(mediaItem.mediaId)
                                        .setUri(musicFully.getUrl())
                                        .setMediaMetadata(mediaMetadata).build();

                                Log.d("playbackservice", "newMediaItem:");
                                int curr = player.getCurrentMediaItemIndex();
                                MusicService.mediaItemArrayList.set(curr, newMediaItem);
                                player.setMediaItems(MusicService.mediaItemArrayList, false);
                                //player.seekTo(curr,0);
                                player.play();

                            }
                        }
                    } else {
                        MainApplication.toast("服务器出错");
                    }
                }

                @Override
                public void onFailure(Call<MusicFully> call, Throwable throwable) {
                    MainApplication.toast("网络出错");
                }
            });
            return;
        }
        Log.d("PlaybackService", mediaItem.mediaMetadata.title + " " + mediaItem.mediaMetadata.artist);
        Player.Listener.super.onMediaItemTransition(mediaItem, reason);
    }

    @Nullable
    @Override
    public MediaSession onGetSession(@NonNull MediaSession.ControllerInfo controllerInfo) {
        return mediaSession;
    }

    @Override
    public void onDestroy() {
        mediaSession.getPlayer().release();
        mediaSession.release();
        mediaSession = null;
        super.onDestroy();
    }
}