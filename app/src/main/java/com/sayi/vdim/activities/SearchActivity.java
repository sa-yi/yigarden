package com.sayi.vdim.activities;

import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;

import androidx.appcompat.app.*;

import com.sayi.*;
import com.sayi.vdim.databinding.*;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.searchInput.setOnEditorActionListener((v, actionId, keyEvent) -> {
            if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                String keyword = v.getText().toString().trim();
                if(!keyword.isEmpty()){
                    search(keyword);
                }
                return true;
            }
            return false;
        });
        binding.back.setOnClickListener(v->finish());
    }
    private void search(String keyword){
        if(keyword.startsWith("tid")){
            String tid=keyword.replace("tid","");
            Intent intent = new Intent(this, PostActivity.class);
            Uri uri = new Uri.Builder().scheme("vdim")
                    .authority("")  // authority 这里可以为空
                    .path("/viewpost")
                    .appendQueryParameter("id", tid)
                    .build();
            intent.setData(uri);
            startActivity(intent);
            finish();
            return;
        }
        MainApplication.toast("搜索功能开发中...");
    }
}