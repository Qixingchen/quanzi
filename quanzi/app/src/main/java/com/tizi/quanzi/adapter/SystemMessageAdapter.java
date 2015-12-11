package com.tizi.quanzi.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.chat.GroupUserAdmin;
import com.tizi.quanzi.dataStatic.SystemMessageList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.model.SystemMessagePair;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.dyns.DynsActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by qixingchen on 15/10/22.
 * 系统消息适配器
 */
public class SystemMessageAdapter extends RecyclerViewAdapterAbs {

    public final static int DELETE_ALL = -1;
    private SortedList<SystemMessagePair> systemMessagePairs = new SortedList<>(SystemMessagePair.class, new SortedList.Callback<SystemMessagePair>() {
        @Override
        public int compare(SystemMessagePair o1, SystemMessagePair o2) {
            if (!o1.systemMessage.isread && !o2.systemMessage.isread) {
                return (int) (o2.lastMessTime / 1000L - o1.lastMessTime / 1000L);
            }
            if (!o1.systemMessage.isread) {
                return -1;
            }
            if (!o2.systemMessage.isread) {
                return 1;
            }
            return (int) (o2.lastMessTime / 1000L - o1.lastMessTime / 1000L);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(SystemMessagePair oldItem, SystemMessagePair newItem) {
            return oldItem.systemMessage.equals(newItem.systemMessage);
        }

        @Override
        public boolean areItemsTheSame(SystemMessagePair item1, SystemMessagePair item2) {
            return item1.ID.equals(item2.ID);
        }
    });
    private Context mContext;
    private Onclick onclick;

    public SystemMessageAdapter(List<SystemMessagePair> systemMessagePairs, Context mContext, Onclick onclick) {
        this.systemMessagePairs.beginBatchedUpdates();
        this.systemMessagePairs.addAll(systemMessagePairs);
        this.systemMessagePairs.endBatchedUpdates();
        this.mContext = mContext;
        this.onclick = onclick;
        try {
            BusProvider.getInstance().register(this);
        } catch (IllegalStateException ignore) {
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            BusProvider.getInstance().unregister(this);
        } catch (IllegalStateException ignore) {
        }
    }

    @Subscribe
    public void onChanged(SystemMessageList list) {
        this.systemMessagePairs.beginBatchedUpdates();
        this.systemMessagePairs.addAll(SystemMessageList.getInstance().getGroupList());
        this.systemMessagePairs.endBatchedUpdates();
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
        if (position == 0) {
            return DELETE_ALL;
        }
        return systemMessagePairs.get(position - 1).systemMessage.sys_msg_flag;
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
            case DELETE_ALL:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_delete_all_message, parent, false);
                vh = new DeleteAllViewHolder(v);
                break;

            case StaticField.SystemMessAttrName.systemFlag.invitation:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_system_mess, parent, false);
                vh = new GroupInviteViewHolder(v);
                break;

