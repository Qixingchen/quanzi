package com.tizi.quanzi.ui.main;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.BoomGroupListAdapter;
import com.tizi.quanzi.dataStatic.BoomGroupList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.BoomGroup;
import com.tizi.quanzi.model.BoomGroupClass;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Timer;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.ChatActivity;

import java.util.List;

/**
 * 碰撞圈子列表界面
 */
public class BoomGroupFragment extends BaseFragment {


    //get Intent
    private static final String ACT_ID = "actID";
    private static final String START_TIME = "START_TIME";
    private static final String END_TIME = "END_TIME";

    private android.widget.TextView countdownTime;
    private android.widget.TextView groupNums;
    private android.support.v7.widget.RecyclerView boomGroupItemRecyclerview;
    private BoomGroupListAdapter boomGroupListAdapter;
    private String themeID;
    private String startTime, endTime;
    private Timer timer;

    public BoomGroupFragment() {
        // Required empty public constructor
    }

    public static BoomGroupFragment newInstance(String actID, String startTime, String endTime) {
        BoomGroupFragment fragment = new BoomGroupFragment();
        Bundle args = new Bundle();
        args.putString(ACT_ID, actID);
        args.putString(START_TIME, startTime);
        args.putString(END_TIME, endTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            themeID = getArguments().getString(ACT_ID);
            startTime = getArguments().getString(START_TIME);
            endTime = getArguments().getString(END_TIME);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_boom_group, container, false);
    }

    @Override
    protected void findViews(View view) {
        this.boomGroupItemRecyclerview = (RecyclerView) view.findViewById(R.id.boom_group_item_recycler_view);
        this.groupNums = (TextView) view.findViewById(R.id.group_nums);
        this.countdownTime = (TextView) view.findViewById(R.id.countdown_time);
    }

    @Override
    protected void initViewsAndSetEvent() {
        ThemeActs.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                BoomGroup boomGroup = (BoomGroup) ts;
                groupNums.setText(String.format("共有%d个圈子配对成功,点击和小伙伴们开始热聊吧!",
                        boomGroup.groupmatch.size()));

                List<BoomGroupClass> booms = BoomGroupClass.getBoomGroupListFromBoomGroupGson(boomGroup.groupmatch);

                for (BoomGroupClass boom : booms) {
                    ChatMessage chatMessage = DBAct.getInstance().queryNewestMessage(boom.convId);
                    if (chatMessage != null) {
                        boom.lastMessTime = chatMessage.create_time;
                        boom.lastMess = ChatMessage.getContentText(chatMessage);
                    }
                }

                BoomGroupList.getInstance().setGroupList(booms);

                boomGroupListAdapter = new BoomGroupListAdapter(mContext, themeID);
                boomGroupListAdapter.setOnClick(new BoomGroupListAdapter.OnClick() {
                    @Override
                    public void clickBoomGroup(BoomGroupClass boomGroup) {
                        Intent chatmess = new Intent(mActivity, ChatActivity.class);
                        chatmess.putExtra("chatType", StaticField.ConvType.BoomGroup);
                        chatmess.putExtra("conversation", boomGroup.convId);
                        startActivity(chatmess);
                    }
                });
                boomGroupItemRecyclerview.setAdapter(boomGroupListAdapter);
                boomGroupItemRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
            }

            @Override
            public void onError(String Message) {

            }
        }).getBoomGroup(themeID);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (boomGroupListAdapter != null) {
            boomGroupListAdapter.notifyDataSetChanged();
        }
        int countDown = FriendTime.getThemeCountDown(startTime, endTime);

        timer = new Timer();
        timer.setOnResult(new Timer.OnResult() {
            @Override
            public void OK() {
                getFragmentManager().popBackStack();
            }

            @Override
            public void countdown(int s) {
                countdownTime.setText(String.format("还剩%d:%d:%d结束", s / 3600, (s % 3600) / 60,
                        s % 60));
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, countDown * 1000);
    }


    @Override
    public void onPause() {
        super.onPause();
        timer.cancel(false);
    }
}
