package com.tizi.quanzi.ui.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ThemesPagerAdapter;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.theme.ThemeSignUpFragment;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class LockLock extends BaseFragment {

    private static LockLock mInstance;

    private VerticalViewPager viewPager;


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
        viewPager = (VerticalViewPager) view.findViewById(R.id.VerticalViewPager);
        //        mThemeItemsRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view);
        //        mThemeItemsRecyclerView.setHasFixedSize(true);
        //        mLayoutManager = new LinearLayoutManager(mActivity);
        //        mThemeItemsRecyclerView.setLayoutManager(mLayoutManager);
        //        mThemeItemsRecyclerView.setAdapter(themeAdapter);
    }

    @Override
    protected void initViewsAndSetEvent() {

        final ThemesPagerAdapter.OnClick onClick = new ThemesPagerAdapter.OnClick() {
            @Override
            public void SignUP(Theme.ActsEntity act) {
                if (Tool.isGuest()) {
                    Tool.GuestAction(mActivity);
                    return;
                }
                getParentFragment().getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.disapear,
                                R.anim.no_change, R.anim.slide_out_to_bottom)
                        .replace(R.id.fragment, ThemeSignUpFragment.newInstance(act.id))
                        .addToBackStack("ThemeSignUpFragment").commit();
            }

            @Override
            public void EnterTheme(Theme.ActsEntity act) {
                if (//BuildConfig.BUILD_TYPE.equals("debug") ||
                        FriendTime.isInThemeTime(act.beginTime, act.endTime)) {
                    getParentFragment().getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.disapear,
                                    R.anim.no_change, R.anim.slide_out_to_bottom)
                            .replace(R.id.fragment, BoomGroupFragment.newInstance(act.id, act.beginTime, act.endTime))
                            .addToBackStack("BoomGroupFragment").commit();
                } else {
                    getParentFragment().getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.disapear,
                                    R.anim.no_change, R.anim.slide_out_to_bottom)
                            .replace(R.id.fragment, CountdownFragment.newInstance(act.beginTime, act.id))
                            .addToBackStack("CountdownFragment").commit();
                }

            }

            @Override
            public void watchIntro(Theme.ActsEntity act) {
                getParentFragment().getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, WebViewFragment.newInstance(
                                WebViewFragment.Theme_Intro, act.detailUrl))
                        .addToBackStack("WebViewFragment").commit();
            }
        };

        ThemeActs.getNewInstance().setNetworkListener(
                new RetrofitNetworkAbs.NetworkListener() {
                    @Override
                    public void onOK(Object ts) {
                        Theme theme = (Theme) ts;
                        if (theme.success) {
                            ThemesPagerAdapter adapter = new ThemesPagerAdapter(theme, mActivity, onClick);
                            viewPager.setAdapter(adapter);

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
