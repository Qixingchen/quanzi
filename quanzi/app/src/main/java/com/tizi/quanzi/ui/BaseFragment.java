package com.tizi.quanzi.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVAnalytics;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.otto.FragmentResume;
import com.tizi.quanzi.tool.Tool;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by qixingchen on 15/8/31.
 * Fragment 的抽象
 */
public abstract class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();
    protected Activity mActivity;
    protected Context mContext;
    protected View view;
    protected boolean isAttached = false;

    private CompositeSubscription mCompositeSubscription;

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState);

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        findViews(view);
        Tool.hideKeyboard(view, getActivity());
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
        isAttached = true;
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
        isAttached = false;
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
        try {
            BusProvider.getInstance().unregister(this);
        } catch (IllegalArgumentException ignore) {
        }
    }

    protected abstract void findViews(View view);

    protected abstract void initViewsAndSetEvent();

    @Override
    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart(TAG);
        BusProvider.getInstance().post(new FragmentResume(true, TAG));
    }

    @Override
    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd(TAG);
        BusProvider.getInstance().post(new FragmentResume(false, TAG));
    }

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }
}
