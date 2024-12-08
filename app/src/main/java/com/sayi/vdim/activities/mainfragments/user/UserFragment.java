package com.sayi.vdim.activities.mainfragments.user;

import static android.content.Context.*;
import static com.sayi.vdim.Consts.*;

import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;
import androidx.core.content.*;
import androidx.core.view.*;
import androidx.fragment.app.*;
import androidx.lifecycle.*;

import com.bumptech.glide.*;
import com.sayi.*;
import com.sayi.vdim.*;
import com.sayi.vdim.R;
import com.sayi.vdim.activities.*;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.dz_entity.*;
import com.sayi.vdim.utils.*;

import java.text.*;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        binding.statusbarPlaceholder.setHeight(Statusbar.getStatusBHeight(requireActivity()));
        binding.statusbarPlaceholder.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.default_background));

        View root = binding.getRoot();

        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.toolbar);
        requireActivity().setTitle("");
        setupMenu();

        userViewModel.getUserData().observe(getViewLifecycleOwner(), userEntity -> {
            DzUser user=userEntity.getSpace();
            Log.d("User",user.toString());
            int id = user.getUid();
            binding.userId.setText(MessageFormat.format("{0}", id));
            String name = user.getUsername();
            binding.displayName.setText(name);
            Glide.with(UserFragment.this).load("https://i.lty.fan/uc_server/avatar.php?size=big&uid=" + id).into(binding.avatorImage);
        });

        userViewModel.fetchUserData();

        binding.logout.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(sp_user_data, MODE_PRIVATE);
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

    private void setupMenu(){
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.user_menu,menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
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