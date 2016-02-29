package com.tizi.quanzi.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.GroupListAdapter;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.ChatActivity;
import com.tizi.quanzi.ui.new_group.NewGroupActivity;
import com.tizi.quanzi.widget.AutoGridfitLayoutManager;

import java.util.List;


public class GroupChatList extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private List<GroupClass> groupClasses;

    private RecyclerView mGroupListRecyclerView;
    private GroupListAdapter groupListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public GroupChatList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment GroupChatList.
     */
    public static GroupChatList newInstance(String param1, String param2) {
        GroupChatList fragment = new GroupChatList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mParam1.compareTo("1") != 0) {
            return;
        }
        groupClasses = GroupList.getInstance().getGroupList();
        showGroupAndSetCallBack();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_chat, container, false);
    }

    @Override
    protected void findViews(View view) {

    }

    @Override
    protected void initViewsAndSetEvent() {
        List<GroupClass> groupClasses = GroupList.getInstance().getGroupList();
        /*检查是否第一次运行，是的话刷新纪录*/
        //        if (AppStaticValue.getStringPrefer(getString(R.string.isFirstRun) + AppStaticValue.getUserID()).compareTo("NO") != 0) {
        //            Log.i(TAG, "是第一次运行，开始刷新纪录");
        //            Log.i(TAG, String.format("共有%d个群", groupClasses.size()));
        //            for (GroupClass groupClass : groupClasses) {
        //
        //                FlushMess.getNewInstance().Flush(groupClass.convId);
        //            }
        //        }
        //        AppStaticValue.setStringPrefer(getString(R.string.isFirstRun) + AppStaticValue.getUserID(), "NO");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * 显示群组并设置回调
     */
    private void showGroupAndSetCallBack() {
        mGroupListRecyclerView = (RecyclerView) mActivity.findViewById(R.id.group_item_recycler_view);
        GroupListAdapter.Onclick onclick = new GroupListAdapter.Onclick() {
            @Override
            public void itemClick(int position) {
                if (position == groupClasses.size()) {
                    if (Tool.isGuest()) {
                        Tool.GuestAction(mActivity);
                    } else {
                        Intent newGroup = new Intent(mActivity, NewGroupActivity.class);
                        startActivity(newGroup);
                    }
                } else {
                    Intent chatmess = new Intent(mActivity, ChatActivity.class);
                    chatmess.putExtra("chatType", StaticField.ConvType.GROUP);
                    chatmess.putExtra("conversation", groupClasses.get(position).getConvId());
                    startActivity(chatmess);
                }
            }
        };
        groupListAdapter = new GroupListAdapter(groupClasses, mActivity, onclick);
        mGroupListRecyclerView.setHasFixedSize(true);
        mLayoutManager = new AutoGridfitLayoutManager(mContext, 150);
        mGroupListRecyclerView.setLayoutManager(mLayoutManager);
        mGroupListRecyclerView.setAdapter(groupListAdapter);
    }

}
