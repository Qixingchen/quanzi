package com.tizi.quanzi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.tool.StaticField;

/**
 * Created by qixingchen on 15/9/3.
 * 私聊／系统消息
 */
public abstract class PriavteMessAbsViewHolder extends RecyclerView.ViewHolder {

    public ImageView groupFaceImageView;
    public TextView titleTextview, MessTextview, unreadTextview, lastTimeTextview;
    public View itemView;
    public Button acceptButton, refuseButton;

    public PriavteMessAbsViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;

    }

    /**
     * 将所有附加品可见度更改为Gone
     */
    public void setAllAdditionVisibilityGone() {
        if (groupFaceImageView != null) {
            groupFaceImageView.setVisibility(View.GONE);
        }
        if (titleTextview != null) {
            titleTextview.setVisibility(View.GONE);
        }
        if (MessTextview != null) {
            MessTextview.setVisibility(View.GONE);
        }
        if (acceptButton != null) {
            acceptButton.setVisibility(View.GONE);
            acceptButton.setOnClickListener(null);
        }
        if (refuseButton != null) {
            refuseButton.setVisibility(View.GONE);
            refuseButton.setOnClickListener(null);
        }
    }

    /**
     * 为界面元素赋值
     *
     * @param v            布局
     * @param itemViewType 消息来源类型
     *
     * @see com.tizi.quanzi.tool.StaticField.PrivateMessOrSysMess
     */
    public void findViewByID(View v, int itemViewType) {
        if (itemViewType == StaticField.PrivateMessOrSysMess.SysMess) {
            groupFaceImageView = (ImageView) itemView.findViewById(R.id.group_face_image_view);
            titleTextview = (TextView) itemView.findViewById(R.id.mess_title_text_view);
            MessTextview = (TextView) itemView.findViewById(R.id.mess_text_view);
            acceptButton = (Button) itemView.findViewById(R.id.accept_button);
            refuseButton = (Button) itemView.findViewById(R.id.refuse_button);
            //unreadCountTextview = (TextView) view.findViewById(R.id.unread_text_view);
            //lastTimeTextview = (TextView) view.findViewById(R.id.last_dyns_text_view);
        } else {
            // TODO: 15/9/3 set
        }

    }
}