package com.tizi.quanzi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.chat.GroupUserAdmin;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.ChatActivity;

import java.util.List;

/**
 * Created by qixingchen on 15/9/3.
 * 私信、系统消息列表
 */
public class PrivateMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PrivateMessPair> privateMessPairs;
    private Context context;
    private Onclick onclick;


    /**
     * @param privateMessPairs 私信列表
     * @param context          上下文
     * @param onclick          被点击时的回调
     */
    public PrivateMessageAdapter(List<PrivateMessPair> privateMessPairs, Context context, Onclick onclick) {
        this.privateMessPairs = privateMessPairs;
        PrivateMessPairList.getInstance().addOnChangeCallBack(new PrivateMessPairList.OnChangeCallBack() {
            @Override
            public void changed() {
                updateData();
            }
        });
        this.context = context;
        this.onclick = onclick;
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
        return privateMessPairs.get(position).Type;
    }

    /**
     * 从 {@link PrivateMessPairList}刷新数据
     */
    private void updateData() {
        this.privateMessPairs = PrivateMessPairList.getInstance().getGroupList();
        notifyDataSetChanged();
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
            case StaticField.PrivateMessOrSysMess.SysMess:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_system_mess, parent, false);
                vh = new SystemMessViewHolder(v);
                break;
            case StaticField.PrivateMessOrSysMess.PrivateMess:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_private_mess, parent, false);
                vh = new PrivateViewHolder(v);
                break;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_system_mess, parent, false);
                vh = new SystemMessViewHolder(v);
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
        if (SystemMessViewHolder.class.isInstance(holder)) {
            SystemMessViewHolder systemHolder = (SystemMessViewHolder) holder;

            systemHolder.setAllAdditionVisibilityGone();
            PrivateMessPair privateMessPair = privateMessPairs.get(position);
            if (privateMessPair.Type == StaticField.PrivateMessOrSysMess.SysMess) {
                final SystemMessage systemMessage = privateMessPair.systemMessage;
                if (systemMessage.getSys_msg_flag() == StaticField.SystemMessAttrName.systemFlag.invitation) {
                    systemHolder.titleTextview.setVisibility(View.VISIBLE);
                    systemHolder.MessTextview.setVisibility(View.VISIBLE);
                    systemHolder.titleTextview.setText("邀请您加入圈子");
                    systemHolder.MessTextview.setText(systemMessage.getContent());

                    if (systemMessage.getStatus() == StaticField.SystemMessAttrName.statueCode.notComplete) {

                        systemHolder.acceptButton.setVisibility(View.VISIBLE);
                        systemHolder.refuseButton.setVisibility(View.VISIBLE);

                        systemHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GroupUserAdmin.getInstance(context).
                                        acceptToJoinGroup(true, systemMessage.getConvid(), systemMessage.getGroup_id());
                                systemMessage.setStatus(StaticField.SystemMessAttrName.statueCode.complete);
                                systemMessage.setIsread(true);
                                DBAct.getInstance().addOrReplaceSysMess(systemMessage);
                                PrivateMessPairList.getInstance().updateGroup(PrivateMessPair.PriMessFromSystemMess(systemMessage));
                            }
                        });

                        systemHolder.refuseButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GroupUserAdmin.getInstance(context).
                                        acceptToJoinGroup(false, systemMessage.getConvid(), systemMessage.getGroup_id());
                                systemMessage.setStatus(StaticField.SystemMessAttrName.statueCode.complete);
                                systemMessage.setIsread(true);
                                DBAct.getInstance().addOrReplaceSysMess(systemMessage);
                                PrivateMessPairList.getInstance().updateGroup(PrivateMessPair.PriMessFromSystemMess(systemMessage));

                            }
                        });
                    }
                }
            }
        }

        /*私信*/
        if (PrivateViewHolder.class.isInstance(holder)) {
            final PrivateViewHolder privateVH = (PrivateViewHolder) holder;
            final PrivateMessPair privateMess = privateMessPairs.get(position);
            privateVH.mUserFaceImageNetworkImageView.setImageUrl(privateMess.Face,
                    GetVolley.getmInstance().getImageLoader());
            privateVH.mUserNameTextViewTextView.setText(privateMess.Name);
            privateVH.mMessTextViewTextView.setText(privateMess.lastMess);
            privateVH.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chat = new Intent(privateVH.view.getContext(), ChatActivity.class);
                    chat.putExtra("conversation", privateMess.convId);
                    chat.putExtra("chatType", StaticField.ConvType.twoPerson);
                    chat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    privateVH.view.getContext().startActivity(chat);
                }
            });
        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return privateMessPairs == null ? 0 : privateMessPairs.size();
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

    public static class SystemMessViewHolder extends RecyclerView.ViewHolder {

        private ImageView groupFaceImageView;
        private TextView titleTextview, MessTextview;
        private View itemView;
        private Button acceptButton, refuseButton;

        public SystemMessViewHolder(View itemView) {
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

    public static class PrivateViewHolder extends RecyclerView.ViewHolder {

        private NetworkImageView mUserFaceImageNetworkImageView;
        private TextView mUserNameTextViewTextView;
        private TextView mMessTextViewTextView;
        private View view;

        public PrivateViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            mUserFaceImageNetworkImageView = (NetworkImageView) itemView.findViewById(R.id.user_face_image);
            mUserNameTextViewTextView = (TextView) itemView.findViewById(R.id.user_name_text_view);
            mMessTextViewTextView = (TextView) itemView.findViewById(R.id.mess_text_view);
        }
    }

}
