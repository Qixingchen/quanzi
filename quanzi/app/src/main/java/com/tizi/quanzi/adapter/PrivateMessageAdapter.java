package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.chat.GroupUserAdmin;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.tool.StaticField;

import java.util.List;

/**
 * Created by qixingchen on 15/9/3.
 */
public class PrivateMessageAdapter extends RecyclerView.Adapter<PriavteMessAbsViewHolder> {

    private List<PrivateMessPair> privateMessPairs;
    private Context context;
    private Onclick onclick;


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
     * @return PriavteMessAbsViewHolder {@link PriavteMessAbsViewHolder}
     */
    @Override
    public PriavteMessAbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        PriavteMessAbsViewHolder vh;
        switch (viewType) {
            case StaticField.ChatFrom.OTHER:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.private_mess_item, parent, false);
                vh = new SystemMessViewHolder(v);
                break;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.private_mess_item, parent, false);
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
    public void onBindViewHolder(PriavteMessAbsViewHolder holder, int position) {
        holder.setAllAdditionVisibilityGone();
        PrivateMessPair privateMessPair = privateMessPairs.get(position);
        if (privateMessPair.Type == StaticField.PrivateMessOrSysMess.SysMess) {
            final SystemMessage systemMessage = DBAct.getInstance().quarySysMess(privateMessPair.MessID);
            if (systemMessage.getSys_msg_flag() == StaticField.SystemMessAttrName.systemFlag.invitation) {
                holder.titleTextview.setVisibility(View.VISIBLE);
                holder.MessTextview.setVisibility(View.VISIBLE);
                holder.titleTextview.setText("邀请您加入圈子");
                holder.MessTextview.setText(systemMessage.getContent());
                if (systemMessage.getStatus() == StaticField.SystemMessAttrName.statueCode.notComplete) {
                    holder.acceptButton.setVisibility(View.VISIBLE);
                    holder.refuseButton.setVisibility(View.VISIBLE);
                    holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GroupUserAdmin.getInstance(context).
                                    acceptToJoinGroup(true, systemMessage.getConvid(), systemMessage.getGroup_id());
                            systemMessage.setStatus(StaticField.SystemMessAttrName.statueCode.complete);
                            PrivateMessPairList.getInstance().updateGroup(PrivateMessPair.PriMessFromSystemMess(systemMessage));
                            DBAct.getInstance().addOrReplaceSysMess(systemMessage);
                        }
                    });
                    holder.refuseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GroupUserAdmin.getInstance(context).
                                    acceptToJoinGroup(false, systemMessage.getConvid(), systemMessage.getGroup_id());
                            systemMessage.setStatus(StaticField.SystemMessAttrName.statueCode.complete);
                            DBAct.getInstance().addOrReplaceSysMess(systemMessage);
                            PrivateMessPairList.getInstance().updateGroup(PrivateMessPair.PriMessFromSystemMess(systemMessage));

                        }
                    });
                }
            }
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

    public static class SystemMessViewHolder extends PriavteMessAbsViewHolder {

        public SystemMessViewHolder(View itemView) {
            super(itemView);
            findViewByID(itemView, StaticField.PrivateMessOrSysMess.SysMess);
        }
    }


}
