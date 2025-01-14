package com.sayi.vdim.activities.mainfragments.user;

import static android.content.Context.*;
import static com.sayi.vdim.Consts.*;

import android.Manifest;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;

import androidx.activity.result.*;
import androidx.activity.result.contract.*;
import androidx.annotation.*;
import androidx.appcompat.app.*;
import androidx.core.app.*;
import androidx.core.content.*;
import androidx.core.view.*;
import androidx.fragment.app.*;
import androidx.lifecycle.*;

import com.bumptech.glide.*;
import com.sayi.*;
import com.sayi.vdim.R;
import com.sayi.vdim.activities.*;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.dz_entity.*;
import com.sayi.vdim.utils.*;
import com.yxing.*;
import com.yxing.def.*;

import java.text.*;
import java.util.*;

public class UserFragment extends Fragment {

    static int requestCameraPermission = 1;
    ActivityResultLauncher<String> callPermissionRequest;

    boolean granted = false;
    private FragmentUserBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestPermission(), _granted -> {
            granted = _granted;
            if (granted)
                ScanCodeConfig.create(requireActivity(), UserFragment.this).setStyle(ScanStyle.NONE)
                        .setPlayAudio(true)
                        .buidler()
                        .start(ScanCodeActivity.class);
        });
    }

    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        binding.statusbarPlaceholder.setHeight(Statusbar.getStatusBHeight(requireActivity()));
        binding.statusbarPlaceholder.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.default_background));

        View root = binding.getRoot();

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        requireActivity().setTitle("");
        setupMenu();

        userViewModel.getUserData().observe(getViewLifecycleOwner(), userEntity -> {
            DzUser user = userEntity.getSpace();
            Log.d("User", user.toString());
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
            Intent intent = new Intent(getContext(), AccountActivity.class);
            startActivity(intent);
        });

        binding.settings.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });
        binding.exit.setOnClickListener(v -> {
            requireActivity().finish();
        });

        return root;
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.user_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.scan) {//TODO 申请相机权限
                    if (isCameraPermissionGranted()) {
                        ScanCodeConfig.create(requireActivity(), UserFragment.this).setStyle(ScanStyle.NONE)
                                .setPlayAudio(true)
                                .buidler()
                                .start(ScanCodeActivity.class);
                    } else {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, requestCameraPermission);
                    }
                    return true;
                } else if (id == R.id.settings) {
                    Intent intent = new Intent(requireActivity(), SettingsActivity.class);
                    startActivity(intent);
                    return true;
                }
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
        } else if (requestCode == ScanCodeConfig.QUESTCODE) {
            if (data == null) return;
            Bundle extras = data.getExtras();
            if (extras != null) {//TODO 解析二维码
                int codeType = extras.getInt(ScanCodeConfig.CODE_TYPE);//0为一维码，其它为二维码
                String code = extras.getString(ScanCodeConfig.CODE_KEY);
                Uri uri = Uri.parse(code);
                String scheme = uri.getScheme();//vdim,http,htps
                String authority = uri.getAuthority();//i.lty.fan
                String path = uri.getPath();//viewthread
                String query = uri.getQuery();//tid=679
                Set<String> parameter = uri.getQueryParameterNames();//?
                String paras = "";
                for (String para : parameter) {
                    paras += "," + para;
                }
                Log.d("qrcode", "scheme:" + scheme + ",authority:" + authority + ",parameter:" + paras + ",query:" + query + ",path=" + path);
                if (scheme == null) {
                    return;
                }
                if (authority == null) {
                    return;
                }
                if (scheme.equals("vdim")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return;
                } else if (scheme.equals("http") || scheme.equals("https")) {
                    if (authority.equals("i.lty.fan")) {
                        if (path.equals("/forum.php")) {
                            String mod = uri.getQueryParameter("mod");
                            if (mod != null) {
                                if (mod.equals("viewthread")) {
                                    Log.d("thread", query);
                                    String tidStr = uri.getQueryParameter("tid");
                                    Log.d("tid", tidStr);
                                    int tid = Integer.parseInt(tidStr);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vdim://i.lty.fan/viewthread?tid=" + tid));
                                    startActivity(intent);
                                } else if (mod.equals("forumdisplay")) {
                                    String fidStr = uri.getQueryParameter("fid");
                                    Log.d(mod + ":fid", fidStr);
                                    int fid = Integer.parseInt(fidStr);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vdim://i.lty.fan/forum?fid=" + fid));
                                    startActivity(intent);
                                }
                            }
                        }
                        return;
                    }
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}