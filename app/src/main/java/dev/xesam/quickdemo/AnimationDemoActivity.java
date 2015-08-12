package dev.xesam.quickdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import dev.xesam.android.quickdemo.QuickDemo;


public class AnimationDemoActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QuickDemo.inflateActivity(this, R.id.lv);
    }

}
