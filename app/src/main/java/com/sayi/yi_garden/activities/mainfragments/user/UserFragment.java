package com.sayi.yi_garden.activities.mainfragments.user;

import static android.content.Context.MODE_PRIVATE;
import static com.sayi.yi_garden.Consts.sp_token;
import static com.sayi.yi_garden.Consts.sp_user_data;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sayi.yi_garden.activities.LoginActivity;
import com.sayi.yi_garden.activities.SettingsActivity;
import com.sayi.yi_garden.databinding.FragmentUserBinding;
import com.sayi.yi_garden.entity.User;

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
        userViewModel.fetchUserData(0);


        binding.logout.setOnClickListener(v->{
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(sp_user_data, MODE_PRIVATE);
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