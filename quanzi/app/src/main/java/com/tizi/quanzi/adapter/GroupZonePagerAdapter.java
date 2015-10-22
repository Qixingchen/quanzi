package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.network.DynamicAct;
import com.tizi.quanzi.network.RetrofitNetworkAbs;

/**
 * Created by qixingchen on 15/8/26.
 * 群空间 参与与相册 PagerAdapter
 */
@Deprecated
public class GroupZonePagerAdapter extends PagerAdapter {


    private final static String TAG = GroupZonePagerAdapter.class.getSimpleName();
    private final String tabTitles[] = new String[]{"参与", "相册"};
    private View Views[] = new View[tabTitles.length];
    private Context mContext;
    private RecyclerView mDynsItemsRecyclerView;
    private DynsAdapter dynsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public GroupZonePagerAdapter(Context context) {
        mContext = context;
        LayoutInflater Inflater = LayoutInflater.from(context);
        Views[0] = Inflater.inflate(R.layout.item_only_recycler_view, null);
        Views[1] = Inflater.inflate(R.layout.item_only_recycler_view, null);
    }

    /**
     * 获取标签数量
     */
    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    /**
     * 获取PageTitle
     * 第一屏不显示 Know issue & fixed on next relese
     * https://code.google.com/p/android/issues/detail?id=183127
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(Views[position]);
    }

    /**
     * 新增项目
     */
    @Override
    public Object instantiateItem(View container, int position) {
        if (position == 0) {

            mDynsItemsRecyclerView = (RecyclerView) Views[0].findViewById(R.id.item_recycler_view);
            mDynsItemsRecyclerView.setHasFixedSize(true);
            dynsAdapter = new DynsAdapter(null, mContext);
            mLayoutManager = new LinearLayoutManager(mContext);
            mDynsItemsRecyclerView.setLayoutManager(mLayoutManager);
            mDynsItemsRecyclerView.setAdapter(dynsAdapter);

            DynamicAct.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                @Override
                public void onOK(Object ts) {
                    Dyns dyns = (Dyns) ts;
                    dynsAdapter.addItems(dyns.dyns);
                }

                @Override
                public void onError(String Message) {

                }
            }).getGroupDynamic("", "", 1);

        }
        ((ViewPager) container).addView(Views[position], 0);
        return Views[position];
    }
}
