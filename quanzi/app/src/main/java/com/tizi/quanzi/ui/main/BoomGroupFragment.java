package com.tizi.quanzi.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.BoomGroupListAdapter;
import com.tizi.quanzi.gson.BoomGroup;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.ChatActivity;

/**
 * 碰撞圈子列表界面
 */
public class BoomGroupFragment extends BaseFragment {


    //get Intent
    private static final String ACT_ID = "actID";
    private android.widget.TextView countdownTime;
    private android.widget.TextView groupNums;
    private android.support.v7.widget.RecyclerView boomGroupItemRecyclerview;
    private BoomGroupListAdapter boomGroupListAdapter;
    private String themeID;

    public BoomGroupFragment() {
        // Required empty public constructor
    }

    public static BoomGroupFragment newInstance(String actID) {
        BoomGroupFragment fragment = new BoomGroupFragment();
        Bundle args = new Bundle();
        args.putString(ACT_ID, actID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            themeID = getArguments().getString(ACT_ID);
        }

    }

    public void setThemeID(String themeID) {
        this.themeID = themeID;
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
                groupNums.setText(String.valueOf(boomGroup.groupmatch.size()));
                boomGroupListAdapter = new BoomGroupListAdapter(boomGroup.groupmatch, mContext);
                boomGroupListAdapter.setOnClick(new BoomGroupListAdapter.OnClick() {
                    @Override
                    public void clickBoomGroup(BoomGroup.GroupmatchEntity boomGroup) {
                        Intent chatmess = new Intent(mActivity, ChatActivity.class);
                        chatmess.putExtra("chatType", StaticField.ChatBothUserType.BoomGroup);
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


}
