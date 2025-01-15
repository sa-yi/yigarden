package com.sayi.vdim.utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class Dialog {
    private Context context;
    private AlertDialog.Builder builder;

    public static Dialog init(Context context) {
        return new Dialog(context);
    }

    private Dialog(Context context) {
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
    }

    public Dialog setupDialog(String title, String content) {
        builder.setTitle(title);
        builder.setMessage(content);


        return this;
    }

    public Dialog setPositiveButton(String positiveText, android.content.DialogInterface.OnClickListener listener) {
        builder.setPositiveButton(positiveText, listener);
        return this;
    }

    public Dialog setNegativeButton(String negativeText, android.content.DialogInterface.OnClickListener listener) {
        builder.setNegativeButton(negativeText, listener);
        return this;
    }
    public Dialog setNetrualButton(String netrualText, android.content.DialogInterface.OnClickListener listener){
        builder.setNeutralButton(netrualText,listener);
        return this;
    }


    public void show() {
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
}
