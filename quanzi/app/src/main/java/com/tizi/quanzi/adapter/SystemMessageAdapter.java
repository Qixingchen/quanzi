package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.chat.GroupUserAdmin;
import com.tizi.quanzi.dataStatic.ConvGroupAbsList;
import com.tizi.quanzi.dataStatic.SystemMessageList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.model.SystemMessagePair;
import com.tizi.quanzi.tool.StaticField;

import java.util.List;

/**
 * Created by qixingchen on 15/10/22.
 * 系统消息适配器
 */
public class SystemMessageAdapter extends RecyclerViewAdapterAbs {

    private List<SystemMessagePair> systemMessagePairs;
    private Context mContext;
    private Onclick onclick;

    public SystemMessageAdapter(List<SystemMessagePair> systemMessagePairs, Context mContext, Onclick onclick) {
        this.systemMessagePairs = systemMessagePairs;
        this.mContext = mContext;
        this.onclick = onclick;
        SystemMessageList.getInstance().addOnChangeCallBack(new ConvGroupAbsList.OnChangeCallBack() {
            @Override
            public void changed() {
                notifyDataSetChanged();
            }
        });
    }


    /**
     * 判定类型
     *
     * @param position 判定位置
     *
     * @return 类型 {@link com.tizi.quanzi.tool.StaticField.PrivateMessOrSysMess}
     */
    @Override
    public int getItemViewType(int position) {
        return systemMessagePairs.get(position).systemMessage.sys_msg_flag;
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
        switch (viewType) {
            case StaticField.SystemMessAttrName.systemFlag.invitation:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_system_mess, parent, false);
                vh = new GroupInviteViewHolder(v);
                break;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_system_mess, parent, false);
                vh = new GroupInviteViewHolder(v);
        }
        return vh;

    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        /*系统消息*/
        if (GroupInviteViewHolder.class.isInstance(holder)) {
            GroupInviteViewHolder systemHolder = (GroupInviteViewHolder) holder;

            systemHolder.setAllAdditionVisibilityGone();
            SystemMessagePair systemMessagePair = systemMessagePairs.get(position);

            final SystemMessage systemMessage = systemMessagePair.systemMessage;
            if (systemMessage.sys_msg_flag == StaticField.SystemMessAttrName.systemFlag.invitation) {
                systemHolder.titleTextview.setVisibility(View.VISIBLE);
                systemHolder.MessTextview.setVisibility(View.VISIBLE);
                systemHolder.titleTextview.setText("邀请您加入圈子");
                systemHolder.MessTextview.setText(systemMessage.getContent());

                if (systemMessage.status == StaticField.SystemMessAttrName.statueCode.notComplete) {

                    systemHolder.acceptButton.setVisibility(View.VISIBLE);
                    systemHolder.refuseButton.setVisibility(View.VISIBLE);

                    systemHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GroupUserAdmin.getInstance(mContext).
                                    acceptToJoinGroup(true, systemMessage.getConvid(), systemMessage.getGroup_id());
                            systemMessage.setStatus(StaticField.SystemMessAttrName.statueCode.complete);
                            systemMessage.setIsread(true);
                            DBAct.getInstance().addOrReplaceSysMess(systemMessage);
                            SystemMessageList.getInstance().updateGroup(
                                    SystemMessagePair.SysMessPairFromSystemMess(systemMessage));
                        }
                    });

                    systemHolder.refuseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GroupUserAdmin.getInstance(mContext).
                                    acceptToJoinGroup(false, systemMessage.getConvid(), systemMessage.getGroup_id());
                            systemMessage.setStatus(StaticField.SystemMessAttrName.statueCode.complete);
                            systemMessage.setIsread(true);
                            DBAct.getInstance().addOrReplaceSysMess(systemMessage);
                            SystemMessageList.getInstance().updateGroup(
                                    SystemMessagePair.SysMessPairFromSystemMess(systemMessage));

                        }
                    });
                }
            }
            systemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onclick != null) {
                        onclick.systemMessClick(systemMessage);
                    } else {
                        Log.w(TAG, "系统消息被点击,但是没有 onclick 回调");
                    }
                }
            });


        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return systemMessagePairs == null ? 0 : systemMessagePairs.size();
    }


    /**
     * 点击接口
     */
    public interface Onclick {
        /**
         * 项目被点击
         *
         * @param systemMessage 被点击的系统消息
         */
        void systemMessClick(SystemMessage systemMessage);
    }

    static class GroupInviteViewHolder extends RecyclerView.ViewHolder {

        private ImageView groupFaceImageView;
        private TextView titleTextview, MessTextview;
        private View itemView;
        private Button acceptButton, refuseButton;

        public GroupInviteViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            findViewByID(itemView);
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
         * @param v 布局
         */
        public void findViewByID(View v) {
            groupFaceImageView = (ImageView) v.findViewById(R.id.group_face_image_view);
            titleTextview = (TextView) v.findViewById(R.id.mess_title_text_view);
            MessTextview = (TextView) v.findViewById(R.id.mess_text_view);
            acceptButton = (Button) v.findViewById(R.id.accept_button);
            refuseButton = (Button) v.findViewById(R.id.refuse_button);
        }

    }
}
