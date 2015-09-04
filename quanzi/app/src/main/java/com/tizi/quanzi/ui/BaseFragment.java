package com.tizi.quanzi.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by qixingchen on 15/8/31.
 * Fragment 的抽象
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected final String TAG = this.getClass().getSimpleName();

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState);

    @Override
    public void onStart() {
        super.onStart();
        findViews();
        initViewsAndSetEvent();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    protected abstract void findViews();

    protected abstract void initViewsAndSetEvent();
}
