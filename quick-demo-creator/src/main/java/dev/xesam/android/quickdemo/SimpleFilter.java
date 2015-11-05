package dev.xesam.android.quickdemo;

import android.content.Context;

import java.util.regex.Pattern;

/**
 * Created by xesamguo@gmail.com on 15-8-5.
 */
public class SimpleFilter implements QuickDemoFilter {

    Pattern target = Pattern.compile("demo|sample|example", Pattern.CASE_INSENSITIVE);
    String pkgName;

    public SimpleFilter(Context context) {
        pkgName = context.getPackageName();
    }

    @Override
    public boolean filter(String className) {
        String[] comps = className.split("\\.");
        String simpleClassName = comps[comps.length - 1];
        return className.startsWith(pkgName) && target.matcher(simpleClassName).find() && simpleClassName.indexOf("$") == -1;
    }
}
