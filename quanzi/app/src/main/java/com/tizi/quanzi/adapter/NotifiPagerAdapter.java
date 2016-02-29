package com.tizi.quanzi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.chatlibrary.action.MessageManage;
import com.tizi.chatlibrary.model.group.ConvGroupAbs;
import com.tizi.chatlibrary.model.message.ChatMessage;
import com.tizi.chatlibrary.staticData.GroupList;
import com.tizi.quanzi.R;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.ChatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/10/22.
 * 私聊 系统消息 PagerAdapter
 */
public class NotifiPagerAdapter extends PagerAdapter {

    private final static String TAG = NotifiPagerAdapter.class.getSimpleName();
    private final String tabTitles[] = new String[]{"私信", "系统"};
    private View Views[] = new View[tabTitles.length];
    private Context mContext;
    private RecyclerView privateMessRecyclerView, systemMessRecyclerView;
    private PrivateMessageAdapter privateMessageAdapter;
    private SystemMessageAdapter systemMessageAdapter;

    public NotifiPagerAdapter(Context context) {
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
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(Views[position]);
    }

    /**
     * 新增项目
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        switch (position) {
            case 0:
                privateMessRecyclerView = (RecyclerView) Views[0].findViewById(R.id.item_recycler_view);
                privateMessRecyclerView.setHasFixedSize(true);
                ArrayList privateMessPairList = GroupList.getInstance().getGroupList();
                List<PrivateMessPair> pairs = new ArrayList<>();
                for (Object group : privateMessPairList) {
                    if (((ConvGroupAbs) group).getType() == ChatMessage.CONVERSATION_TYPE_TWO_PERSION) {
                        pairs.add((PrivateMessPair) group);
                    }
                }

                privateMessageAdapter = new PrivateMessageAdapter(pairs,
                        mContext, new PrivateMessageAdapter.Onclick() {
                    /**
                     * 项目被点击
                     *
                     * @param privateMessPair 被点击的私聊消息组
                     */
                    @Override
                    public void priMessClick(ConvGroupAbs privateMessPair) {
                        Intent chat = new Intent(mContext, ChatActivity.class);
                        chat.putExtra("conversation", privateMessPair.getConvId());
                        chat.putExtra("chatType", StaticField.ConvType.TWO_PERSON);
                        mContext.startActivity(chat);
                    }
                });
                privateMessRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                privateMessRecyclerView.setAdapter(privateMessageAdapter);
                container.addView(Views[position], 0);
                break;

            case 1:
                systemMessRecyclerView = (RecyclerView) Views[1].findViewById(R.id.item_recycler_view);
                systemMessRecyclerView.setHasFixedSize(true);
                List<ChatMessage> chatMessages = new ArrayList<>();
                chatMessages.addAll(MessageManage.queryAllSystemMess());
                chatMessages.addAll(MessageManage.queryAllCommentNotifyMessage());

                systemMessageAdapter = new SystemMessageAdapter(chatMessages,
                        mContext, new SystemMessageAdapter.Onclick() {
                    /**
                     * 项目被点击
                     *
                     * @param chatMessage 被点击的消息
                     */
                    @Override
                    public void messClick(ChatMessage chatMessage) {
                        //ignore
                    }
                });
                systemMessRecyclerView.setAdapter(systemMessageAdapter);
                systemMessRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                container.addView(Views[position], 1);

                break;

            default:
                Log.e(TAG, "error view pager positon:" + position);
        }


        return Views[position];
    }
}
