package com.tizi.quanzi.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * Created by qixingchen on 15/9/4.
 */
public class MainFragment extends BaseFragment {

    private ViewPager mViewPager;
    private MainFragmentPagerAdapter mainFragmentPagerAdapter;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_activity, container, false);
    }

    @Override
    protected void findViews() {
        TabLayout tabLayout = (TabLayout) mActivity.findViewById(R.id.main_tab_layout);
        mViewPager = (ViewPager) mActivity.findViewById(R.id.viewpager);
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(this.getChildFragmentManager(), mActivity);
        mViewPager.setAdapter(mainFragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(getOnPageChangeListener());
        tabLayout.setupWithViewPager(mViewPager);

        TextView tab = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.custom_tab, null);
        tab.setText("小圈子");
        tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_insert_photo_black_36dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tab);

        tab = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.custom_tab, null);
        tab.setText("看一看");
        tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_insert_photo_black_36dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tab);

        tab = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.custom_tab, null);
        tab.setText("大世界");
        tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_insert_photo_black_36dp, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tab);
    }

    @Override
    protected void initViewsAndSetEvent() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(getOnPageChangeListener());
    }

    private ViewPager.OnPageChangeListener getOnPageChangeListener() {
        if (onPageChangeListener == null) {
            onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    //Log.w(TAG, "onPageScrolled position=" + position);
                }

                @Override
                public void onPageSelected(int position) {
                    Log.w(TAG, "onPageSelected position=" + position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    Log.w(TAG, "onPageScrollStateChanged state=" + state);
                }
            };
        }
        return onPageChangeListener;
    }
}
