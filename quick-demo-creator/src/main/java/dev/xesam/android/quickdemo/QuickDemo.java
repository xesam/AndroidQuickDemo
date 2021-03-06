package dev.xesam.android.quickdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import dalvik.system.DexFile;

/**
 * Created by xesamguo@gmail.com on 15-8-5.
 */
public class QuickDemo {

    @NonNull
    static ActivityInfo[] getAllActivityInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES).activities;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return new ActivityInfo[]{};
    }

    public static void inflateActivity(View view, int listViewId) {
        inflateActivity((ListView) view.findViewById(listViewId));
    }

    public static void inflateActivity(Activity activity, int listViewId) {
        inflateActivity((ListView) activity.findViewById(listViewId));
    }

    public static void inflateActivity(ListView listView) {
        ActivityInfo[] activityInfos = getAllActivityInfo(listView.getContext());
        List<String> activities = new LinkedList<>();
        for (ActivityInfo activityInfo : activityInfos) {
            if (activityInfo.name.endsWith(listView.getContext().getClass().getCanonicalName())) {
                continue;
            }
            activities.add(activityInfo.name);
        }
        listView.setAdapter(new ArrayAdapter<String>(listView.getContext(), R.layout.quick_demo_apt, activities) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.quick_demo_apt, parent, false);
                    convertView.setTag(new QuickDemoAdapter.ViewHolder(convertView));
                }
                QuickDemoAdapter.ViewHolder viewHolder = (QuickDemoAdapter.ViewHolder) convertView.getTag();
                viewHolder.vPrefix.setText(QuickDemoAdapter.ACTIVITY_FLAG);
                viewHolder.vPrefix.setBackgroundColor(convertView.getResources().getColor(R.color.quick_demo_activity));
                viewHolder.vName.setText(getItem(position));
                return convertView;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    view.getContext().startActivity(
                            new Intent(view.getContext(), Class.forName(((ArrayAdapter<String>) parent.getAdapter()).getItem(position)))
                    );
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    ///////////////////////////////////////////


    /**
     * 过滤所有符合条件的类
     */
    static Set<String> filterClasses(Context context, QuickDemoFilter classFilter) {
        DexFile dexFile = null;
        Set<String> demos = new HashSet<>();
        try {
            dexFile = new DexFile(context.getPackageCodePath());
            for (Enumeration<String> iterator = dexFile.entries(); iterator.hasMoreElements(); ) {
                String className = iterator.nextElement();
                if (classFilter.filter(className)) {
                    demos.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dexFile != null) {
                    dexFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return demos;
    }

    public static void inflateDemo(FragmentManager fragmentManager, int containerId, QuickDemoTree demoTree) {
        QuickTreeNode root = demoTree.getRoot();
        fragmentManager.beginTransaction()
                .replace(containerId, QuickDemoFragment.newInstance(root))
                .commit();
    }

    public static void inflateDemo(FragmentActivity activity, int containerId, QuickDemoFilter quickDemoFilter) {
        Set<String> demos = QuickDemo.filterClasses(activity, quickDemoFilter);
        QuickDemoTree demoTree = QuickDemoTree.makeDemoTree(activity, demos);
        inflateDemo(activity.getSupportFragmentManager(), containerId, demoTree);
    }

    public static void inflateDemo(FragmentActivity activity, int containerId) {
        inflateDemo(activity, containerId, new SimpleFilter(activity));
    }
}
