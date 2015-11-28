package com.tizi.quanzi.ui.main;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ThemesBoomGroupListAdapter;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * 碰撞圈子列表界面
 */
public class BoomGroupFragment extends BaseFragment {


    //get Intent
    private static final String THEME = "theme";

    private android.support.v7.widget.RecyclerView themesBoomGroupListsRecyclerView;
    private ThemesBoomGroupListAdapter themesBoomGroupListAdapter;
    private Theme theme;

    public BoomGroupFragment() {
        // Required empty public constructor
    }

    public static BoomGroupFragment newInstance(Theme theme) {
        BoomGroupFragment fragment = new BoomGroupFragment();
        Bundle args = new Bundle();
        args.putParcelable(THEME, theme);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            theme = getArguments().getParcelable(THEME);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_boom_group, container, false);
    }

    @Override
    protected void findViews(View view) {
        this.themesBoomGroupListsRecyclerView = (RecyclerView) view.findViewById(R.id.themes_boom_group_recycler_view);
    }

    @Override
    protected void initViewsAndSetEvent() {
        themesBoomGroupListAdapter = new ThemesBoomGroupListAdapter(theme, mActivity);
        themesBoomGroupListsRecyclerView.setAdapter(themesBoomGroupListAdapter);
        themesBoomGroupListsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }
}
