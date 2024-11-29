package com.sayi.vdim.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class Statusbar {
    public static int getStatusBarHeight(Activity activity){

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //应用区域
        Rect outRect1 = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        int statusBar = dm.heightPixels - outRect1.height();  //状态栏高度=屏幕高度-应用区域高度

        return statusBar;
    }
    public static int getStatusBHeight(Activity activity){
        int result=0;
        int resourceId=activity.getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0){
            result=activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void fixStatusBarMargin(Activity activity,View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            if (lp != null) {
                lp.topMargin += getStatusBarHeight(activity);
                view.requestLayout();
            }
        }
    }

    public static void paddingByStatusBar(Activity activity,View view) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(activity),
                view.getPaddingRight(), view.getPaddingBottom());
    }

}
