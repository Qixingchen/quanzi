package com.tizi.quanzi.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * Created by qixingchen on 15/9/4.
 * 主界面 Fragment
 */
public class MainFragment extends BaseFragment {

    private ViewPager mViewPager;
    private MainFragmentPagerAdapter mainFragmentPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_activity, container, false);
    }

    @Override
    protected void findViews(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.main_tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(this.getChildFragmentManager(), mActivity);
        mViewPager.setAdapter(mainFragmentPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        TextView tab = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.custom_tab, null);
        tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.group, 0, 0);
        tab.setSelected(true);
        tabLayout.getTabAt(0).setCustomView(tab);

        tab = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.custom_tab, null);
        tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.theme_park, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tab);

        tab = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.custom_tab, null);
        tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.my, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tab);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((MainActivity) mActivity).onTabChanged(mainFragmentPagerAdapter.getPageTitle(position).toString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(1);
    }

    @Override
    protected void initViewsAndSetEvent() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
