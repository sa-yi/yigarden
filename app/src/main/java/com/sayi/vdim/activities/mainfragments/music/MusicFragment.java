package com.sayi.vdim.activities.mainfragments.music;

import static android.content.Context.BIND_AUTO_CREATE;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sayi.MainApplication;
import com.sayi.music.MusicActivity;
import com.sayi.music.MusicService;
import com.sayi.vdim.R;
import com.sayi.vdim.databinding.FragmentMusicBinding;
import com.sayi.vdim.databinding.MusicBarBinding;
import com.sayi.vdim.utils.Dialog;
import com.sayi.vdim.utils.Statusbar;

import java.util.ArrayList;

public class MusicFragment extends Fragment implements Player.Listener {

    MusicService.MusicBinder binder;
    MusicAdapter adapter;
    MusicBarBinding barBinding;
    boolean isServiceConnected = false;
    boolean granted = false;
    ActivityResultLauncher<String> callPermissionRequest;
    int clickCount = 0;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MusicService.MusicBinder) service;
            binder.addListener(MusicFragment.this);


            binder.setOnLoadFinishedListener(mediaItems -> {
                adapter.setMediaItems(mediaItems);
            });
            binder.fetchData();
            isServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
    MusicViewModel musicViewModel;
    private FragmentMusicBinding binding;
    private Drawable playBtnDrawable;
    private Drawable pauseBtnDrawable;

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            callPermissionRequest.launch(Manifest.permission.READ_MEDIA_AUDIO);
        } else {
            callPermissionRequest.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private boolean checkPermission() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_AUDIO;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        return ContextCompat.checkSelfPermission(requireActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestPermission(), _granted -> {
            granted = _granted;
            if (granted) {
                Intent intent = new Intent(getActivity(), MusicService.class);
                requireActivity().bindService(intent, connection, BIND_AUTO_CREATE);
                binding.reqPermView.setVisibility(View.GONE);
            }
            //MainApplication.toast("requested");
        });

        adapter = new MusicAdapter();

        playBtnDrawable = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_play);
        pauseBtnDrawable = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_pause);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        musicViewModel = new ViewModelProvider(this).get(MusicViewModel.class);


        binding = FragmentMusicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //musicViewModel.fetchSongList();
        binding.statusbarPlaceholder.setHeight(Statusbar.getStatusBHeight(requireActivity()));
        binding.statusbarPlaceholder.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white_blue));
        binding.toolbarTitle.setText("音乐");
        binding.musicSettings.setOnClickListener(v -> {
            //TODO 音乐的设置界面
            /*Intent intent = new Intent(requireActivity(), MusicSettingsActivity.class);
            startActivity(intent);*/
            MainApplication.toast("开发中");
        });

        if (checkPermission()) {
            binding.reqPermView.setVisibility(View.GONE);
            requestPermission();
        }
        binding.reqMediaPerm.setOnClickListener(v -> {
            if (clickCount <= 3) {
                requestPermission();
                clickCount++;
            } else {
                Dialog.init(requireActivity()).setupDialog("申请权限失败", "是否跳转设置页面申请")
                        .setPositiveButton("确认", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.fromParts("package", requireActivity().getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //TODO:权限申请startActivityForResult
                            startActivity(intent);
                        })
                        .setNegativeButton("取消", null).show();
                clickCount = 0;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerview.setLayoutManager(linearLayoutManager);


        binding.recyclerview.setAdapter(adapter);

        barBinding = MusicBarBinding.bind(binding.getRoot().findViewById(R.id.homeControlWrapper));

        barBinding.getRoot().setOnClickListener(v -> {
            if (!granted) return;
            Intent intent = new Intent(requireActivity(), MusicActivity.class);
            startActivity(intent);
        });

        barBinding.homePlayPauseBtn.setOnClickListener(v -> {
            if (!granted) return;
            binder.playOrPause();
            barBinding.homePlayPauseBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    binder.isPlaying() ? pauseBtnDrawable : playBtnDrawable,
                    null, null, null);
        });
        barBinding.homeSkipNextBtn.setOnClickListener(v -> {
            if (!granted) return;
            binder.skipToNext();
        });
        barBinding.homeSkipPreviousBtn.setOnClickListener(v -> {
            if (!granted) return;
            binder.skipToPrevious();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (granted && isServiceConnected)
            requireActivity().unbindService(connection);
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = requireActivity().getWindow();
        /*如果之前是半透明模式，要加这一句需要取消半透明的Flag*/
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
        Player.Listener.super.onMediaItemTransition(mediaItem, reason);
        if (mediaItem == null) return;
        barBinding.homeSongNameView.setText(mediaItem.mediaMetadata.title);
        int currentIndex = binder.getCurrentMediaItemIndex();
        adapter.setCurrentPlayingIndex(currentIndex);
    }

    @Override
    public void onPlaybackStateChanged(int playbackState) {
        if (playbackState == Player.STATE_READY) {
            barBinding.homePlayPauseBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(binder.isPlaying() ? pauseBtnDrawable : playBtnDrawable, null, null, null);
        }
    }

    @Override
    public void onIsPlayingChanged(boolean isPlaying) {
        Player.Listener.super.onIsPlayingChanged(isPlaying);
        barBinding.homePlayPauseBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(binder.isPlaying() ? pauseBtnDrawable : playBtnDrawable, null, null, null);
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(requireActivity(), view);
        popupMenu.inflate(R.menu.item_menu);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.action_play_next) {
                MainApplication.toast("开发中");
                return true;
            } else if (itemId == R.id.action_play) {
                binder.play(position);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
        private int currentPlayingIndex = -1;
        private ArrayList<MediaItem> mediaItems = new ArrayList<>();

        public void setMediaItems(ArrayList<MediaItem> mediaItems) {
            this.mediaItems.clear();
            this.mediaItems.addAll(mediaItems);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.item_music, parent,
                    false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MediaItem mediaItem = mediaItems.get(position);
            holder.music_name.setText(mediaItem.mediaMetadata.title);
            holder.music_artist.setText(mediaItem.mediaMetadata.artist);
            holder.itemView.setOnClickListener(v -> {
                binder.play(position);
            });
            holder.more_action.setOnClickListener(v -> {
                showPopupMenu(holder.more_action, position);
            });
            if (position == currentPlayingIndex) {
                holder.music_name.setTextColor(requireContext().getColor(R.color.tianyi_blue));
            } else {
                holder.music_name.setTextColor(requireContext().getColor(R.color.day_black));
            }
        }

        @Override
        public int getItemCount() {
            return mediaItems.size();
        }

        public void setCurrentPlayingIndex(int index) {
            int previousIndex = currentPlayingIndex;
            currentPlayingIndex = index;
            notifyItemChanged(previousIndex);
            notifyItemChanged(currentPlayingIndex);
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView music_name;
            TextView music_artist;
            ImageView more_action;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                music_name = itemView.findViewById(R.id.music_name);
                music_artist = itemView.findViewById(R.id.music_artist);
                more_action = itemView.findViewById(R.id.more_action);
            }
        }
    }
}