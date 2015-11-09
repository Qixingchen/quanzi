package com.tizi.quanzi.ui.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.widget.Toast;

/**
 * Created by qixingchen on 15/7/13.
 * 主界面 FragmentPagerAdapter
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    public Fragment[] fragments = new Fragment[3];
    private String tabTitles[] = new String[]{"圈子", "主题", "个人"};
    private Context context;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    /**
     * @return page个数
     */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    /**
     * @param position 需要Fragment的位置
     *
     * @return Fragment 新实例
     */
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

    /**
     * @param position 需要标题的位置
     *
     * @return 标题
     */
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }


}
