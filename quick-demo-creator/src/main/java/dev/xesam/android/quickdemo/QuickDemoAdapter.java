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

    public static final String ACTIVITY_FLAG = "A";
    public static final String FRAGMENT_FLAG = "F";
    public static final String PACKAGE_FLAG = "P";

    private Context mContext;
    private QuickTreeNode mTreeNode;

    public QuickDemoAdapter(Context context, QuickTreeNode treeNode) {
        this.mContext = context;
        this.mTreeNode = treeNode;
    }

    @Override
    public int getCount() {
        return mTreeNode.getChildCount();
    }

    @Override
    public QuickTreeNode getItem(int position) {
        return mTreeNode.getTreeNodes().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuickTreeNode item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.quick_demo_apt, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (item.isDemoNode()) {
            try {
                Class clazz = Class.forName(item.getCanonicalName());
                if (QuickDemoAction.isActivity(clazz)) {
                    viewHolder.vPrefix.setText(ACTIVITY_FLAG);
                    viewHolder.vPrefix.setBackgroundColor(convertView.getResources().getColor(R.color.quick_demo_activity));
                } else if (QuickDemoAction.isFragment(clazz)) {
                    viewHolder.vPrefix.setText(FRAGMENT_FLAG);
                    viewHolder.vPrefix.setBackgroundColor(convertView.getResources().getColor(R.color.quick_demo_fragment));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            viewHolder.vName.setText(item.getName());
        } else {
            viewHolder.vPrefix.setText(PACKAGE_FLAG);
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
