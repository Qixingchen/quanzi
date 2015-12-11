package com.tizi.quanzi.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVAnalytics;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.otto.FragmentResume;
import com.tizi.quanzi.tool.Tool;

import java.util.ArrayList;
import java.util.List;

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
    protected Fragment mFragment;
    protected View view;
    protected boolean isAttached = false;

    protected List<Integer> menus = new ArrayList<>();

    private CompositeSubscription mCompositeSubscription;

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState);

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        findViews(view);
        Tool.addHideKeyboardToAllViews(view, getActivity());
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
        mFragment = this;
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
        for (Integer i : menus) {
            ((BaseActivity) mActivity).mMenu.removeItem(i);
        }
    }

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }

    protected void menuAdded(@IdRes int id, String title, int ShowAsAction, @DrawableRes int icon) {
        if (((BaseActivity) mActivity).mMenu.findItem(id) == null) {
            ((BaseActivity) mActivity).mMenu.add(Menu.NONE, id, Menu.NONE, title)
                    .setIcon(icon)
                    .setShowAsAction(ShowAsAction);
            menus.add(id);
        }
    }

    protected void menuAdded(@IdRes int id, String title, int ShowAsAction) {
        if (((BaseActivity) mActivity).mMenu.findItem(id) == null) {
            ((BaseActivity) mActivity).mMenu.add(Menu.NONE, id, Menu.NONE, title)
                    .setShowAsAction(ShowAsAction);
            menus.add(id);
        }

    }
}
