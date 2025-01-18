package com.sayi.vdim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sayi.MainApplication;
import com.sayi.vdim.databinding.ActivitySettingsNewBinding;
import com.sayi.vdim.databinding.ItemSettingBlockBinding;
import com.sayi.vdim.databinding.ItemSettingSubItemBinding;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsNewBinding binding;

    private final ArrayList<SettingBlockEntity> settingBlockEntities = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        binding=ActivitySettingsNewBinding.inflate(inflater);
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v->{
            finish();
        });

        settingBlockEntities.add(new SettingBlockEntity("账户")
            .addSubEntity(new SettingEntity("编辑个人资料"))
            .addSubEntity(new SettingEntity("密码安全"))
            .addSubEntity(new SettingEntity("二维码名片"))
            .addSubEntity(new SettingEntity("隐私设置"))
        );
        settingBlockEntities.add(new SettingBlockEntity("通用")
            .addSubEntity(new SettingEntity("消息提醒"))
            .addSubEntity(new SettingEntity("风格配置"))
            .addSubEntity(new SettingEntity("清理缓存"))
        );
        settingBlockEntities.add(new SettingBlockEntity("关于")
            .addSubEntity(new SettingEntity("关于V次元"))
            .addSubEntity(new SettingEntity("检查更新"))
            .addSubEntity(new SettingEntity("开源软件声明"))
        );

        for(SettingBlockEntity settingBlockEntity:settingBlockEntities) {
            ItemSettingBlockBinding itemSettingBlockBinding = ItemSettingBlockBinding.inflate(inflater, binding.settings, false);
            binding.settings.addView(itemSettingBlockBinding.getRoot());
            itemSettingBlockBinding.classification.setText(settingBlockEntity.getClassification());


            for(int i=0;i<settingBlockEntity.getSubEntities().size();i++){
                SettingEntity settingEntity = settingBlockEntity.getSubEntities().get(i);

                ItemSettingSubItemBinding subItemBinding = ItemSettingSubItemBinding.inflate(inflater, itemSettingBlockBinding.subItem, false);
                itemSettingBlockBinding.subItem.addView(subItemBinding.getRoot());
                subItemBinding.settingItem.setText(settingEntity.name);

                subItemBinding.getRoot().setOnClickListener(v->{
                    MainApplication.toast(settingEntity.name);
                });

                if(i==settingBlockEntity.getSubEntities().size()-1){
                    subItemBinding.divider.setVisibility(View.GONE);
                }
            }
        }

        binding.exit.setOnClickListener(v->{
            finish();
            Intent intent=new Intent(this, AccountActivity.class);
            startActivity(intent);
        });
    }


    private class SettingBlockEntity{
        public SettingBlockEntity(String classification){
            this.classification=classification;
        }
        private String classification;
        public String getClassification(){
            return classification;
        }
        public ArrayList<SettingEntity> getSubEntities() {
            return subEntities;
        }
        public SettingBlockEntity addSubEntity(SettingEntity settingEntity){
            subEntities.add(settingEntity);
            return this;
        }

        private ArrayList<SettingEntity> subEntities=new ArrayList<>();
    }
    private class SettingEntity {
        private String name;
        public SettingEntity(String name){
            this.name=name;
        }
    }


}
