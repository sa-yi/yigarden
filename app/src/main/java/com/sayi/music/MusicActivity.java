package com.sayi.music;

import static android.view.KeyEvent.*;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.activity.result.*;
import androidx.activity.result.contract.*;
import androidx.annotation.*;
import androidx.appcompat.app.*;
import androidx.media3.common.*;

import com.sayi.*;
import com.sayi.vdim.R;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.utils.*;

public class MusicActivity extends AppCompatActivity implements Player.Listener {
    Ticker ticker;
    ActivityMusicBinding binding;
    MusicService.MusicBinder binder;
    boolean isSeekBarTracking = false;
    boolean isLrcViewScrolled = false;
    int lrcViewScrolledDelayedCount = 0;
    ActivityResultLauncher<Intent> resultLauncher;
    boolean isInited = false;

    boolean showLyrics = false;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MusicService.MusicBinder) service;
            binder.addListener(MusicActivity.this);

            ticker = new Ticker();
            ticker.addOnTickListener(() -> {

                binding.current.setText(binder.getFormattedCurrentPosition());
                if (lrcViewScrolledDelayedCount > 0) lrcViewScrolledDelayedCount--;
                else binding.lrcview.smoothScrollToTime((long) binder.getCurrentPosition());
                if (!isSeekBarTracking) binding.seek.setProgress((int) binder.getCurrentPosition());
            });
            ticker.setInterval(1000);
            ticker.start();

            update();

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
            if (showLyrics)
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
        if (binder != null) {
            if (binder.getIfShouldShowLyrics())
                binder.showLyrics();
        }
    }

    void setupOnclickListener() {
        binding.cover.setOnClickListener(v -> {
            binding.cover.setVisibility(View.GONE);
        });
        binding.lrcview.setOnClickListener(v -> {
            binding.cover.setVisibility(View.VISIBLE);
        });

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
            update();
        }
    }

    void update() {
        Bitmap artWork = binder.getArtWork();
        if (artWork != null) {
            binding.cover.setImageBitmap(artWork);//网络
        }
        Bitmap bitmap = binder.getAlbumCover();
        if (bitmap != null)
            binding.cover.setImageBitmap(bitmap);//本地

        binding.lrcview.setLrcData(binder.getLyrics());
        binding.title.setText(binder.getTitle());
        binding.author.setText(binder.getAuthor());

        binding.seek.setMax((int) binder.getDuration());
        binding.duration.setText(binder.getFormattedDuration());
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
