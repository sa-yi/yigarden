package com.sayi.music;

import android.app.Service;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.*;
import androidx.media3.session.MediaController;
import androidx.media3.session.*;
import androidx.preference.*;

import com.bumptech.glide.*;
import com.bumptech.glide.request.target.*;
import com.bumptech.glide.request.transition.*;
import com.google.common.util.concurrent.*;
import com.hw.lrcviewlib.LrcRow;
import com.hw.lrcviewlib.*;
import com.sayi.*;
import com.sayi.music.util.lrcparser.*;
import com.sayi.vdim.sayi_music_entity.*;
import com.sayi.vdim.utils.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import retrofit2.*;

public class MusicService extends Service implements Player.Listener {
    static String TAG = "MusicService";
    Ticker ticker;
    ArrayList<Player.Listener> listeners = new ArrayList<>();
    WindowManager windowManager;
    WindowManager.LayoutParams params;
    TextView lrcView;
    boolean isInited = false;
    ListenableFuture<MediaController> controllerFuture;
    SyService syService;
    boolean loadFromNetwork = false;
    ArrayList<MediaItem> mediaItemArrayList = new ArrayList<>();
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
        controllerFuture = new MediaController.Builder(this, sessionToken).buildAsync();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        loadFromNetwork = sharedPreferences.getBoolean("use_network", false);
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
        if (controller != null)
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
        boolean ifShouldShowLyrics = false;
        private boolean isLyricsShown = false;
        private LoadFinishedListener listener;

        public MusicBinder() {
            initWindow();
        }

