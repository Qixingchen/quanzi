package com.tizi.quanzi.fragment.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.GroupListAdapter;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.ui.ChatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class GroupChatList extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private GroupClass[] groupClasses;

    private RecyclerView mGroupListRecyclerView;
    private GroupListAdapter groupListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Activity mActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
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


        Intent intent = mActivity.getIntent();

        final String[] ids, icons, groupNames, types;
        ids = intent.getStringArrayExtra("groupids");
        icons = intent.getStringArrayExtra("groupicons");
        groupNames = intent.getStringArrayExtra("groupgroupNames");
        types = intent.getStringArrayExtra("grouptypes");
        //todo use getGroupByEntity
        groupClasses = GroupClass.getGroupByInent(ids, icons, groupNames, types);

        mGroupListRecyclerView = (RecyclerView) mActivity.findViewById(R.id.group_item_recycler_view);
        GroupListAdapter.Onclick onclick = new GroupListAdapter.Onclick() {
            @Override
            public void itemClick(int position) {
                Intent chatmess = new Intent(mActivity, ChatActivity.class);
                chatmess.putExtra("conversation", ids[position]);
                startActivity(chatmess);
            }
        };
        groupListAdapter = new GroupListAdapter(groupClasses, mActivity, onclick);
        mGroupListRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mGroupListRecyclerView.setLayoutManager(mLayoutManager);
        mGroupListRecyclerView.setAdapter(groupListAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //leancloud
        //todo 测试用，应当删除
//        List<String> clientIds = new ArrayList<String>();
//        clientIds.add(App.getUserID());
//        clientIds.add("Bob");

// 我们给对话增加一个自定义属性 type，表示单聊还是群聊
// 常量定义：
// int ConversationType_OneOne = 0; // 两个人之间的单聊
// int ConversationType_Group = 1;  // 多人之间的群聊
//        Map<String, Object> attr = new HashMap<>();
//        attr.put("type", "1");
//        App.getImClient().createConversation(clientIds, attr, new AVIMConversationCreatedCallback() {
//            @Override
//            public void done(AVIMConversation conversation, AVException e) {
//                if (null != conversation) {
//                    // 成功了，这时候可以显示对话的 Activity 页面（假定为 ChatActivity）了。
//                    Intent chatmess = new Intent(mActivity, ChatActivity.class);
//                    chatmess.putExtra("conversation", conversation.getConversationId());
//                    startActivity(chatmess);
//                }
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_chat, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
