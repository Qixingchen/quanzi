package com.tizi.quanzi.ui.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.tizi.quanzi.widget.custom_tab.SimpleCustomChromeTabsHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class LockLock extends BaseFragment {

    private static LockLock mInstance;

    private ViewPager viewPager;
    private ImageButton participateButton, detailButton, boomButton;
    private TextView participantsNum;

    private int nowPosition;
    private Theme themes;


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
        viewPager = (ViewPager) view.findViewById(R.id.VerticalViewPager);
        participateButton = (ImageButton) view.findViewById(R.id.participate_button);
        detailButton = (ImageButton) view.findViewById(R.id.detail_button);
        participantsNum = (TextView) view.findViewById(R.id.num_of_participants);
        boomButton = (ImageButton) view.findViewById(R.id.boom_button);

    }

    @Override
    protected void initViewsAndSetEvent() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (themes == null) {
                    return;
                }
                Theme.ActsEntity act = themes.acts.get(position);
                participantsNum.setText(String.valueOf(act.signNum));
                nowPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        boomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Theme.ActsEntity act = themes.acts.get(nowPosition);
                if (//BuildConfig.BUILD_TYPE.equals("debug") ||
                        FriendTime.isInThemeTime(act.beginTime, act.endTime)) {
                    getParentFragment().getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.disapear,
                                    R.anim.no_change, R.anim.slide_out_to_bottom)
                            .replace(R.id.fragment, BoomGroupFragment.newInstance(themes))
                            .addToBackStack("BoomGroupFragment").commit();
                } else {
                    getParentFragment().getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.disapear,
                                    R.anim.no_change, R.anim.slide_out_to_bottom)
                            .replace(R.id.fragment, CountdownFragment.newInstance(act.beginTime, act.id))
                            .addToBackStack("CountdownFragment").commit();
                }
            }
        });

        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Theme.ActsEntity act = themes.acts.get(nowPosition);
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
        });

        final SimpleCustomChromeTabsHelper mCustomTabHelper = new SimpleCustomChromeTabsHelper(mActivity);
        SimpleCustomChromeTabsHelper.CustomTabsUiBuilder uiBuilder = mCustomTabHelper.new CustomTabsUiBuilder();
        uiBuilder.setToolbarColor(mActivity.getResources().getColor(R.color.colorPrimary));

        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Theme.ActsEntity act = themes.acts.get(nowPosition);
                if (SimpleCustomChromeTabsHelper.canUseCustomChromeTabs(mActivity)) {
                    mCustomTabHelper.openUrl(act.detailUrl);
                } else {
                    mCustomTabHelper.openUrl(act.detailUrl);
                }
            }
        });

        ThemeActs.getNewInstance().setNetworkListener(
                new RetrofitNetworkAbs.NetworkListener() {
                    @Override
                    public void onOK(Object ts) {
                        themes = (Theme) ts;
                        if (themes.success) {
                            ThemesPagerAdapter adapter = new ThemesPagerAdapter(themes, mActivity);
                            viewPager.setAdapter(adapter);
                            for (Theme.ActsEntity act : themes.acts) {
                                mCustomTabHelper.prepareUrl(act.detailUrl);
                            }

                        } else {
                            Log.w(TAG, themes.msg);
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
