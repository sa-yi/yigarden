package com.sayi.vdim.activities;

import android.annotation.*;
import android.content.ClipboardManager;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.*;

import com.android.volley.toolbox.*;
import com.google.gson.internal.*;
import com.sayi.*;
import com.sayi.vdim.*;
import com.sayi.vdim.customentity.*;
import com.sayi.vdim.databinding.*;

import java.io.*;
import java.net.*;
import java.security.cert.*;
import java.util.*;

import javax.net.ssl.*;

import okhttp3.*;
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


        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);
        window.setStatusBarColor(Color.TRANSPARENT);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.login.setEnabled(false);
        InputTextWatcher watcher=new InputTextWatcher();
        watcher.addEditText(binding.username);
        watcher.addEditText(binding.password);
        binding.username.addTextChangedListener(watcher);
        binding.password.addTextChangedListener(watcher);

        CustomService service = CustomClient.getRetrofitInstance().create(CustomService.class);
        binding.login.setOnClickListener(v -> {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();


            Call<CustomToken> call = service.login(username,password);
            call.enqueue(new Callback<>() {

                @Override
                public void onResponse(@NonNull Call<CustomToken> call, @NonNull Response<CustomToken> response) {
                    if (response.isSuccessful()) {
                        CustomToken tokenEntity = response.body();
                        if (tokenEntity != null) {
                            String token=tokenEntity.getToken();
                            Log.d("token", token);
                            if(!token.isEmpty()){
                                ((MainApplication)getApplication()).putToken(token);
                                MainApplication.toast("登录成功");
                                finish();
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }else {
                            Log.e("login","null token");
                        }
                    }else {
                        Log.e("login",response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CustomToken> call, @NonNull Throwable e) {
                    Log.e("login",e.getMessage());
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
        binding.webLogin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, WebLoginActivity.class);
            startActivity(intent);
            finish();
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
        private ArrayList<EditText> editTexts = new ArrayList<>();

        public void addEditText(EditText editText){
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
                Log.d("textWatcher",editText.getText().toString());
                if (editText.getText().toString().isEmpty())
                    enabled = false;
            }
            binding.login.setEnabled(enabled);
        }
    }
}
