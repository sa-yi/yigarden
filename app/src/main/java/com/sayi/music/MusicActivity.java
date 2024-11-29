package com.sayi.music;

import static android.view.KeyEvent.KEYCODE_BACK;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;

import com.sayi.MainApplication;
import com.sayi.vdim.R;
import com.sayi.vdim.databinding.ActivityMusicBinding;
import com.sayi.vdim.utils.DarkModeUtils;
import com.sayi.vdim.utils.Ticker;

public class MusicActivity extends AppCompatActivity implements Player.Listener, Ticker.OnTickListener {
    Ticker ticker;
    ActivityMusicBinding binding;
    MusicService.MusicBinder binder;
    boolean isSeekBarTracking = false;
    boolean isLrcViewScrolled = false;
    int lrcViewScrolledDelayedCount = 0;
    ActivityResultLauncher<Intent> resultLauncher;
    boolean isInited = false;

    boolean showLyrics=false;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MusicService.MusicBinder) service;
            binder.addListener(MusicActivity.this);

            ticker = new Ticker();
            ticker.addOnTickListener(MusicActivity.this);
            ticker.setInterval(1000);
            ticker.start();

            onPlaybackStateChanged(Player.STATE_READY);

            setupOnclickListener();
            isInited = true;
            if (binder.isLyricsShown()) {
                binder.hideLyrics();
                binding.showLyricsBtn.setTextColor(0xff66ccff);
            } else {
                binding.showLyricsBtn.setTextColor(0xff757575);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            binder.removeListener(MusicActivity.this);
        }
    };

    @Override
    public void onTick() {
        binding.current.setText(binder.getFormattedCurrentPosition());
        if (lrcViewScrolledDelayedCount > 0) lrcViewScrolledDelayedCount--;
        else binding.lrcview.smoothScrollToTime((long) binder.getCurrentPosition());
        if (!isSeekBarTracking) binding.seek.setProgress((int) binder.getCurrentPosition());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//实现状态栏图标和文字颜色为亮色

        Window window = getWindow();
        if (DarkModeUtils.isDarkMode(this)) {
            //黑色状态栏文本
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d("result", result.toString());
            binder.initWindow();
        });


        binding = ActivityMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);

        binding.lrcview.getLrcSetting().setMessageColor(0xff66ccff);
        binding.lrcview.getLrcSetting().setHeightRowColor(0xff66ccff);
        binding.lrcview.getLrcSetting().setNormalRowColor(0xff757575);
        binding.lrcview.commitLrcSettings();
        binding.lrcview.setLrcViewSeekListener((currentLrcRow, CurrentSelectedRowTime) -> {
            isLrcViewScrolled = true;
            lrcViewScrolledDelayedCount = 2;
            Log.d("lrcViewScrollChange", "scrolled");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInited)
            binder.hideLyrics();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isInited)
            if(showLyrics)
                binder.showLyrics();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK) {
            if (showLyrics)
                binder.showLyrics();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(binder!=null){
            if(binder.getIfShouldShowLyrics())
                binder.showLyrics();
        }
    }

    void setupOnclickListener() {
        binding.playOrPause.setOnClickListener(v -> {
            binder.playOrPause();
        });
        binding.previous.setOnClickListener(v -> {
            binder.skipToPrevious();
        });
        binding.next.setOnClickListener(v -> {
            binder.skipToNext();
        });
        binding.seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    binder.seekToPosition(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekBarTracking = false;
                binder.seekToPosition(binding.seek.getProgress());
            }
        });
        binding.back.setOnClickListener(v -> finish());
        binding.showLyricsBtn.setOnClickListener(v -> {
            if (Settings.canDrawOverlays(this)) {
                boolean isLrcShown = binder.getIfShouldShowLyrics();
                if (isLrcShown) {
                    binder.ifShouldShowLyrics(false);
                    binding.showLyricsBtn.setTextColor(0xff757575);
                } else {
                    binder.ifShouldShowLyrics(true);
                    binding.showLyricsBtn.setTextColor(0xff66ccff);
                    MainApplication.toast("悬浮歌词已开启，将在此界面外显示");
                }
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                resultLauncher.launch(intent);
            }
        });
    }

    @Override
    public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
        Player.Listener.super.onMediaItemTransition(mediaItem, reason);
        if (mediaItem == null) return;
        update();
    }

    @Override
    public void onPlaybackStateChanged(int playbackState) {
        if (playbackState == Player.STATE_READY) {
            binding.playOrPause.setImageResource(binder.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);
            binding.duration.setText(binder.getFormattedDuration());
            binding.seek.setMax((int) binder.getDuration());
            update();
        }
    }

    void update() {
        binding.lrcview.setLrcData(binder.getLyrics());
        binding.title.setText(binder.getTitle());
        binding.author.setText(binder.getAuthor());
        Bitmap bitmap = binder.getAlbumCover();
        if (bitmap != null)
            binding.cover.setImageBitmap(bitmap);
    }

    @Override
    public void onIsPlayingChanged(boolean isPlaying) {
        Player.Listener.super.onIsPlayingChanged(isPlaying);
        binding.playOrPause.setImageResource(binder.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    @Override
    public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
        Player.Listener.super.onPlayWhenReadyChanged(playWhenReady, reason);
    }
}
