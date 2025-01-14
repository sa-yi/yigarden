package com.sayi.vdim.activities.fragments;

import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;

import androidx.annotation.*;
import androidx.fragment.app.*;
import androidx.viewpager2.adapter.*;
import androidx.viewpager2.widget.*;

import com.sayi.*;
import com.sayi.vdim.activities.*;
import com.sayi.vdim.databinding.*;

import java.util.*;

public class RegisterFragment extends Fragment {
    RegisterFragmentBinding binding;
    ViewPagerAdapter adapter;

    int currPage=0;
    int allPage=4;

    RegisterNameFragment registerNameFragment=new RegisterNameFragment();
    RegisterEmailFragment registerEmailFragment=new RegisterEmailFragment();
    RegisterPhoneFragment registerPhoneFragment=new RegisterPhoneFragment();
    RegisterTermsFragment registerTermsFragment=new RegisterTermsFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=RegisterFragmentBinding.inflate(getLayoutInflater());
        adapter=new ViewPagerAdapter(requireActivity());
        binding.rootViewPager.setAdapter(adapter);
        binding.rootViewPager.setUserInputEnabled(false);

        adapter.addFragment(registerNameFragment);
        adapter.addFragment(registerEmailFragment);
        adapter.addFragment(registerPhoneFragment);
        adapter.addFragment(registerTermsFragment);

        binding.confirm.setOnClickListener(v->{
            if(!binding.confirm.isClickable())return;
            if(currPage==allPage-1){
                if(!registerTermsFragment.isTermAccepted()){
                    MainApplication.toast("请阅读并同意条款");
                    return;
                }
                Intent intent=new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
                return;
            }
            currPage++;
            binding.rootViewPager.setCurrentItem(currPage);
            Log.d("confirm",currPage+":"+allPage);
            if(currPage==allPage-1){
                binding.confirm.setText("进入V次元");
                return;
            }
        });
        binding.back.setOnClickListener(v->{
            if(binding.rootViewPager.getCurrentItem()==0)binding.back.setVisibility(View.INVISIBLE);
            else binding.back.setVisibility(View.VISIBLE);
            currPage--;
            binding.rootViewPager.setCurrentItem(binding.rootViewPager.getCurrentItem()-1);
            if(currPage==allPage){
                binding.confirm.setText("进入V次元");
            }
        });
        binding.rootViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currPage=position;
                if(position==0){
                    binding.back.setVisibility(View.INVISIBLE);
                }else {
                    binding.back.setVisibility(View.VISIBLE);
                }
                binding.confirm.setText(String.format("下一步(%d/%d)",currPage+1,allPage-1));
            }
        });

        return binding.getRoot();
    }
    public class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }
}
