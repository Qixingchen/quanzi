package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.ContantUsers;

import java.util.List;

/**
 * Created by qixingchen on 15/9/18.
 * 邀请好友
 */
public class InviteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ContantUsers.MobilesEntity> users;
    private Context context;
    private OnAddUser onAddUser;

    public InviteListAdapter(List<ContantUsers.MobilesEntity> users, Context context, OnAddUser onAddUser) {
        this.users = users;
        this.context = context;
        this.onAddUser = onAddUser;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invite, parent, false);
        RecyclerView.ViewHolder vh = new AvailableUserViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (AvailableUserViewHolder.class.isInstance(holder)) {
            AvailableUserViewHolder vh = (AvailableUserViewHolder) holder;
            vh.userPhone.setText(users.get(position).mobile);
            vh.inviteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAddUser != null) {
                        onAddUser.add(users.get(position).userId);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class AvailableUserViewHolder extends RecyclerView.ViewHolder {

        private TextView userPhone;
        private Button inviteButton;
        private View view;

        public AvailableUserViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            findViews();
        }

        private void findViews() {
            userPhone = (TextView) view.findViewById(R.id.phonenumber);
            inviteButton = (Button) view.findViewById(R.id.invite_button);
        }
    }

    public interface OnAddUser {
        void add(String userID);
    }
}
