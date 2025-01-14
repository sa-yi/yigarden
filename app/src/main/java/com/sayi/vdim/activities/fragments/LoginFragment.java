package com.sayi.vdim.activities.fragments;

import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;

import androidx.annotation.*;
import androidx.fragment.app.*;

import com.sayi.*;
import com.sayi.vdim.activities.*;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.dz_entity.*;

import java.util.*;

import retrofit2.*;

public class LoginFragment extends Fragment {
    LoginFragmentBinding binding;
    DzService service = DzClient.getRetrofitInstance().create(DzService.class);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=LoginFragmentBinding.inflate(getLayoutInflater());

        binding.login.setOnClickListener(v->{
            String username = binding.account.getText().toString();
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
                                ((MainApplication) requireActivity().getApplication()).putToken(token);
                                MainApplication.toast("登录成功");
                                requireActivity().finish();
                                Intent intent = new Intent(requireActivity(), MainActivity.class);
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
        return binding.getRoot();
    }

}
