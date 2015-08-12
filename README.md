#AndroidQuickDemo


#两种模式：

## 1. 列出所有已经注册的Activity，点击即可打开

用法：

    QuickDemo.inflateActivity(activity, R.id.listview);

## 2. 像文件管理器一样，列出 app 中 package 的目录索引，并按照给定的过滤规则过滤需要展示的组件（Activity 以及 Fragment）

用法：

    将 dev.xesam.android.quickdemo.QuickDemoActivity 设置为 LAUNCHER Activity 即可

#默认过滤规则

参见 SimpleFilter：

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
            return className.startsWith(pkgName) && target.matcher(simpleClassName).find();
        }
    }