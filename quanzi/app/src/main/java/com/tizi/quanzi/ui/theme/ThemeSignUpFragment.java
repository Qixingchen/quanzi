package com.tizi.quanzi.ui.theme;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ThemeSignUpGroupAdapter;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * 报名参与活动
 */
public class ThemeSignUpFragment extends BaseFragment {

    private RecyclerView mGroupListRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ThemeSignUpGroupAdapter themeSignUpGroupAdapter;


    public ThemeSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_theme_sign_up, container, false);
    }

    @Override
    protected void findViews() {
        mGroupListRecyclerView = (RecyclerView) view.findViewById(R.id.group_item_recycler_view);
    }

    @Override
    protected void initViewsAndSetEvent() {
        themeSignUpGroupAdapter = new ThemeSignUpGroupAdapter(GroupList.getInstance().getGroupList(), mActivity);
        mGroupListRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(mActivity, 3);
        mGroupListRecyclerView.setLayoutManager(mLayoutManager);
        mGroupListRecyclerView.setAdapter(themeSignUpGroupAdapter);
    }


}
