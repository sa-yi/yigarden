package com.sayi.vdim.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;


import java.util.HashMap;
import java.util.Map;

public class DialogLoading {

    private static Map<Activity, ProgressDialog> progressDialogMap = new HashMap<>();

    public static void show(Activity activity, String msg) {
        ProgressDialog progressDialog = progressDialogMap.get(activity);
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialogMap.put(activity, progressDialog);
        } else {
            progressDialog.setMessage(msg);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }
    public static void show(Activity activity, String msg,boolean cancelable) {
        show(activity, msg,cancelable,null);
    }


    public static void show(Activity activity, String msg, boolean cancelable, DialogInterface.OnCancelListener onCancelListener) {
        ProgressDialog progressDialog = progressDialogMap.get(activity);
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(cancelable);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(onCancelListener);
            progressDialogMap.put(activity, progressDialog);
        } else {
            progressDialog.setMessage(msg);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public static void dismiss(Activity activity) {
        ProgressDialog progressDialog = progressDialogMap.get(activity);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void dismissAll() {
        for (Map.Entry<Activity, ProgressDialog> entry : progressDialogMap.entrySet()) {
            ProgressDialog progressDialog = entry.getValue();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        progressDialogMap.clear();
    }
}
