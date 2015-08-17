package com.tizi.quanzi.fragment.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

/**
 * Created by qixingchen on 15/7/13.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"小圈子", "看一看", "大世界"};
    private Context context;
    public Fragment[] fragments = new Fragment[3];

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                fragments[0] = GroupChatList.newInstance("1", "2");
                return fragments[0];
            case 1:
                fragments[1] = LockLock.newInstance();
                return fragments[1];
            case 2:
                fragments[2] = BigWorld.newInstance("1", "2");
                return fragments[2];
            default:
                Toast.makeText(context, "异常Fragment在" + position, Toast.LENGTH_SHORT).show();
                return BigWorld.newInstance("3", "2");
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }


}
