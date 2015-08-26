package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.tizi.quanzi.R;

/**
 * Created by qixingchen on 15/8/26.
 * 群空间 参与与相册 PagerAdapter
 */
public class GroupZonePagerAdapter extends PagerAdapter {


    private final String tabTitles[] = new String[]{"参与", "相册"};
    private View Views[] = new View[tabTitles.length];
    private Context mContext;

    public GroupZonePagerAdapter(Context context) {
        mContext = context;
        LayoutInflater Inflater = LayoutInflater.from(context);
        Views[0] = Inflater.inflate(R.layout.group_zone_dyns, null);
        Views[1] = Inflater.inflate(R.layout.group_zone_dyns, null);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    //获取PageTitle
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(Views[position]);
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(Views[position], 0);
        return Views[position];
    }
}
