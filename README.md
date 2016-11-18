
# 【Android】如何快速构建Android Demo

说明：由于 Android Plugin for Gradle 2.0.0 的 InstantRun 方式破坏了 debug 版本的启动方式，因此，new DexFile(getPackageCodePath()); 的方式无法获取到所有的 Class。
此问题暂未找到简便的解决方式，因此，本 lib 只能运行在非 InstantRun 模式下。

可以在 Android Studio 的 File - Setting - Build,Execution,Deployment - Instant Run 中禁用Instant Run。

问题详述可以参见 [DexFile in 2.0 versions of Android Studio and Gradle](http://stackoverflow.com/questions/36572515/dexfile-in-2-0-versions-of-android-studio-and-gradle/36594966#36594966)

如果您有好的解决方案，请联系 [xesam](http://xesam.github.io/about/)

## 简介

在 Android 学习的过程中，经常需要针对某些项目来写一些测试的例子，或者在做一些 demo 的时候，都需要先写 Activity 然后注册。
如果里面有太多的跳转的话，还需要每个跳转都增加一个事件。这些都是非常繁琐的步骤。那么如何省略这些步骤呢？

有一种办法就是使用 Fragment，然后按照“约定大于配置”的原则，遍历安装包下符合条件的 Fragment 然后自动构建目录索引与跳转动作。
通俗来讲，就是把 APK 里面的包结构文件当做树形结构的文件夹来处理，然后构建一个文件浏览器。当然，我们构建的“类浏览器”。

一个完整的实现请参考 *[https://github.com/xesam/AndroidQuickDemo](https://github.com/xesam/AndroidQuickDemo)*

PS：AndroidQuickDemo 同时增加了 Activity 的支持，但是由于 Android 系统的限制，Activity必须被注册，这一点是无法绕过去的。
当然，也可以使用插件的原理，达到自动索引 Activity 的目的，不过，在我的实际使用中，当一定要使用 Activity 的时候，肯定是为了使用或者探究 Activity 的直接效果，而不应该进行代理或者拦截。

## 使用方式

    compile 'dev.xesam.android:quick-demo-creator:0.2.0'

# 使用方式

现在支持两种模式：

## 1. 列出所有已经注册的 Activity，点击即可打开
这个方式只是使用一个列表简单列出所有的已注册 Activity，然后点击即可打开。

用法：

    QuickDemo.inflateActivity(activity, R.id.listview);

## 2. 像文件管理器一样，列出 app 的目录索引

个人比较推荐这种用法，直接构建完整的“类浏览器”。

lib工程里面已经内置了一个 QuickDemoActivity，你只需要在你的 Android 项目中的 AndroidManifest.xml 中注册这个 Activity，并设置为 LAUNCHER Activity 即可

```xml
<activity
    android:name="dev.xesam.android.quickdemo.QuickDemoActivity"
    android:label="@string/app_name">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

如果你不想使用内置的 QuickDemoActivity，那么也可以在自己的 Activity 中来显示目录，只需要提供一个 container view id 就可以了，示例如下：

```java
public class MyManActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QuickDemo.inflateActivity(this, R.id.lv);
    }
}

```

默认的目录索引只会显示当前 PackageName 下名称中包含 "demo 或者 sample 或者 example"的 Activity 或者 Fragment，
如果想按照自己的规则来定义过滤，可以自定义 QuickDemoFilter,一个示例如下：

```java
public class CustomFilter implements QuickDemoFilter {

    Pattern target = Pattern.compile("demo|sample|example", Pattern.CASE_INSENSITIVE);
    String pkgName;

    public CustomFilter(Context context) {
        pkgName = context.getPackageName();
    }

    @Override
    public boolean filter(String className) {
        String[] comps = className.split("\\.");
        String simpleClassName = comps[comps.length - 1];
        return className.startsWith(pkgName) && target.matcher(simpleClassName).find() && simpleClassName.indexOf("$") == -1;
    }
}
```

然后在对应的 Activity 中：

```java
public class MyManActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QuickDemo.inflateDemo(this, R.id.quick_demo_root, new CustomFilter(this));
    }
}

```

## 效果预览

![Screenshot_2015-08-12-23-36-42.png](https://github.com/xesam/AndroidQuickDemo/raw/master/Screenshot_2015-08-12-23-36-42.png)

![Screenshot_2015-08-12-23-36-47.png](https://github.com/xesam/AndroidQuickDemo/raw/master/Screenshot_2015-08-12-23-36-47.png)

![Screenshot_2015-08-12-23-36-53.png](https://github.com/xesam/AndroidQuickDemo/blob/master/Screenshot_2015-08-12-23-36-53.png)

