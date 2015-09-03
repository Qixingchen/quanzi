package com.tizi.quanzi.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.GroupListAdapter;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.tool.FlushMess;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.ChatActivity;
import com.tizi.quanzi.ui.NewGroup.NewGroupActivity;

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


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment GroupChatList.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupChatList newInstance(String param1, String param2) {
        GroupChatList fragment = new GroupChatList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GroupChatList() {
        // Required empty public constructor
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
    protected void findViews() {

    }

    @Override
    protected void initViewsAndSetEvent() {
        List<GroupClass> groupClasses = GroupList.getInstance().getGroupList();
        if (App.getPrefer(getString(R.string.isFirstRun)).compareTo("") == 0) {
            for (GroupClass groupClass : groupClasses) {

                FlushMess.getInstance().Flush(groupClass.convId);
            }
        }
        App.setPrefer(getString(R.string.isFirstRun), "NO");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 主界面点击新建群后调用这里
     */
    public void newAGroup() {
        // TODO: 15/8/20 创建群
        //        String GroupName = "xingchen test2", icon = "http://ac-hy5srahi.clouddn.com/2j5dU2E1dvXcVD1TKPmgNBC.jpeg";
        //        String notice = "公告", userID = App.getUserID(), tag = "[{tagid:\"1\",tagname:\"1name\"},{tagid:\"2\",tagName:\"2name\"}]";
        //        AddOrQuitGroup.getInstance().setNewGroupListener(new AddOrQuitGroup.NewGroupListener() {
        //            @Override
        //            public void onOK(GroupClass groupClass) {
        //                groupListAdapter.addGroup(groupClass);
        //
        //            }
        //
        //            @Override
        //            public void onError() {
        //
        //            }
        //        }).NewAGroup();
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
                    Intent newGroup = new Intent(mActivity, NewGroupActivity.class);
                    startActivity(newGroup);
                } else {
                    Intent chatmess = new Intent(mActivity, ChatActivity.class);
                    chatmess.putExtra("conversation", groupClasses.get(position).convId);
                    startActivity(chatmess);
                }
            }
        };
        groupListAdapter = new GroupListAdapter(groupClasses, mActivity, onclick);
        mGroupListRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(mActivity, 2);
        mGroupListRecyclerView.setLayoutManager(mLayoutManager);
        mGroupListRecyclerView.setAdapter(groupListAdapter);
    }

}
