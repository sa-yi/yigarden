package com.sayi.music;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.hw.lrcviewlib.LrcDataBuilder;
import com.hw.lrcviewlib.LrcRow;
import com.sayi.music.util.lrcparser.LrcParser;
import com.sayi.yi_garden.utils.Ticker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MusicService extends Service implements Player.Listener {
    Ticker ticker;
    ArrayList<Player.Listener> listeners = new ArrayList<>();
    WindowManager windowManager;
    WindowManager.LayoutParams params;
    TextView lrcView;
    boolean isInited = false;
    private MusicBinder binder;
    private MediaController controller;

    public static String formatTime(float msec) {
        int minute = ((int) msec) / 1000 / 60;
        int second = ((int) msec) / 1000 % 60;
        String minuteString;
        String secondString;
        if (minute < 10) {
            minuteString = "0" + minute;
        } else {
            minuteString = "" + minute;
        }
        if (second < 10) {
            secondString = "0" + second;
        } else {
            secondString = "" + second;
        }
        return minuteString + ":" + secondString;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SessionToken sessionToken =
                new SessionToken(this, new ComponentName(this, PlaybackService.class));
        ListenableFuture<MediaController> controllerFuture =
                new MediaController.Builder(this, sessionToken).buildAsync();
        MusicScanner.scanLocalMedia(this, mediaItems -> {
            controllerFuture.addListener(() -> {
                // Call controllerFuture.get() to retrieve the MediaController.
                // MediaController implements the Player interface, so it can be
                // attached to the PlayerView UI component.
                //playerView.setPlayer(controllerFuture.get());
                try {
                    controller = controllerFuture.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    Log.e("ControllerFeature",e.getMessage());
                }
                controller.setMediaItems(mediaItems);
                controller.setShuffleModeEnabled(true);
                for (Player.Listener listener : listeners)
                    controller.addListener(listener);
                controller.addListener(new Player.Listener() {
                    @Override
                    public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                        Player.Listener.super.onMediaItemTransition(mediaItem, reason);
                    }
                });
            }, MoreExecutors.directExecutor());
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder = new MusicBinder();
        binder.addListener(this);
        ticker = new Ticker();
        ticker.addOnTickListener(() -> {
            if (lrcView != null)
                lrcView.setText(LrcParser.getLrc(binder.getCurrentPosition() / 1000).lrc);
        });
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isViewInOverlay(lrcView))
            windowManager.removeView(lrcView);
        controller.release();
    }

    public boolean isViewInOverlay(View view) {
        if (view == null) {
            return false;
        }

        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            // 检查布局参数的类型是否为 WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            // 或其他悬浮窗类型，例如 TYPE_PHONE、TYPE_SYSTEM_ALERT、TYPE_SYSTEM_OVERLAY 等
            return layoutParams.type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ||
                    layoutParams.type == WindowManager.LayoutParams.TYPE_PHONE ||
                    layoutParams.type == WindowManager.LayoutParams.TYPE_SYSTEM_ALERT ||
                    layoutParams.type == WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        }

        return false;
    }

    @Override
    public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
        Player.Listener.super.onMediaItemTransition(mediaItem, reason);
        if (mediaItem == null) return;
        if (!isInited) {
            ticker.start();
            isInited = true;
        }
        LrcParser.parse(binder.getMediaPath());
    }

    public class MusicBinder extends Binder {
        MediaItem mediaItem;
        boolean isLyricsShown = false;

        public MusicBinder() {
            initWindow();
        }


        public void initWindow() {
            // 获取WindowManager
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            // 创建布局参数
            params = new WindowManager.LayoutParams();
            //这里需要进行不同的设置
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            //设置透明度
            params.alpha = 1.0f;
            //设置内部视图对齐方式
            params.gravity = Gravity.TOP;
            params.rotationAnimation = WindowManager.LayoutParams.ROTATION_ANIMATION_JUMPCUT;
            //窗口的右下角角坐标
            params.x = 20;
            params.y = 20;
            //是指定窗口的像素格式为 RGBA_8888。
            //使用 RGBA_8888 像素格式的窗口可以在保持高质量图像的同时实现透明度效果。
            params.format = PixelFormat.RGBA_8888;
            //设置窗口的宽高,这里为自动
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //这段非常重要，是后续是否穿透点击的关键
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  //表示悬浮窗口不需要获取焦点，这样用户点击悬浮窗口以外的区域，就不需要关闭悬浮窗口。
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;//表示悬浮窗口不会阻塞事件传递，即用户点击悬浮窗口以外的区域时，事件会传递给后面的窗口处理。
            lrcView = new TextView(getApplicationContext());
            lrcView.setTextColor(0xff66ccff);
            lrcView.setSingleLine();
            lrcView.setText("");
            lrcView.setVisibility(View.GONE);
            if (Settings.canDrawOverlays(MusicService.this)) {
                windowManager.addView(lrcView, params);
            }
        }

        public void addListener(Player.Listener listener) {
            if (controller != null)
                controller.addListener(listener);
            else
                listeners.add(listener);
        }

        public void removeListener(Player.Listener listener) {
            controller.removeListener(listener);
        }

        public void skipToPrevious() {
            controller.seekToPrevious();
            controller.play();
        }

        public void skipToNext() {
            controller.seekToNext();
            controller.play();
        }

        public void pause() {
            controller.pause();
        }

        public void play() {
            controller.play();
        }

        public void play(int index) {
            controller.seekTo(index, 0);
            controller.play();
        }

        public void seekToPosition(long positionMs) {
            controller.seekTo(positionMs);
        }

        public void playOrPause() {
            if (isPlaying()) pause();
            else play();
        }

        public boolean isPlaying() {
            return controller.isPlaying();
        }

        public float getCurrentPosition() {
            return controller.getCurrentPosition();
        }

        public void setCurrentPosition(float currentPosition) {
            controller.seekTo((long) currentPosition);
        }

        public float getDuration() {
            return controller.getDuration();
        }

        public String getFormattedCurrentPosition() {
            float curr = getCurrentPosition();
            return formatTime(curr);
        }

        public String getFormattedDuration() {
            float dur = getDuration();
            Log.d("msec", dur + "");
            return formatTime(dur);
        }

        public int getCurrentMediaItemIndex() {
            return controller.getCurrentMediaItemIndex();
        }

        public Uri getMediaUri() {
            mediaItem = controller.getCurrentMediaItem();
            Uri mediaUri = mediaItem.localConfiguration.uri;
            return mediaUri;
        }

        public String getMediaPath() {
            return getMediaUri().getPath();
        }

        public List<LrcRow> getLyrics() {
            String mediaPath = getMediaPath();

            int dotIndex = mediaPath.lastIndexOf('.');
            String lyricPath = mediaPath.substring(0, dotIndex) + ".lrc";
            File lyricFile = new File(lyricPath);
            List<LrcRow> lrcRows = null;
            if (lyricFile.exists()) {
                Log.d("lyricPath", lyricPath);
                lrcRows = new LrcDataBuilder().Build(lyricFile);
            } else {
                Log.d("lyricPath", "not existed");
                return null;
            }
            return lrcRows;
        }

        public String getTitle() {
            return (String) mediaItem.mediaMetadata.title;
        }

        public String getAuthor() {
            return (String) mediaItem.mediaMetadata.artist;
        }

        public void showLyrics() {
            if (isLyricsShown)
                lrcView.setVisibility(View.VISIBLE);
        }

        public void hideLyrics() {
            lrcView.setVisibility(View.GONE);
        }

        public boolean showOrHideLyrics() {
            isLyricsShown = !isLyricsShown;
            return isLyricsShown;
        }

        public Bitmap getAlbumCover() {
            Bitmap bitmap = null;
            Uri artworkUri = mediaItem.mediaMetadata.artworkUri;
            Log.d("artworkUri", artworkUri + "");
            if (artworkUri == null)
                return null;
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(MusicService.this, artworkUri);
                byte[] albumArt = retriever.getEmbeddedPicture();
                if (albumArt != null) {
                    // 将字节数组转换为Bitmap
                    bitmap = BitmapFactory.decodeByteArray(albumArt, 0, albumArt.length);
                    // 设置专辑封面给ImageView
                    return bitmap;
                }
                retriever.release();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

}
