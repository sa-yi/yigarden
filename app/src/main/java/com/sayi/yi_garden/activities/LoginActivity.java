package com.sayi.yi_garden.activities;

import static com.sayi.yi_garden.Consts.sp_token;
import static com.sayi.yi_garden.Consts.sp_user_data;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sayi.yi_garden.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;


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
        window.setStatusBarColor(Color.TRANSPARENT);

        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);

        setContentView(R.layout.activity_login);
        jump_to_rig = findViewById(R.id.jump_to_rig);

        login = findViewById(R.id.login);
        usernameV = findViewById(R.id.username);
        passwdV = findViewById(R.id.password);
        retrieve_password =findViewById(R.id.retrieve_password);


        login.setOnClickListener(v -> {
            String username = usernameV.getText().toString();
            String password = passwdV.getText().toString();

            // 创建JSON对象
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
                jsonObject.put("password", password);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 创建请求队列
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this, new AllTrustHurlStack());

            // 创建请求
            String url = "http://118.25.55.56/wp-json/api/v1/token"; // 本地服务器地址
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                            response -> {
                                String jwt_token="";
                                try {
                                    jwt_token = (String) response.get("jwt_token");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                // 登录成功
                                Log.d("LoginActivity",jwt_token);

                                SharedPreferences sharedPreferences = getSharedPreferences(sp_user_data, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(sp_token, jwt_token);
                                editor.apply();



                                Toast.makeText(LoginActivity.this, "Login Successful:" + response, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            },
                            error -> {
                                // 登录失败
                                Toast.makeText(LoginActivity.this, "Login Failed" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("LoginActivity", error.getMessage()+"");
                                //finish();
                                Log.e("LoginActivity",error.toString()+"");
                            });

            // 添加请求到队列
            requestQueue.add(jsonObjectRequest);
        });
        jump_to_rig.setOnClickListener(v -> {
            String username = usernameV.getText().toString();
            String password = passwdV.getText().toString();

            // 创建JSON对象
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
                jsonObject.put("password", password);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 创建请求队列
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this, new AllTrustHurlStack());

            // 创建请求
            String url = "http://127.0.0.1:5000/register"; // 本地服务器地址
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                            response -> {
                                // 注册成功
                                Toast.makeText(LoginActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            },
                            error -> {
                                // 注册失败
                                Toast.makeText(LoginActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            });
            // 添加请求到队列
            requestQueue.add(jsonObjectRequest);
        });
        retrieve_password.setOnClickListener(v->{

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public static class AllTrustManager implements X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

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
}
