package com.sayi.vdim.activities.mainfragments.user;

import static android.content.Context.*;
import static com.sayi.vdim.Consts.*;

import android.content.*;
import android.os.*;
import android.view.*;

import androidx.annotation.*;
import androidx.fragment.app.*;
import androidx.lifecycle.*;

import com.sayi.*;
import com.sayi.vdim.activities.*;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.dz_entity.*;

import java.text.*;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userViewModel.getUserData().observe(getViewLifecycleOwner(), userEntity -> {
            DzUser.Space user=userEntity.getSpace();
            int id = user.getUid();
            binding.userId.setText(MessageFormat.format("{0}", id));
            String name = user.getUsername();
            binding.displayName.setText(name);
        });
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(sp_user_data, MODE_PRIVATE);
        String user_name = sharedPreferences.getString(sp_user_name, "");
        int uid = sharedPreferences.getInt(sp_user_id, -1);
        binding.userId.setText(uid + "");
        binding.displayName.setText(user_name);

        binding.logout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(sp_token, "");
            editor.apply();
            requireActivity().finish();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });

        binding.settings.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });

        /*binding.scan.setOnClickListener(v->{
            Intent intent=new Intent(getContext(), QRScanActivity.class);
            startActivityForResult(intent,1);
        });*/
        binding.exit.setOnClickListener(v->{
            requireActivity().finish();
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                String result = data.getStringExtra("data");
                MainApplication.toast(result);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}