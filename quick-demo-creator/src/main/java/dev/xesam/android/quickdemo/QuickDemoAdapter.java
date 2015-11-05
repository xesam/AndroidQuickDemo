package dev.xesam.android.quickdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by xesamguo@gmail.com on 15-8-12.
 */
public class QuickDemoAdapter extends BaseAdapter {

    Context context;
    QuickTreeNode treeNode;

    public QuickDemoAdapter(Context context, QuickTreeNode treeNode) {
        this.context = context;
        this.treeNode = treeNode;
    }

    @Override
    public int getCount() {
        return treeNode.getChildCount();
    }

    @Override
    public QuickTreeNode getItem(int position) {
        return treeNode.getTreeNodes().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuickTreeNode item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.quick_demo_apt, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (item.isDemoNode()) {
            try {
                Class clazz = Class.forName(item.getCanonicalName());
                if (QuickDemoAction.isActivity(clazz)) {
                    viewHolder.vPrefix.setText("A");
                    viewHolder.vPrefix.setBackgroundColor(convertView.getResources().getColor(R.color.quick_demo_activity));
                } else if (QuickDemoAction.isFragment(clazz)) {
                    viewHolder.vPrefix.setText("F");
                    viewHolder.vPrefix.setBackgroundColor(convertView.getResources().getColor(R.color.quick_demo_fragment));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            viewHolder.vName.setText(item.getName());
        } else {
            viewHolder.vPrefix.setText("P");
            viewHolder.vPrefix.setBackgroundColor(convertView.getResources().getColor(R.color.quick_demo_package));
            viewHolder.vName.setText(item.getName());
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView vPrefix;
        TextView vName;

        public ViewHolder(View itemView) {
            vPrefix = (TextView) itemView.findViewById(R.id.quick_demo_apt_prefix);
            vName = (TextView) itemView.findViewById(R.id.quick_demo_apt_name);
        }
    }
}