            case StaticField.SystemMessAttrName.systemFlag.dyn_comment:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_dyn_notify, parent, false);
                vh = new DynNotifyViewHolder(v);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (DeleteAllViewHolder.class.isInstance(holder)) {
            final DeleteAllViewHolder vh = (DeleteAllViewHolder) holder;
            vh.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(vh.view.getContext()).setTitle("确认清空系统消息么")
                            .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DBAct.getInstance().deleteAllSystemMessage();
                                    SystemMessageList.getInstance().removeAllUnreadMess();
                                    systemMessagePairs.clear();
                                    notifyDataSetChanged();
                                }
                            }).setNegativeButton("取消", null).show();
                }
            });
            return;
        }

        SystemMessagePair systemMessagePair = systemMessagePairs.get(position - 1);
        final SystemMessage systemMessage = systemMessagePair.systemMessage;
        if (!systemMessage.isread) {
            systemMessage.isread = true;
            SystemMessageList.getInstance().removeUnreadMess(systemMessage.id);
            DBAct.getInstance().addOrReplaceSysMess(systemMessage);
        }

        /*系统消息*/
        if (GroupInviteViewHolder.class.isInstance(holder)) {
            GroupInviteViewHolder systemHolder = (GroupInviteViewHolder) holder;

            systemHolder.setAllAdditionVisibilityGone();

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
                            GroupUserAdmin.getInstance(mContext).setOnResult(new GroupUserAdmin.OnResult() {
                                @Override
                                public void OK() {

                                }

                                @Override
                                public void error(String errorMessage) {
                                    Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }).
                                    acceptToJoinGroup(true, systemMessage.getConvid(), systemMessage.getGroup_id());
                            systemMessage.setStatus(StaticField.SystemMessAttrName.statueCode.complete);
                            systemMessage.setIsread(true);
                            DBAct.getInstance().addOrReplaceSysMess(systemMessage);
                            SystemMessageList.getInstance().updateGroup(
                                    SystemMessagePair.SysMessPairFromSystemMess(systemMessage));
                            notifyItemChanged(position);
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
                            notifyItemChanged(position);
                        }
                    });
                }
            } else {
                systemHolder.MessTextview.setVisibility(View.VISIBLE);
                systemHolder.MessTextview.setText(systemMessage.getContent());
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
            if (systemMessagePair.getUnreadCount() != 0) {
                systemHolder.MessTextview.setTypeface(Typeface.DEFAULT_BOLD);
                systemHolder.MessTextview.setTextColor(mContext.getResources().getColor(R.color.md_black));
                systemHolder.titleTextview.setTypeface(Typeface.DEFAULT_BOLD);
                systemHolder.titleTextview.setTextColor(mContext.getResources().getColor(R.color.md_black));
            } else {
                systemHolder.MessTextview.setTypeface(Typeface.DEFAULT);
                systemHolder.MessTextview.setTextColor(mContext.getResources().getColor(R.color.md_grey_600));
                systemHolder.titleTextview.setTypeface(Typeface.DEFAULT);
                systemHolder.titleTextview.setTextColor(mContext.getResources().getColor(R.color.md_grey_600));
            }
        }

        /*动态评论*/
        if (DynNotifyViewHolder.class.isInstance(holder)) {
            DynNotifyViewHolder dynNotifyViewHolder = (DynNotifyViewHolder) holder;
            dynNotifyViewHolder.old_weibo_name.setText(systemMessage.dyn_create_username);
            dynNotifyViewHolder.weiboName.setText(systemMessage.user_name);
            dynNotifyViewHolder.weibo_content.setText(systemMessage.content);
            dynNotifyViewHolder.weibo_date.setText(FriendTime.FriendlyDate(systemMessage.create_time));
            dynNotifyViewHolder.old_weibo_content.setText(systemMessage.dyn_content);
            if (systemMessage.reply_comment == null || systemMessage.reply_comment.compareTo("") == 0) {
                dynNotifyViewHolder.reply_content.setVisibility(View.GONE);
            } else {
                dynNotifyViewHolder.reply_content.setText(systemMessage.reply_comment);
                dynNotifyViewHolder.reply_content.setVisibility(View.VISIBLE);
            }

            Picasso.with(mContext).load(systemMessage.user_icon)
                    .fit()
                    .into(dynNotifyViewHolder.weiboUser);

            Picasso.with(mContext).load(systemMessage.dyn_icon)
                    .fit()
                    .into(dynNotifyViewHolder.oldWeiboUser);

            if (systemMessagePair.getUnreadCount() != 0) {
                dynNotifyViewHolder.weiboName.setTypeface(Typeface.DEFAULT_BOLD);
                dynNotifyViewHolder.weiboName.setTextColor(mContext.getResources().getColor(R.color.md_black));
                dynNotifyViewHolder.weibo_content.setTypeface(Typeface.DEFAULT_BOLD);
                dynNotifyViewHolder.weibo_content.setTextColor(mContext.getResources().getColor(R.color.md_black));
            } else {
                dynNotifyViewHolder.weiboName.setTypeface(Typeface.DEFAULT);
                dynNotifyViewHolder.weiboName.setTextColor(mContext.getResources().getColor(R.color.md_grey_600));
                dynNotifyViewHolder.weibo_content.setTypeface(Typeface.DEFAULT);
                dynNotifyViewHolder.weibo_content.setTextColor(mContext.getResources().getColor(R.color.md_grey_600));
            }
            dynNotifyViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dyn = new Intent(mContext, DynsActivity.class);
                    dyn.putExtra("dynID", systemMessage.dynid);
                    dyn.putExtra("isUser", !systemMessage.dyn_create_userid.equals(""));
                    mContext.startActivity(dyn);
                }
            });
        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        if (systemMessagePairs == null) {
            return 0;
        }
        return systemMessagePairs.size() == 0 ? 0 : systemMessagePairs.size() + 1;
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

    static class DynNotifyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView weiboUser;
        private ImageView oldWeiboUser;
        private TextView weiboName, weibo_date, weibo_from, weibo_content, old_weibo_content,
                old_weibo_name, reply_content;
        private View itemView;

        public DynNotifyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            findViewByID(itemView);
        }

        /**
         * 为界面元素赋值
         *
         * @param v 布局
         */
        private void findViewByID(View v) {
            weiboUser = (CircleImageView) v.findViewById(R.id.weibo_avatar);
            oldWeiboUser = (ImageView) v.findViewById(R.id.old_weibo_avatar);

            weiboName = (TextView) v.findViewById(R.id.weibo_name);
            weibo_date = (TextView) v.findViewById(R.id.weibo_date);
            weibo_from = (TextView) v.findViewById(R.id.weibo_from);
            weibo_content = (TextView) v.findViewById(R.id.weibo_content);
            old_weibo_content = (TextView) v.findViewById(R.id.old_weibo_content);
            old_weibo_name = (TextView) v.findViewById(R.id.old_weibo_name);
            reply_content = (TextView) v.findViewById(R.id.reply_content);
        }

    }

    static class DeleteAllViewHolder extends RecyclerView.ViewHolder {

        private View view;

        public DeleteAllViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(itemView.getContext()).setTitle("确认清空系统消息么")
                            .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DBAct.getInstance().deleteAllSystemMessage();
                                    SystemMessageList.getInstance().removeAllUnreadMess();
                                }
                            }).setNegativeButton("取消", null).show();
                }
            });
        }
    }
}
