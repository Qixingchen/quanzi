package com.tizi.quanzi.ui.main;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.chatlibrary.action.MessageManage;
import com.tizi.chatlibrary.model.group.ConvGroupAbs;
import com.tizi.chatlibrary.model.message.ChatMessage;
import com.tizi.chatlibrary.staticData.GroupList;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.NotifiPagerAdapter;
import com.tizi.quanzi.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

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
        //如果没有未读私信,但有未读通知,跳转到通知页
        List<ConvGroupAbs> groups = GroupList.getInstance().getGroupList();
        List<ConvGroupAbs> PrivateMessList = new ArrayList<>();
        List<ConvGroupAbs> systemMessageList = new ArrayList<>();

        int unread = 0;

        for (ConvGroupAbs groupAbs : groups) {
            if (groupAbs.getType() == ChatMessage.CONVERSATION_TYPE_TWO_PERSION) {
                PrivateMessList.add(groupAbs);
                unread += groupAbs.getUnreadCount();
            }
        }

        if (unread == 0
                && MessageManage.queryUnreadCommentCount() + MessageManage.queryUnreadCommentCount() != 0) {
            notifiViewPager.setCurrentItem(1);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
