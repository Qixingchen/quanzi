package com.tizi.quanzi.ui.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ThemeAdapter;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.theme.ThemeSignUpFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class LockLock extends BaseFragment {

    private static LockLock mInstance;
    private RecyclerView mThemeItemsRecyclerView;
    private ThemeAdapter themeAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


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
    protected void findViews(View view) {
        mThemeItemsRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view);
        mThemeItemsRecyclerView.setHasFixedSize(true);
        themeAdapter = new ThemeAdapter(null, mActivity).setOnClick(new ThemeAdapter.OnClick() {
            @Override
            public void SignUP(Theme.ActsEntity act) {
                if (Tool.isGuest()) {
                    Tool.GuestAction(mActivity);
                    return;
                }
                getParentFragment().getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, ThemeSignUpFragment.newInstance(act.id))
                        .addToBackStack("ThemeSignUpFragment").commit();
            }

            @Override
            public void EnterTheme(Theme.ActsEntity act) {
                if (//Statue.IsDev.now == Statue.IsDev.dev ||
                        FriendTime.isInThemeTime(act.beginTime, act.endTime)) {
                    getParentFragment().getFragmentManager().beginTransaction()
                            .replace(R.id.fragment, BoomGroupFragment.newInstance(act.id))
                            .addToBackStack("BoomGroupFragment").commit();
                } else {
                    getParentFragment().getFragmentManager().beginTransaction()
                            .replace(R.id.fragment, CountdownFragment.newInstance(act.beginTime, act.id))
                            .addToBackStack("CountdownFragment").commit();
                }

            }
        });
        mLayoutManager = new LinearLayoutManager(mActivity);
        mThemeItemsRecyclerView.setLayoutManager(mLayoutManager);
        mThemeItemsRecyclerView.setAdapter(themeAdapter);
    }

    @Override
    protected void initViewsAndSetEvent() {
        ThemeActs.getNewInstance().setNetworkListener(
                new RetrofitNetworkAbs.NetworkListener() {
                    @Override
                    public void onOK(Object ts) {
                        Theme theme = (Theme) ts;
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
