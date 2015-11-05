package dev.xesam.android.quickdemo;

import android.content.Context;

import java.util.Set;

/**
 * Created by xe xesamguo@gmail.com on 15-8-12.
 */
public class QuickDemoTree {

    String packageName;
    QuickTreeNode root;

    public QuickDemoTree(String packageName, QuickTreeNode root) {
        this.packageName = packageName;
        this.root = root;
    }

    public String getPackageName() {
        return packageName;
    }

    public QuickTreeNode getRoot() {
        return root;
    }

    public static QuickDemoTree makeDemoTree(Context context, Set<String> demos) {
        return makeDemoTree(context.getPackageName(), demos);
    }

    public static QuickDemoTree makeDemoTree(String packageName, Set<String> demos) {
        QuickTreeNode root = QuickTreeNode.makeDemoNode(packageName, packageName, demos);
        QuickDemoTree demoTree = new QuickDemoTree(packageName, root);
        return demoTree;
    }
}
