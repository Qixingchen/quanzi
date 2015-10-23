package com.tizi.quanzi.ui.main;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.NotifiPagerAdapter;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.dataStatic.SystemMessageList;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * 私信／系统通知
 */
public class NotifiMessageFragment extends BaseFragment {


    private ViewPager notifiViewPager;
    private NotifiPagerAdapter notifiPagerAdapter;


    public NotifiMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifi_message, container, false);
    }

    @Override
    protected void findViews(View view) {
        //crash is know issue at https://code.google.com/p/android/issues/detail?id=183166
        notifiViewPager = (ViewPager) view.findViewById(R.id.viewpager);


    }

    @Override
    protected void initViewsAndSetEvent() {

        notifiPagerAdapter = new NotifiPagerAdapter(mContext);
        notifiViewPager.setAdapter(notifiPagerAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        PrivateMessPairList.getInstance().callUpdate();
        SystemMessageList.getInstance().callUpdate();
    }
}
