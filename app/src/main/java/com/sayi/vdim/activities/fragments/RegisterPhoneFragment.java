package com.sayi.vdim.activities.fragments;

import android.os.*;
import android.view.*;

import androidx.annotation.*;
import androidx.fragment.app.*;

import com.sayi.vdim.databinding.*;

public class RegisterPhoneFragment extends Fragment {
    RegisterPhoneBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=RegisterPhoneBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}
