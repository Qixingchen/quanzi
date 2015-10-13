package com.tizi.quanzi.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.otto.BusProvider;

/**
 * Created by qixingchen on 15/8/31.
 * Fragment 的抽象
 */
public abstract class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();
    protected Activity mActivity;
    protected Context mContext;
    protected View view;

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState);

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        findViews(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewsAndSetEvent();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
        try {
            BusProvider.getInstance().register(this);
        } catch (IllegalArgumentException ignore) {
        }
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            BusProvider.getInstance().unregister(this);
        } catch (IllegalArgumentException ignore) {
        }
    }

    protected abstract void findViews(View view);

    protected abstract void initViewsAndSetEvent();
}
