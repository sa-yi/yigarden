package com.sayi.yi_garden.activities;

import static com.sayi.yi_garden.Consts.sp_token;
import static com.sayi.yi_garden.Consts.sp_user_data;
import static com.sayi.yi_garden.Consts.sp_user_id;
import static com.sayi.yi_garden.Consts.sp_user_name;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.HurlStack;
import com.sayi.MainApplication;
import com.sayi.yi_garden.R;
import com.sayi.yi_garden.entity.ApiClient;
import com.sayi.yi_garden.entity.ApiService;
import com.sayi.yi_garden.entity.JwtToken;
import com.sayi.yi_garden.entity.User;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    TextView jump_to_rig, retrieve_password;
    Button login;
    EditText usernameV, passwdV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Window window = getWindow();
        //如果之前是办透明模式，要加这一句需要取消半透明的Flag
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);
        window.setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_login);
        jump_to_rig = findViewById(R.id.jump_to_rig);

        login = findViewById(R.id.login);
        usernameV = findViewById(R.id.username);
        passwdV = findViewById(R.id.password);
        retrieve_password = findViewById(R.id.retrieve_password);

        login.setEnabled(false);
        usernameV.addTextChangedListener(new InputTextWatcher(usernameV));
        passwdV.addTextChangedListener(new InputTextWatcher(passwdV));

        login.setOnClickListener(v -> {
            ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
            String username = usernameV.getText().toString();
            String password = passwdV.getText().toString();
            // 调用接口
            Call<JwtToken> call = apiService.login(username, password);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                    if (response.isSuccessful()) {
                        String jwt_token = response.body().getJwtToken();
                        // 登录成功
                        Log.d("LoginActivity", jwt_token);

                        SharedPreferences sharedPreferences = getSharedPreferences(sp_user_data, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(sp_token, jwt_token);
                        editor.apply();

                        Call<User> userCall=apiService.getMe();
                        userCall.enqueue(new Callback<>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if(response.isSuccessful()){
                                    int uid=response.body().getId();
                                    String name=response.body().getName();

                                    editor.putInt(sp_user_id,uid);
                                    editor.putString(sp_user_name,name);
                                    editor.apply();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Log.e("Logging",response.errorBody().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable throwable) {
                                Log.e("Logging",throwable.toString());
                            }
                        });
                    } else {
                        Log.d("LoginActivity", response.toString());
                        MainApplication.toast("登录失败");
                    }
                }

                @Override
                public void onFailure(Call<JwtToken> call, Throwable throwable) {
                    if (throwable instanceof ConnectException) {
                        Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                    // 登录失败
                    Toast.makeText(LoginActivity.this, "Login Failed" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", throwable.getMessage() + "");
                    //finish();
                    Log.e("LoginActivity", throwable.toString() + "");
                }
            });
        });
        jump_to_rig.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("是否跳转至网站主页");
            builder.setPositiveButton("是", (dialog, which) -> {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://sa-yi.cn/");
                intent.setData(content_url);
                startActivity(intent);
            }).setNeutralButton("复制网站链接", (dialog, which) -> {
                String url = "http://sa-yi.cn/";
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("website", url);
                clipboard.setPrimaryClip(clip);
            }).setMessage("http://sa-yi.cn/");
            AlertDialog dialog = builder.create();
            dialog.show();


        });
        retrieve_password.setOnClickListener(v -> {
            MainApplication.toast("请前往网页端重置密码");
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class AllTrustManager implements X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public static class AllTrustHurlStack extends HurlStack {
        private final SSLSocketFactory sslSocketFactory;

        public AllTrustHurlStack() {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new AllTrustManager()}, null);
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected HttpURLConnection createConnection(URL url) throws IOException {
            HttpURLConnection httpURLConnection = super.createConnection(url);
            if (httpURLConnection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sslSocketFactory);
            }
            return httpURLConnection;
        }
    }

    class InputTextWatcher implements TextWatcher {
        private ArrayList<EditText> editTexts=new ArrayList<>();

        public InputTextWatcher(EditText editText) {
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
            boolean enabled=true;
            for(EditText editText:editTexts){
                if(editText.getText().toString().isEmpty())
                    enabled=false;
            }
            login.setEnabled(enabled);
        }
    }
}
