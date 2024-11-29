package com.sayi.vdim.activities.mainfragments.user;

import static android.content.Context.MODE_PRIVATE;
import static com.sayi.vdim.Consts.sp_token;
import static com.sayi.vdim.Consts.sp_user_data;
import static com.sayi.vdim.Consts.sp_user_id;
import static com.sayi.vdim.Consts.sp_user_name;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sayi.vdim.activities.LoginActivity;
import com.sayi.vdim.activities.SettingsActivity;
import com.sayi.vdim.databinding.FragmentUserBinding;

import java.text.MessageFormat;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            int id=user.getId();
            binding.userId.setText(MessageFormat.format("{0}", id));
            String name=user.getName();
            binding.displayName.setText(name);
        });
        SharedPreferences sharedPreferences=requireActivity().getSharedPreferences(sp_user_data,MODE_PRIVATE);
        String user_name=sharedPreferences.getString(sp_user_name,"");
        int uid=sharedPreferences.getInt(sp_user_id,-1);
        binding.userId.setText(uid + "");
        binding.displayName.setText(user_name);

        binding.logout.setOnClickListener(v->{
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(sp_token,"");
            editor.apply();
            requireActivity().finish();
            Intent intent=new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });

        binding.settings.setOnClickListener(v->{
            Intent intent=new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}