        public void initWindow() {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);// 获取WindowManager
            params = new WindowManager.LayoutParams();// 创建布局参数
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;//这里需要进行不同的设置
            params.alpha = 1.0f;//设置透明度
            params.gravity = Gravity.TOP;//设置内部视图对齐方式
            params.rotationAnimation = WindowManager.LayoutParams.ROTATION_ANIMATION_JUMPCUT;
            params.x = 20;//窗口的右下角角坐标
            params.y = 20;
            params.format = PixelFormat.RGBA_8888;//是指定窗口的像素格式为 RGBA_8888。使用 RGBA_8888 像素格式的窗口可以在保持高质量图像的同时实现透明度效果。
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;//设置窗口的宽高,这里为自动
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //这段非常重要，是后续是否穿透点击的关键
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  //表示悬浮窗口不需要获取焦点，这样用户点击悬浮窗口以外的区域，就不需要关闭悬浮窗口。
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;//表示悬浮窗口不会阻塞事件传递，即用户点击悬浮窗口以外的区域时，事件会传递给后面的窗口处理。
            lrcView = new TextView(MusicService.this);
            lrcView.setTextColor(0xff66ccff);
            lrcView.setSingleLine();
            lrcView.setVisibility(View.GONE);
            if (Settings.canDrawOverlays(MusicService.this)) {
                windowManager.addView(lrcView, params);
            }
        }

        public void setOnLoadFinishedListener(LoadFinishedListener listener) {
            this.listener = listener;
        }

        public void setMediaItemArrayList(ArrayList<MediaItem> mediaItemArrayList) {
            MusicService.this.mediaItemArrayList = mediaItemArrayList;
            listener.onLoadFinished(mediaItemArrayList);

        }

        public void addListener(Player.Listener listener) {
            if (controller != null)
                controller.addListener(listener);
            else
                listeners.add(listener);
        }

        public void removeListener(Player.Listener listener) {
            if (controller == null) return;
            controller.removeListener(listener);
        }

        public void skipToPrevious() {
            if (controller == null) return;
            controller.seekToPrevious();
            controller.play();
        }

        public void skipToNext() {
            if (controller == null) return;
            controller.seekToNext();
            controller.play();
        }

        public void pause() {
            if (controller == null) return;
            controller.pause();
        }

        public void play() {
            if (controller == null) return;
            controller.play();
        }

        public void play(int index) {
            if (controller == null) return;
            controller.seekTo(index, 0);
            controller.play();
        }

        public void seekToPosition(long positionMs) {
            if (controller == null) return;
            controller.seekTo(positionMs);
        }

        public void playOrPause() {
            if (isPlaying()) pause();
            else play();
        }

        public boolean isPlaying() {
            if (controller == null) return false;
            return controller.isPlaying();
        }

        public float getCurrentPosition() {
            if (controller == null) return -1f;
            return controller.getCurrentPosition();
        }

        public float getDuration() {
            if (controller == null) return -1f;
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
            if (controller == null) return -1;
            return controller.getCurrentMediaItemIndex();
        }

        public Uri getMediaUri() {
            if (controller == null) return null;
            mediaItem = controller.getCurrentMediaItem();
            Uri mediaUri = mediaItem.localConfiguration.uri;
            return mediaUri;
        }

        public String getMediaPath() {
            return getMediaUri().getPath();
        }

        public List<LrcRow> getLyrics() {
            if (controller == null) return null;
            if (loadFromNetwork) {


                return null;
            }


            String mediaPath = getMediaPath();

            int dotIndex = mediaPath.lastIndexOf('.');
            String lyricPath = mediaPath.substring(0, dotIndex) + ".lrc";
            File lyricFile = new File(lyricPath);

            if (lyricFile.exists()) {
                Log.d("lyricPath", lyricPath);
                return new LrcDataBuilder().Build(lyricFile);
            } else {
                Log.d("lyricPath", "not existed");
                return null;
            }
        }

        public String getTitle() {
            return (String) mediaItem.mediaMetadata.title;
        }

        public String getAuthor() {
            return (String) mediaItem.mediaMetadata.artist;
        }

        public void showLyrics() {
            lrcView.setVisibility(View.VISIBLE);
            isLyricsShown = true;
        }

        public void hideLyrics() {
            lrcView.setVisibility(View.GONE);
            isLyricsShown = false;
        }

        public void ifShouldShowLyrics(boolean ifShow) {
            isLyricsShown = ifShow;
            ifShouldShowLyrics = ifShow;
        }

        public boolean getIfShouldShowLyrics() {
            return ifShouldShowLyrics;
        }

        public boolean isLyricsShown() {
            return isLyricsShown;
        }

        public Bitmap getAlbumCover() {
            mediaItem = controller.getCurrentMediaItem();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        public Bitmap getArtWork() {//网络
            mediaItem = controller.getCurrentMediaItem();

            Bitmap bitmap;

            byte[] albumArt = mediaItem.mediaMetadata.artworkData;;
            if (albumArt != null) {
                // 将字节数组转换为Bitmap
                bitmap = BitmapFactory.decodeByteArray(albumArt, 0, albumArt.length);
                // 设置专辑封面给ImageView
                return bitmap;
            }
            return null;
        }

        public void fetchData(){
            if (loadFromNetwork) {
                syService = SyClient.getRetrofitInstance().create(SyService.class);
                Call<ArrayList<Music>> musicListCall = syService.getSongList();
                musicListCall.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<Music>> call, @NonNull Response<ArrayList<Music>> response) {
                        if (response.isSuccessful()) {
                            MediaItem.Builder mediaItemBuilder = new MediaItem.Builder();
                            MediaMetadata.Builder metaDataBuilder = new MediaMetadata.Builder();


                            controllerFuture.addListener(() -> {
                                // Call controllerFuture.get() to retrieve the MediaController.
                                // MediaController implements the Player interface, so it can be
                                // attached to the PlayerView UI component.
                                //playerView.setPlayer(controllerFuture.get());
                                try {
                                    controller = controllerFuture.get();
                                    controller.setMediaItems(mediaItemArrayList);
                                    controller.setShuffleModeEnabled(true);
                                    for (Player.Listener listener : listeners)
                                        controller.addListener(listener);
                                    controller.addListener(MusicService.this);
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                    Log.e("ControllerFeature", e.getMessage());
                                }
                            }, MoreExecutors.directExecutor());


                            ArrayList<Music> musicList = response.body();
                            if (musicList == null) return;


                            for (int i = 0; i < musicList.size(); i++) {
                                Music music = musicList.get(i);
                                Log.d("Music", music.getName());
                                Call<MusicFully> musicFullyCall = syService.getInfo(music.getId());
                                int finalI = i;


                                musicFullyCall.enqueue(new Callback<>() {
                                    @Override
                                    public void onResponse(@NonNull Call<MusicFully> call, @NonNull Response<MusicFully> response) {
                                        if (response.isSuccessful()) {
                                            MusicFully musicFully = response.body();
                                            if (musicFully == null) return;
                                            if (musicFully.getUrl() == null) return;
                                            Log.d("MusicUrl", musicFully.getUrl());


                                            MediaMetadata metadata = metaDataBuilder
                                                    .setTitle(musicFully.getName())
                                                    .setArtist(musicFully.getArtist())
                                                    .build();
                                            MediaItem mediaItem = mediaItemBuilder
                                                    .setUri(musicFully.getUrl())
                                                    .setMediaMetadata(metadata)
                                                    .build();
                                            mediaItemArrayList.add(mediaItem);
                                            if (finalI == musicList.size() - 1) {
                                                controller.setMediaItems(mediaItemArrayList);
                                                binder.setMediaItemArrayList(mediaItemArrayList);
                                                Log.d("MusicUrl", "finished");
                                                //controller.prepare();
                                                //controller.play();
                                            }
                                            Glide.with(MusicService.this).asBitmap().load(musicFully.getPic()).into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                    resource.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                    byte[] bitmapData = stream.toByteArray();

                                                    MediaMetadata metadata = metaDataBuilder
                                                            .setTitle(musicFully.getName())
                                                            .setArtist(musicFully.getArtist())
                                                            .setArtworkData(bitmapData, MediaMetadata.PICTURE_TYPE_FRONT_COVER)
                                                            .build();
                                                    MediaItem mediaItem = mediaItemBuilder
                                                            .setUri(musicFully.getUrl())
                                                            .setMediaMetadata(metadata)
                                                            .build();
                                                    controller.replaceMediaItem(finalI,mediaItem);
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                                }
                                            });
                                        } else {
                                            Log.e("MusicUrl", music.getName());
                                        }
                                    }
                                    @Override
                                    public void onFailure(@NonNull Call<MusicFully> call, @NonNull Throwable throwable) {
                                        Log.e("Music", "failed:" + throwable.getMessage());
                                    }
                                });
                            }
                        } else {
                            MainApplication.toast("获取在线歌单失败，正在加载本地歌单");
                            fetchLocalData();
                            Log.e(TAG, "failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Music>> call, Throwable throwable) {
                        MainApplication.toast("获取在线歌单失败，正在加载本地歌单");
                        fetchLocalData();
                        Log.e(TAG, "error");
                    }
                });
            } else {
                fetchLocalData();
            }
        }

        public void fetchSingleData(Music music) {

            Call<MusicFully> musicFullyCall = syService.getInfo(music.getId());


            musicFullyCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<MusicFully> call, @NonNull Response<MusicFully> response) {
                    if (response.isSuccessful()) {
                        MusicFully musicFully = response.body();
                        if (musicFully == null) return;
                        if (musicFully.getUrl() == null) return;
                        Log.d("MusicUrl", musicFully.getUrl());


                    }
                }

                @Override
                public void onFailure(Call<MusicFully> call, Throwable throwable) {

                }
            });
        }


        public void fetchLocalData() {
            MusicScanner.scanLocalMedia(MusicService.this, mediaItems -> {
                controllerFuture.addListener(() -> {
                    // Call controllerFuture.get() to retrieve the MediaController.
                    // MediaController implements the Player interface, so it can be
                    // attached to the PlayerView UI component.
                    //playerView.setPlayer(controllerFuture.get());
                    try {
                        controller = controllerFuture.get();
                        controller.setMediaItems(mediaItems);
                        binder.setMediaItemArrayList(mediaItems);
                        controller.setShuffleModeEnabled(true);
                        for (Player.Listener listener : listeners)
                            controller.addListener(listener);
                        controller.addListener(new Player.Listener() {
                            @Override
                            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                                Player.Listener.super.onMediaItemTransition(mediaItem, reason);
                            }
                        });
                    } catch (ExecutionException | InterruptedException e) {
                        Log.e("ControllerFeature", Objects.requireNonNull(e.getMessage()));
                    }
                }, MoreExecutors.directExecutor());
            });
        }
        public interface LoadFinishedListener {
            void onLoadFinished(ArrayList<MediaItem> mediaItems);
        }
    }

    @Override
    public void onPlayerError(PlaybackException error) {
        Player.Listener.super.onPlayerError(error);
        MainApplication.toast("播放失败");
    }
}
