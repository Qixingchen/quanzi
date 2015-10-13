package com.tizi.quanzi.ui.main;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.PrivateMessageAdapter;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.ChatActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 私信／系统通知
 */
public class PrivateMessageFragment extends BaseFragment {

    private RecyclerView mPrivateMessRecyclerView;
    private PrivateMessageAdapter privateMessageAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public PrivateMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_private_message, container, false);
    }

    @Override
    protected void findViews(View view) {

    }

    @Override
    protected void initViewsAndSetEvent() {
        mPrivateMessRecyclerView = (RecyclerView) mActivity.findViewById(R.id.private_message_item_recycler_view);
        List<SystemMessage> systemMessages = DBAct.getInstance().quaryAllSysMess();
        List<PrivateMessPair> privateMessPairs = PrivateMessPairList.getInstance().getGroupList();
        for (SystemMessage systemMessage : systemMessages) {
            // TODO: 15/9/18 筛选无需显示的消息
            if (systemMessage.getSys_msg_flag() == StaticField.SystemMessAttrName.systemFlag.group_change_name) {
                continue;
            }
            privateMessPairs.add(PrivateMessPair.PriMessFromSystemMess(systemMessage));
        }
        privateMessageAdapter = new PrivateMessageAdapter(privateMessPairs, mActivity,
                new PrivateMessageAdapter.Onclick() {

                    /**
                     * 项目被点击,忽略 并没有什么需要处理的
                     *
                     * @param systemMessage 被点击的系统消息
                     */
                    @Override
                    public void systemMessClick(SystemMessage systemMessage) {
                        /*ignore*/
                    }

                    /**
                     * 项目被点击,发起私聊
                     *
                     * @param privateMessPair 被点击的私聊消息组
                     */
                    @Override
                    public void priMessClick(PrivateMessPair privateMessPair) {
                        Intent chat = new Intent(getActivity(), ChatActivity.class);
                        chat.putExtra("conversation", privateMessPair.convId);
                        chat.putExtra("chatType", StaticField.ConvType.twoPerson);
                        startActivity(chat);
                    }
                });
        mPrivateMessRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mPrivateMessRecyclerView.setLayoutManager(mLayoutManager);
        mPrivateMessRecyclerView.setAdapter(privateMessageAdapter);
    }

}
