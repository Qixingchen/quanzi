package com.tizi.quanzi.adapter;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.network.FindUser;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.user_zone.UserZoneActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Yulan on 2015/11/29.
 * 点赞用户头像
 * 用于属于个人动态的DynItem
 */
public class AttitudesUsersFaceAdapter extends RecyclerViewAdapterAbs {

    private List<Dyns.DynsEntity.ZansEntity> zans;

    public AttitudesUsersFaceAdapter(List<Dyns.DynsEntity.ZansEntity> zans) {
        this.zans = zans;
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
                .inflate(R.layout.item_attitudes_face, parent, false);
        vh = new AttitudeUserFaceViewHolder(v);
        return vh;
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param viewHolder 被绑定的ViewHolder
     * @param position   列表位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (AttitudeUserFaceViewHolder.class.isInstance(viewHolder)) {
            final AttitudeUserFaceViewHolder holder = (AttitudeUserFaceViewHolder) viewHolder;
            Picasso.with(holder.userface.getContext()).load(zans.get(position).icon)
                    .fit()
                    .into(holder.userface);
            holder.userface.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FindUser.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                        @Override
                        public void onOK(Object ts) {
                            OtherUserInfo otherUserInfo = (OtherUserInfo) ts;
                            Intent otherUser = new Intent(holder.userface.getContext(), UserZoneActivity.class);
                            otherUser.putExtra(StaticField.IntentName.OtherUserInfo, (Parcelable) otherUserInfo);
                            holder.userface.getContext().startActivity(otherUser);
                        }

                        @Override
                        public void onError(String Message) {
                            Toast.makeText(holder.userface.getContext(), "此用户已不存在", Toast.LENGTH_LONG).show();
                        }
                    }).findUserByID(zans.get(position).userId);
                }
            });
        }
    }

    public void addZan(String userIcon, String userID) {
        zans.add(0, new Dyns.DynsEntity.ZansEntity(userIcon, userID));
        notifyItemInserted(0);
    }

    public void removeZan(String userID) {
        for (int i = 0; i < zans.size(); i++) {
            if (zans.get(i).userId.equals(userID)) {
                zans.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return zans.size();
    }

    class AttitudeUserFaceViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userface;

        public AttitudeUserFaceViewHolder(View itemView) {
            super(itemView);
            userface = (CircleImageView) itemView.findViewById(R.id.attitude_user_face);
        }
    }
}
