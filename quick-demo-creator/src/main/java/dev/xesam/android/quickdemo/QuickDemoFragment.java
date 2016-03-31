package dev.xesam.android.quickdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


public class QuickDemoFragment extends Fragment {

    static final String TREE_NODE = "TREE_NODE";

    public static QuickDemoFragment newInstance(QuickTreeNode treeNode) {
        QuickDemoFragment fragment = new QuickDemoFragment();
        Bundle args = new Bundle();
        args.putParcelable(TREE_NODE, treeNode);
        fragment.setArguments(args);
        return fragment;
    }

    public QuickDemoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quick_demo_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        QuickTreeNode treeNode = getArguments().getParcelable(TREE_NODE);

        TextView vTitle = (TextView) view.findViewById(R.id.quick_demo_pkg_name);
        vTitle.setText(treeNode.getPackageName());

        ListView vLV = (ListView) view.findViewById(R.id.quick_demo_lv);
        vLV.setAdapter(new QuickDemoAdapter(getActivity(), treeNode));
        vLV.setOnItemClickListener(new QuickDemoAction(getFragmentManager()));
    }
}
