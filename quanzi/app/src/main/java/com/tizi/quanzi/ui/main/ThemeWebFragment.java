package com.tizi.quanzi.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * 主题网页加载界面
 */
public class ThemeWebFragment extends BaseFragment {


    public ThemeWebFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_theme_web, container, false);
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void initViewsAndSetEvent() {

    }


}
