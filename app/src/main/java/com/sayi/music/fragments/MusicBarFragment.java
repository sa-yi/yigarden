package com.sayi.music.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sayi.music.MusicActivity;
import com.sayi.yi_garden.databinding.MusicBarBinding;

public class MusicBarFragment extends Fragment {
    MusicBarBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MusicBarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        /*root.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MusicActivity.class);
            startActivity(intent);
        });*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
