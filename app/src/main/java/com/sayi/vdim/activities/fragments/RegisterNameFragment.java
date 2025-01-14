package com.sayi.vdim.activities.fragments;

import android.os.*;
import android.text.*;
import android.view.*;

import androidx.annotation.*;
import androidx.fragment.app.*;

import com.sayi.vdim.databinding.*;
import com.sayi.vdim.utils.*;

public class RegisterNameFragment extends Fragment {
    RegisterUsernameBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RegisterUsernameBinding.inflate(getLayoutInflater());
        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int strength = PasswordStrengthChecker.checkPasswordStrength(s.toString());
                binding.passStrength.setProgress(strength, true);

                String stringText;
                int color;
                if (strength < 2) {
                    color = 0xffee0000;
                    stringText="弱";
                } else if (strength == 5) {
                    color = 0xff66ccff;
                    stringText="强";
                } else {
                    color = 0xffeeeeee;
                    stringText="中";
                }
                binding.passStrength.setIndicatorColor(color);
                binding.passStrengthText.setText("密码强度："+stringText);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return binding.getRoot();
    }
}
