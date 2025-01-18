package com.sayi.vdim.activities.mainfragments.user;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import com.sayi.MainApplication;
import com.sayi.vdim.R;
import com.sayi.vdim.activities.*;
import com.sayi.vdim.databinding.FragmentUserBinding;
import com.yxing.ScanCodeActivity;
import com.yxing.ScanCodeConfig;
import com.yxing.def.ScanStyle;

import java.util.Set;

public class UserFragment extends Fragment {
    static int requestCameraPermission = 1;
    ActivityResultLauncher<String> callPermissionRequest;

    boolean granted = false;
    FragmentUserBinding binding;


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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragmentUserBinding.inflate(getLayoutInflater());

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        requireActivity().setTitle("");
        setupMenu();

        binding.exit.setOnClickListener(v->{
            requireActivity().finish();
        });
        binding.editProfile.setOnClickListener(v->{
            MainApplication.toast("编辑资料");
        });

        return binding.getRoot();
    }
    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
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
