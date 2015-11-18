package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.StaticField;

import java.util.List;

/**
 * Created by qixingchen on 15/7/16.
 * 群组聊天列表Adapter
 */
public class GroupListAdapter extends RecyclerViewAdapterAbs {

    private List<GroupClass> groupClasses;
    private Context context;
    private Onclick onclick;

    /**
     * @param kGroupClasses 群组列表
     * @param context       上下文
     * @param onclick       群组被点击时的回调
     */
    public GroupListAdapter(final List<GroupClass> kGroupClasses, Context context, Onclick onclick) {
        this.groupClasses = kGroupClasses;
        this.context = context;
        this.onclick = onclick;
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onChanged(GroupList list) {
        groupClasses = list.getGroupList();
        notifyDataSetChanged();
    }

    /**
     * 创建 ViewHolder
     *
     * @param viewGroup 需要创建ViewHolder的 ViewGroup
     * @param viewType  记录类型
     *
     * @return ViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_group, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        return new MyViewHolder(v);

    }


    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (MyViewHolder.class.isInstance(holder)) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;

            myViewHolder.unreadCountTextview.setVisibility(View.GONE);
            if (position == groupClasses.size()) {
                myViewHolder.groupNameTextview.setText("创建圈子");
                Picasso.with(context).load(R.drawable.add_group)
                        .resizeDimen(R.dimen.group_face, R.dimen.group_face)
                        .into(myViewHolder.groupFaceImageView);
                myViewHolder.lastTimeTextview.setText("");
                myViewHolder.groupNameTextview.setText("");
                myViewHolder.lastMessTextview.setText("");

            } else {
                if (groupClasses.get(position).UnreadCount != 0) {
                    myViewHolder.unreadCountTextview.setVisibility(View.VISIBLE);
                    myViewHolder.unreadCountTextview.setText(String.valueOf(groupClasses.get(position).UnreadCount));
                }
                myViewHolder.groupNameTextview.setText(groupClasses.get(position).Name);
                myViewHolder.lastMessTextview.setText(groupClasses.get(position).lastMess);
                myViewHolder.lastTimeTextview.setText(FriendTime.FriendlyDate(groupClasses.get(position).lastMessTime));
                Picasso.with(context)
                        .load(groupClasses.get(position).Face.toString())
                        .resizeDimen(R.dimen.group_face, R.dimen.group_face)
                        .into(myViewHolder.groupFaceImageView);
            }
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclick.itemClick(position);
                }
            });
        }

    }

    /**
     * @return 注册 记录数+1(最后一个用于创建圈子)
     */
    @Override
    public int getItemCount() {
        return groupClasses == null ? 1 : Math.min(groupClasses.size() + 1, StaticField.QueryLimit.MAX_QUANZI);

    }

    /**
     * 为 groupClasses 添加内容
     *
     * @param group 需要添加的群
     */
    public void addGroup(GroupClass group) {
        groupClasses.add(group);
        notifyDataSetChanged();
    }

    /**
     * 为 groupClasses 添加内容
     *
     * @param groups 需要添加的群
     */
    public void addGroup(List<GroupClass> groups) {
        for (GroupClass group : groups) {
            groupClasses.add(group);
        }
        notifyDataSetChanged();
    }

    /**
     * 点击接口
     */
    public interface Onclick {
        /**
         * 项目被点击
         *
         * @param position 点击序号
         */
        void itemClick(int position);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView groupFaceImageView;
        public TextView groupNameTextview, lastMessTextview, unreadCountTextview, lastTimeTextview;
        public View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            groupFaceImageView = (ImageView) itemView.findViewById(R.id.group_face_image_view);
            groupNameTextview = (TextView) itemView.findViewById(R.id.group_name_text_view);
            lastMessTextview = (TextView) itemView.findViewById(R.id.last_mess_text_view);
            unreadCountTextview = (TextView) itemView.findViewById(R.id.unread_count);
            lastTimeTextview = (TextView) itemView.findViewById(R.id.last_mess_time_text_view);
        }
    }


}
