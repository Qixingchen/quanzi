package com.tizi.quanzi.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.databinding.ItemGroupBinding;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.tool.StaticField;

import java.util.List;

/**
 * Created by qixingchen on 15/7/16.
 * 群组聊天列表Adapter
 */
public class GroupListAdapter extends RecyclerViewAdapterAbs {

    private List<GroupClass> groupClasses;
    private Context mContext;
    private Onclick onclick;

    /**
     * @param kGroupClasses 群组列表
     * @param mContext      上下文
     * @param onclick       群组被点击时的回调
     */
    public GroupListAdapter(final List<GroupClass> kGroupClasses, Context mContext, Onclick onclick) {
        this.groupClasses = kGroupClasses;
        this.mContext = mContext;
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
            final MyViewHolder myViewHolder = (MyViewHolder) holder;

            if (position == groupClasses.size()) {
                GroupClass addGroup = new GroupClass();
                addGroup.setName("创建圈子");
                myViewHolder.setBinding(addGroup);
                ((ImageView) myViewHolder.itemView.findViewById(R.id.group_face_image_view))
                        .setImageResource(R.drawable.add_group);
                myViewHolder.itemView.setOnLongClickListener(null);
            } else {
                final GroupClass group = groupClasses.get(position);
                myViewHolder.setBinding(group);

                /*长按事件*/
                myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final boolean needNotifi = AppStaticValue.getNeedNotifi(group.getConvId());
                        String noticeName;
                        if (needNotifi) {
                            noticeName = "静音";
                        } else {
                            noticeName = "取消静音";
                        }

                        String[] items = {noticeName, "清空记录"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle(group.getName()).setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        group.setNeedNotify(!needNotifi);
                                        break;
                                    case 1:
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setTitle("确认删除聊天记录么？").setMessage("删除后无法恢复");
                                        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                DBAct.getInstance().deleteAllMessage(group.getConvId());
                                                group.setLastMess("");
                                                group.setLastMessTime(0);
                                                notifyItemChanged(position);
                                            }
                                        });
                                        builder.setNegativeButton("取消", null);
                                        builder.create().show();
                                        break;
                                }
                            }
                        }).show();
                        return true;
                    }
                });
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
        return groupClasses == null ? 1 : Math.min(groupClasses.size() + 1, StaticField.Limit.MAX_QUANZI);

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

        public View itemView;

        private ItemGroupBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            binding = ItemGroupBinding.bind(itemView);
        }

        private void setBinding(GroupClass groupClass) {
            binding.setChat(groupClass);
        }

    }


}
