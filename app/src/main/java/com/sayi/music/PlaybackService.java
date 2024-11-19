package com.sayi.music;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

import java.util.List;

@UnstableApi
public class PlaybackService extends MediaSessionService {
    private MediaSession mediaSession = null;
    private MediaSessionCallback mediaSessionCallback;
    @OptIn(markerClass = UnstableApi.class) @Override
    public void onCreate() {
        super.onCreate();
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        mediaSessionCallback=new MediaSessionCallback();
        mediaSession = new MediaSession.Builder(this, player).setCallback(mediaSessionCallback).build();

        Intent intent=new Intent(this,MusicActivity.class);
        mediaSession.setSessionActivity(PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE));
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
    @UnstableApi public class MediaSessionCallback implements MediaSession.Callback {
        @NonNull
        @Override
        public ListenableFuture<MediaSession.MediaItemsWithStartPosition> onPlaybackResumption(@NonNull MediaSession mediaSession, @NonNull MediaSession.ControllerInfo controller) {

            SettableFuture<MediaSession.MediaItemsWithStartPosition> settableFuture = SettableFuture.create();
            settableFuture.addListener(() -> {
                // Your app is responsible for storing the playlist and the start position
                // to use here
                MediaSession.MediaItemsWithStartPosition resumptionPlaylist = restorePlaylist();
                settableFuture.set(resumptionPlaylist);
            }, MoreExecutors.directExecutor());
            return settableFuture;
        }
    }
    @OptIn(markerClass = UnstableApi.class)
    private MediaSession.MediaItemsWithStartPosition restorePlaylist() {
        return new MediaSession.MediaItemsWithStartPosition(MusicScanner.mediaItems, 0, 0);
    }
}