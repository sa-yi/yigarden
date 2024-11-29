package com.sayi.music.fragments;

import android.os.*;
import android.view.*;

import androidx.annotation.*;
import androidx.fragment.app.*;

import com.sayi.vdim.databinding.*;

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
