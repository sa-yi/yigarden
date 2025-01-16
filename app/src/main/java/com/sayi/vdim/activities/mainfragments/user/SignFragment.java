package com.sayi.vdim.activities.mainfragments.user;

import android.animation.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.fragment.app.*;

import com.sayi.*;
import com.sayi.vdim.databinding.*;

public class SignFragment extends Fragment {
    SignFragmentBinding binding;
    String[] presets = new String[]{
        "今日已听洛天依，请洛天依放心！",
        "佬，我没有你怎么活啊，佬",
        "你就是我的唯依",
        "佬，我亲爱的佬，你怎么就是个纸片人啊",
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SignFragmentBinding.inflate(getLayoutInflater());


        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

        });

        // 创建适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireActivity(),
            android.R.layout.simple_dropdown_item_1line,
            presets
        ) {
            @NonNull
            @Override
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        results.values = presets; // 始终返回完整数据列表
                        results.count = presets.length;
                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        notifyDataSetChanged(); // 通知数据更新
                    }
                };
            }
        };

        binding.autoCompleteTextView.setAdapter(adapter);

        binding.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.autoCompleteTextView.showDropDown();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.autoCompleteTextView.setOnClickListener(v -> {
            binding.autoCompleteTextView.showDropDown();
        });

        binding.sign.setOnClickListener(v -> {
            MainApplication.toast("签到成功");
            toggleViewsWithAnimation(binding.unsignView, binding.signedView);
        });
        return binding.getRoot();
    }


    private void toggleViewsWithAnimation(View toHide, View toShow) {
        int animDur = 200;
        // 获取父布局
        FrameLayout parent = (FrameLayout) toHide.getParent();

        // 确保目标视图已经被测量
        if (toShow.getVisibility() == View.GONE) {
            toShow.measure(View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        }

        int fromHeight = toHide.getHeight();
        int toHeight = toShow.getMeasuredHeight();

        // 动态调整 FrameLayout 的高度
        ValueAnimator heightAnimator = ValueAnimator.ofInt(fromHeight, toHeight);
        heightAnimator.setDuration(animDur);
        heightAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parent.getLayoutParams();
            params.height = animatedValue;
            parent.setLayoutParams(params);
        });

        // 隐藏当前视图的动画
        ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(toHide, "alpha", 1f, 0f);
        hideAnimator.setDuration(animDur);
        hideAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        hideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                toHide.setVisibility(View.GONE); // 动画结束后隐藏视图
            }
        });

        // 显示目标视图的动画
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(toShow, "alpha", 0f, 1f);
        showAnimator.setDuration(animDur);
        showAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        showAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                toShow.setVisibility(View.VISIBLE); // 动画开始时显示视图
            }
        });

        // 同步执行高度动画和透明度动画
        heightAnimator.start();
        hideAnimator.start();
        showAnimator.start();
    }


}
