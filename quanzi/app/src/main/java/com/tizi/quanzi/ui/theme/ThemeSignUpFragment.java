package com.tizi.quanzi.ui.theme;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.GroupSelectAdapter;
import com.tizi.quanzi.gson.GroupIDs;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.widget.AutoGridfitLayoutManager;

import java.util.Map;
import java.util.TreeMap;

/**
 * 报名参与活动
 */
public class ThemeSignUpFragment extends BaseFragment {

    private static final String ACT_ID = "actID";
    private RecyclerView mGroupListRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private GroupSelectAdapter groupSelectAdapter;
    private String actID;

    public ThemeSignUpFragment() {
        // Required empty public constructor
    }

    public static ThemeSignUpFragment newInstance(String actID) {
        ThemeSignUpFragment fragment = new ThemeSignUpFragment();
        Bundle args = new Bundle();
        args.putString(ACT_ID, actID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            actID = getArguments().getString(ACT_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_theme_sign_up, container, false);
    }

    @Override
    protected void findViews(View view) {
        mGroupListRecyclerView = (RecyclerView) view.findViewById(R.id.group_item_recycler_view);
    }

    @Override
    protected void initViewsAndSetEvent() {
        groupSelectAdapter = new GroupSelectAdapter(GroupList.getInstance().getGroupList(),
                mActivity, actID, GroupSelectAdapter.Theme_Sign_UP_Group);
        mGroupListRecyclerView.setHasFixedSize(true);
        mLayoutManager = new AutoGridfitLayoutManager(mContext, 80);
        mGroupListRecyclerView.setLayoutManager(mLayoutManager);
        mGroupListRecyclerView.setAdapter(groupSelectAdapter);

        ThemeActs.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                GroupIDs groupIDs = (GroupIDs) ts;
                Map<String, Boolean> signedGroup = new TreeMap<>();
                for (String groupid : groupIDs.grpids) {
                    signedGroup.put(groupid, true);
                }
                groupSelectAdapter.setGroupsSignedIn(signedGroup);
            }

            @Override
            public void onError(String Message) {

            }
        }).getMySignedGroups(actID);
    }


}
