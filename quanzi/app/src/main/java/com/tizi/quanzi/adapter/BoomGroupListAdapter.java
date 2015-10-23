package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.dataStatic.BoomGroupList;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.BoomGroupClass;
import com.tizi.quanzi.otto.BusProvider;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by qixingchen on 15/10/9.
 * 碰撞圈子列表 适配器
 */
public class BoomGroupListAdapter extends RecyclerViewAdapterAbs {

    private List<BoomGroupClass> boomGroups;
    private Context context;
    private OnClick onClick;

    public BoomGroupListAdapter(Context context) {
        this.boomGroups = BoomGroupList.getInstance().getGroupList();
        this.context = context;
        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void onChanged(BoomGroupList ignore) {
        notifyDataSetChanged();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        BusProvider.getInstance().unregister(this);
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
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
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_boom_group, parent, false);
        return new BoomGroupItemViewHolder(v);
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (BoomGroupItemViewHolder.class.isInstance(holder)) {
            BoomGroupItemViewHolder vh = (BoomGroupItemViewHolder) holder;
            final BoomGroupClass boomGroup = boomGroups.get(position);
            /*1*/
            vh.firstGroupName.setText(boomGroup.groupName1);
            Picasso.with(context).load(boomGroup.icon1)
                    .resizeDimen(R.dimen.group_face, R.dimen.group_face).into(vh.firstGroupFace);

            /*2*/
            vh.secondGroupName.setText(boomGroup.groupName2);
            Picasso.with(context).load(boomGroup.icon2)
                    .resizeDimen(R.dimen.group_face, R.dimen.group_face).into(vh.secondGroupFace);

            /*both*/
            if (boomGroup.isGroup1MyGroup) {
                vh.firstGroupDescription.setText(R.string.my_group);
                vh.secondGroupDescription.setText(R.string.boom_group);
            } else {
                vh.firstGroupDescription.setText(R.string.boom_group);
                vh.secondGroupDescription.setText(R.string.my_group);
            }

            /*unread*/
            Integer unreadCount = BoomGroupList.getInstance().getUnreadCount(boomGroup.convId, boomGroup.ID);
            vh.unreadCount.setText(String.valueOf(unreadCount == null ? 0 : unreadCount));

            /*onCLick*/
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClick == null) {
                        Log.w(TAG, "onclick is null");
                        return;
                    }
                    onClick.clickBoomGroup(boomGroup);
                }
            });
        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return boomGroups == null ? 0 : boomGroups.size();
    }


    public interface OnClick {
        void clickBoomGroup(BoomGroupClass boomGroup);
    }

    class BoomGroupItemViewHolder extends RecyclerView.ViewHolder {
        private de.hdodenhof.circleimageview.CircleImageView firstGroupFace;
        private android.widget.TextView firstGroupName;
        private android.widget.TextView firstGroupDescription;
        private android.widget.TextView unreadCount;
        private de.hdodenhof.circleimageview.CircleImageView secondGroupFace;
        private android.widget.TextView secondGroupName;
        private android.widget.TextView secondGroupDescription;

        public BoomGroupItemViewHolder(View v) {
            super(v);
            this.secondGroupDescription = (TextView) v.findViewById(R.id.second_group_description);
            this.secondGroupName = (TextView) v.findViewById(R.id.second_group_name);
            this.secondGroupFace = (CircleImageView) v.findViewById(R.id.second_group_face);
            this.unreadCount = (TextView) v.findViewById(R.id.unread_count);
            this.firstGroupDescription = (TextView) v.findViewById(R.id.first_group_description);
            this.firstGroupName = (TextView) v.findViewById(R.id.first_group_name);
            this.firstGroupFace = (CircleImageView) v.findViewById(R.id.first_group_face);
        }
    }
}
