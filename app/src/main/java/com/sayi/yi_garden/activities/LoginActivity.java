package com.sayi.yi_garden.activities;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sayi.yi_garden.R;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    TextView jump_to_rig,retrive_password;
    Button login;
    EditText usernameV, passwdV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        jump_to_rig = findViewById(R.id.jump_to_rig);

        login = findViewById(R.id.login);
        usernameV = findViewById(R.id.username);
        passwdV = findViewById(R.id.password);
        retrive_password=findViewById(R.id.retrieve_password);


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
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

            // 创建请求
            String url = "http://127.0.0.1:5000/login"; // 本地服务器地址
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                            response -> {
                                // 登录成功
                                Toast.makeText(LoginActivity.this, "Login Successful:" + response.toString(), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            },
                            error -> {
                                // 登录失败
                                Toast.makeText(LoginActivity.this, "Login Failed" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(this,MainActivity.class);
                                startActivity(intent);
                                finish();
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
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

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
        retrive_password.setOnClickListener(v->{

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
