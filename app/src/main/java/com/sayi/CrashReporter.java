package com.sayi;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CrashReporter extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        LinearLayout ll=new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(layoutParams);
        ll.setBackgroundColor(Color.WHITE);
        String error=getIntent().getStringExtra("error");
        TextView tx=new TextView(this);
        tx.setText(error);
        tx.setTextIsSelectable(true);
        ll.addView(tx);
        setContentView(ll);
    }
}
