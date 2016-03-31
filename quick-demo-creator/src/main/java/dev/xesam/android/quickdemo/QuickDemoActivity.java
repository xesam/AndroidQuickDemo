package dev.xesam.android.quickdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * default Host Activity
 * Created by xesamguo@gmail.com on 15-8-5.
 */
public class QuickDemoActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_demo_activity);

        QuickDemo.inflateDemo(this, R.id.quick_demo_root);
    }

}
