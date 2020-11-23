package com.icurety;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class WinPopup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) ((float) width * 0.8f), (int) ((float) height * 0.8f));


    }
}