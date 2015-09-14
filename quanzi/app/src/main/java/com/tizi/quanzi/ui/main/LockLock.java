package com.tizi.quanzi.ui.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ThemeAdapter;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.NetWorkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.theme.ThemeSignUpFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class LockLock extends BaseFragment {

    private RecyclerView mThemeItemsRecyclerView;
    private ThemeAdapter themeAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static LockLock mInstance;


    public LockLock() {
        // Required empty public constructor
    }

    public static LockLock newInstance() {
        mInstance = new LockLock();
        return mInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lock_lock, container, false);
    }

    @Override
    protected void findViews() {
        mThemeItemsRecyclerView = (RecyclerView) mActivity.findViewById(R.id.theme_item_recycler_view);
        mThemeItemsRecyclerView.setHasFixedSize(true);
        themeAdapter = new ThemeAdapter(null, mActivity).setOnClick(new ThemeAdapter.OnClick() {
            @Override
            public void Participate(Theme.ActsEntity act) {
                getParentFragment().getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, new ThemeSignUpFragment()).commit();
            }
        });
        mLayoutManager = new LinearLayoutManager(mActivity);
        mThemeItemsRecyclerView.setLayoutManager(mLayoutManager);
        mThemeItemsRecyclerView.setAdapter(themeAdapter);
    }

    @Override
    protected void initViewsAndSetEvent() {
        ThemeActs.getInstance(mContext).setNetworkListener(
                new NetWorkAbs.NetworkListener() {
                    @Override
                    public void onOK(Object ts) {
                        Theme theme = new Gson().fromJson((String) ts, Theme.class);
                        if (theme.success) {
                            for (Theme.ActsEntity act : theme.acts) {
                                themeAdapter.addItem(act);
                            }
                        } else {
                            Log.w(TAG, theme.msg);
                        }
                    }

                    @Override
                    public void onError(String Message) {
                        Log.w(TAG, Message);
                    }
                }
        ).getThemes();
    }

}
