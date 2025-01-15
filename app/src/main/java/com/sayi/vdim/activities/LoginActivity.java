package com.sayi.vdim.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sayi.MainApplication;

import com.sayi.vdim.Consts;
import com.sayi.vdim.databinding.ActivityLoginBinding;
import com.sayi.vdim.dz_entity.DzClient;
import com.sayi.vdim.dz_entity.DzService;


import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Window window = getWindow();
        //如果之前是办透明模式，要加这一句需要取消半透明的Flag
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        Log.d("pkg", MainApplication.getContext().getPackageName());


        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);
        window.setStatusBarColor(Color.TRANSPARENT);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.login.setEnabled(false);
        InputTextWatcher watcher = new InputTextWatcher();
        watcher.addEditText(binding.username);
        watcher.addEditText(binding.password);
        binding.username.addTextChangedListener(watcher);
        binding.password.addTextChangedListener(watcher);

        DzService service = DzClient.getRetrofitInstance().create(DzService.class);
        binding.login.setOnClickListener(v -> {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();


            Call<Map<String, Object>> call = service.login(username, password);
            call.enqueue(new Callback<>() {

                @Override
                public void onResponse(@NonNull Call<Map<String, Object>> call, @NonNull Response<Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        Map<String, Object> tokenEntity = response.body();
                        if (tokenEntity != null) {
                            String token = (String) tokenEntity.get("token");
                            Log.d("token", token);
                            if (!token.isEmpty()) {
                                ((MainApplication) getApplication()).putToken(token);
                                MainApplication.toast("登录成功");
                                finish();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Log.e("login", "null token");
                        }
                    } else {
                        Log.e("login", response.message());
                        switch (response.code()) {
                            case 400:
                                MainApplication.toast("账户或密码为空");
                                break;
                            case 401:
                                MainApplication.toast("账户密码输入错误");
                                break;
                            case 404:
                                MainApplication.toast("未找到账户");
                                break;
                            case 500:
                                MainApplication.toast("数据库连接错误");
                                break;
                            case 567:
                                MainApplication.toast("登录次数过多，请稍后重试");
                                break;
                            default:
                                MainApplication.toast("未知错误，错误码：" + response.code());

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Map<String, Object>> call, @NonNull Throwable e) {
                    Log.e("login", e.getMessage());
                }

            });
        });
        binding.jumpToRig.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("是否跳转至网站主页");
            builder.setPositiveButton("是", (dialog, which) -> {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(Consts.website_url);
                intent.setData(content_url);
                startActivity(intent);
            }).setNeutralButton("复制网站链接", (dialog, which) -> {
                String url = Consts.website_url;
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("website", url);
                clipboard.setPrimaryClip(clip);
            }).setMessage(Consts.website_url);
            AlertDialog dialog = builder.create();
            dialog.show();


        });
        binding.retrievePassword.setOnClickListener(v -> {
            MainApplication.toast("请前往网页端重置密码");
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class InputTextWatcher implements TextWatcher {
        private ArrayList<EditText> editTexts = new ArrayList<>();

        public void addEditText(EditText editText) {
            editTexts.add(editText);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean enabled = true;
            for (EditText editText : editTexts) {
                Log.d("textWatcher", editText.getText().toString());
                if (editText.getText().toString().isEmpty())
                    enabled = false;
            }
            binding.login.setEnabled(enabled);
        }
    }
}
