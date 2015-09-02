package dev.xesam.android.quickdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by xesamguo@gmail.com on 15-8-12.
 */
public class QuickDemoAction implements AdapterView.OnItemClickListener {

    FragmentManager fragmentManager;

    public QuickDemoAction(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    private void nextFragment(String packageName, Fragment fragment) {

        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.quick_demo_root, fragment)
                .addToBackStack(packageName)
                .commit();
    }

    static boolean isActivity(Class clazz) {
        return Activity.class.isAssignableFrom(clazz);
    }

    static boolean isFragment(Class clazz) {
        return Fragment.class.isAssignableFrom(clazz);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        QuickTreeNode item = (QuickTreeNode) parent.getAdapter().getItem(position);
        String packageName = item.getPackageName();
        if (item.isDemoNode()) {
            try {
                Class clazz = Class.forName(item.getCanonicalName());
                if (isActivity(clazz)) {
                    parent.getContext().startActivity(new Intent(parent.getContext(), clazz));
                } else if (isFragment(clazz)) {
                    nextFragment(packageName, (Fragment) clazz.newInstance());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            nextFragment(packageName, QuickDemoFragment.newInstance(item));
        }
    }
}
