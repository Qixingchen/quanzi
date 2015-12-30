package com.tizi.quanzi.view_model;

import android.content.Context;

import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.ui.BaseActivity;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * Created by qixingchen on 15/12/30.
 */
public abstract class BaseViewModel<T extends BaseFragment> {

    protected BaseActivity mActivity;
    protected T mFragment;
    protected Context mContext;

    public BaseViewModel(T t) {
        this.mFragment = t;
        mActivity = t.getBaseActivity();
        mContext = t.getContext();
        try {
            BusProvider.getInstance().register(this);
        } catch (Exception ignore) {
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            BusProvider.getInstance().unregister(this);
        } catch (Exception ignore) {
        }
    }
}
