package dev.xesam.android.quickdemo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xe xesamguo@gmail.com on 15-8-12.
 */
public class QuickTreeNode implements Parcelable {
    String packageName;
    String name;
    List<QuickTreeNode> treeNodes;

    public QuickTreeNode(String packageName, String name, List<QuickTreeNode> treeNodes) {
        this.packageName = packageName;
        this.name = name;
        this.treeNodes = treeNodes;
    }

    public QuickTreeNode(String packageName, String name) {
        this.packageName = packageName;
        this.name = name;
    }

    public boolean isDemoNode() {
        return getChildCount() == 0 && name != null;
    }

    public String getCanonicalName() {
        if (isDemoNode()) {
            return packageName + "." + name;
        } else {
            throw new RuntimeException("not demo node");
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }

    public List<QuickTreeNode> getTreeNodes() {
        return treeNodes;
    }

    public int getChildCount() {
        return treeNodes == null ? 0 : treeNodes.size();
    }

    private static boolean isCurrentPackage(String packageName, String nodeName) {
        return nodeName.startsWith(packageName + ".");
    }

    private static boolean isDemoNode(String packageName, String nodeName) {
        return nodeName.split("\\.").length == packageName.split("\\.").length + 1;
    }

    public static QuickTreeNode makeDemoNode(String packageName, String name, Set<String> nodes) {
        List<QuickTreeNode> treeNodes1 = new ArrayList<>();

        Set<String> handledPackage = new HashSet<>();
        for (String node : nodes) {

            if (!isCurrentPackage(packageName, node)) {
                continue;
            }

            if (isDemoNode(packageName, node)) {
                treeNodes1.add(new QuickTreeNode(packageName, node.replace(packageName + ".", "")));
            } else {
                String newName = node.split("\\.")[packageName.split("\\.").length];
                String newPackageName = packageName + "." + newName;
                if (!handledPackage.contains(newPackageName)) {
                    handledPackage.add(newPackageName);
                    treeNodes1.add(makeDemoNode(newPackageName, newName, nodes));
                }
            }
        }
        Collections.sort(treeNodes1, new Comparator<QuickTreeNode>() {
            @Override
            public int compare(QuickTreeNode lhs, QuickTreeNode rhs) {
                if (lhs.isDemoNode()) {
                    return -1;
                } else if (rhs.isDemoNode()) {
                    return 1;
                } else {
                    return lhs.getName().charAt(0) > rhs.getName().charAt(0) ? 1 : -1;
                }
            }
        });
        return new QuickTreeNode(packageName, name, treeNodes1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.name);
        dest.writeTypedList(treeNodes);
    }

    protected QuickTreeNode(Parcel in) {
        this.packageName = in.readString();
        this.name = in.readString();
        this.treeNodes = in.createTypedArrayList(QuickTreeNode.CREATOR);
    }

    public static final Creator<QuickTreeNode> CREATOR = new Creator<QuickTreeNode>() {
        public QuickTreeNode createFromParcel(Parcel source) {
            return new QuickTreeNode(source);
        }

        public QuickTreeNode[] newArray(int size) {
            return new QuickTreeNode[size];
        }
    };
}
