package com.sayi.vdim.activities.fragments;

import android.os.*;
import android.view.*;

import androidx.annotation.*;
import androidx.fragment.app.*;

import com.sayi.vdim.databinding.*;

public class RegisterEmailFragment extends Fragment {
    RegisterEmailBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=RegisterEmailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}
