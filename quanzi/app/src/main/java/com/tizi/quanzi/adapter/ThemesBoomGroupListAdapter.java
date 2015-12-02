package com.tizi.quanzi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.dataStatic.BoomGroupList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.BoomGroup;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.model.BoomGroupClass;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Timer;
import com.tizi.quanzi.ui.ChatActivity;
import com.tizi.quanzi.widget.AutoLinearLayoutManager;

import java.util.List;

/**
 * Created by Yulan on 2015/11/28.
 * 多个活动的总对话列表
 */
public class ThemesBoomGroupListAdapter extends RecyclerViewAdapterAbs {

    private Theme theme;
    private Activity mActivity;

    public ThemesBoomGroupListAdapter(Theme theme, Activity mActivity) {
        this.theme = theme;
        this.mActivity = mActivity;
    }

    /**
     * 创建 ViewHolder
     *
     * @param parent   需要创建ViewHolder的 ViewGroup
     * @param viewType 记录类型
     *
     * @return ViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder vh;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_boom_group_list, parent, false);
        vh = new ThemesBoomViewHolder(v);
        return vh;
    }


    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (ThemesBoomViewHolder.class.isInstance(holder)) {
            ThemesBoomViewHolder themeVH = (ThemesBoomViewHolder) holder;
            if (themeVH.timer != null) {
                themeVH.timer.cancel();
            }
        }
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (ThemesBoomViewHolder.class.isInstance(holder)) {
            ThemesBoomViewHolder themeVH = (ThemesBoomViewHolder) holder;
            themeVH.setView(theme.acts.get(position));
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (ThemesBoomViewHolder.class.isInstance(holder)) {
            ThemesBoomViewHolder themeVH = (ThemesBoomViewHolder) holder;
            themeVH.timer.cancel();
        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return theme == null ? 0 : theme.acts.size();
    }

    class ThemesBoomViewHolder extends RecyclerView.ViewHolder {
        private android.widget.TextView countdownTime;
        private android.widget.TextView groupNums;
        private android.support.v7.widget.RecyclerView boomGroupItemRecyclerview;
        private BoomGroupListAdapter boomGroupListAdapter;
        private String themeID;
        private String startTime, endTime;
        private Timer timer;
        private Theme.ActsEntity act;

        public ThemesBoomViewHolder(View itemView) {
            super(itemView);
            init(itemView);
        }

        private void init(View view) {
            this.boomGroupItemRecyclerview = (RecyclerView) view.findViewById(R.id.boom_group_item_recycler_view);
            this.groupNums = (TextView) view.findViewById(R.id.group_nums);
            this.countdownTime = (TextView) view.findViewById(R.id.countdown_time);
        }

        protected void setView(final Theme.ActsEntity act) {
            this.act = act;
            themeID = act.id;
            startTime = act.beginTime;
            endTime = act.endTime;

            ThemeActs.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                @Override
                public void onOK(Object ts) {
                    BoomGroup boomGroup = (BoomGroup) ts;
                    groupNums.setText(String.format("共有%d个圈子配对成功,点击和小伙伴们开始热聊吧!",
                            boomGroup.groupmatch.size()));

                    List<BoomGroupClass> booms = BoomGroupClass.getBoomGroupListFromBoomGroupGson(boomGroup.groupmatch, themeID);

                    for (BoomGroupClass boom : booms) {
                        ChatMessage chatMessage = DBAct.getInstance().queryNewestMessage(boom.convId);
                        if (chatMessage != null) {
                            boom.lastMessTime = chatMessage.create_time;
                            boom.lastMess = ChatMessage.getContentText(chatMessage);
                        }
                        if (BoomGroupList.getInstance().getGroup(boom.ID) == null) {
                            BoomGroupList.getInstance().addGroup(boom);
                        }
                    }


                    boomGroupListAdapter = new BoomGroupListAdapter(mActivity, themeID);
                    boomGroupListAdapter.setOnClick(new BoomGroupListAdapter.OnClick() {
                        @Override
                        public void clickBoomGroup(BoomGroupClass boomGroup) {
                            Intent chatmess = new Intent(mActivity, ChatActivity.class);
                            chatmess.putExtra("chatType", StaticField.ConvType.BoomGroup);
                            chatmess.putExtra("conversation", boomGroup.convId);
                            mActivity.startActivity(chatmess);
                        }
                    });
                    boomGroupItemRecyclerview.setAdapter(boomGroupListAdapter);
                    boomGroupItemRecyclerview.setLayoutManager(new AutoLinearLayoutManager(mActivity, OrientationHelper.VERTICAL, false));
                }

                @Override
                public void onError(String Message) {

                }
            }).getBoomGroup(themeID);

            int countDown = FriendTime.getThemeCountDown(startTime, endTime);

            if (timer != null) {
                timer.cancel();
            }
            timer = new Timer();
            timer.setOnResult(new Timer.OnResult() {
                @Override
                public void OK() {
                }

                @Override
                public void countdown(long s, long goneS) {
                    countdownTime.setText(String.format("%s还剩%d:%d:%d结束", act.title, s / 3600, (s % 3600) / 60,
                            s % 60));
                }
            }).setTimer(countDown * 1000).start();
        }
    }
}
