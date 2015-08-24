package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.network.GetVolley;

import java.util.List;

/**
 * Created by qixingchen on 15/7/16.
 * 群组列表Adapter
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.MyViewHolder> {

    private List<GroupClass> groupClasses;
    private Context context;
    private Onclick onclick;

    /**
     * @param groupClasses 群组列表
     * @param context      上下文
     * @param onclick      群组被点击时的回调
     */
    public GroupListAdapter(List<GroupClass> groupClasses, Context context, Onclick onclick) {
        this.groupClasses = groupClasses;
        this.context = context;
        this.onclick = onclick;
    }

    /**
     * 创建 ViewHolder
     *
     * @param viewGroup 需要创建ViewHolder的 ViewGroup
     * @param i         记录类型
     *
     * @return MyViewHolder
     *
     * @see com.tizi.quanzi.adapter.GroupListAdapter.MyViewHolder
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.group_item, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param myViewHolder 被绑定的ViewHolder
     * @param position     列表位置
     */
    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {
        myViewHolder.unreadTextview.setText("unread commit");
        myViewHolder.groupNameTextview.setText(groupClasses.get(position).groupName);
        myViewHolder.lastCommitTextview.setText("2015-05-12 12:12");
        Picasso.with(context)
                .load(groupClasses.get(position).groupFace.toString())
                .into(myViewHolder.groupFaceImageView);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.itemClick(position);
            }
        });
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return groupClasses == null ? 0 : groupClasses.size();
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
        public TextView groupNameTextview, lastCommitTextview, unreadTextview, lastTimeTextview;
        public View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            groupFaceImageView = (ImageView) itemView.findViewById(R.id.group_face_image_view);
            groupNameTextview = (TextView) itemView.findViewById(R.id.group_name_text_view);
            lastCommitTextview = (TextView) itemView.findViewById(R.id.last_commit_text_view);
            unreadTextview = (TextView) itemView.findViewById(R.id.unread_text_view);
            lastTimeTextview = (TextView) itemView.findViewById(R.id.last_commit_text_view);
        }
    }

    /**
     * 为 groupClasses 添加内容
     *
     * @param group 需要添加的群
     */
    public void addAGroup(GroupClass group) {
        groupClasses.add(group);
        notifyDataSetChanged();
    }
}
