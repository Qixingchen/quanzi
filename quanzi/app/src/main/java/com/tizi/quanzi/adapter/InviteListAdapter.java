package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.ContantUsers;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.network.FindUser;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.tool.GetShareIntent;

import java.util.List;

/**
 * Created by qixingchen on 15/9/18.
 * 邀请好友
 */
public class InviteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int USER_VIEW = 1, SHARE_VIEW = 2, MANU_ADD = 3;
    private List<ContantUsers.MobilesEntity> users;
    private Context context;
    private OnAddUser onAddUser;

    public InviteListAdapter(List<ContantUsers.MobilesEntity> users, Context context, OnAddUser onAddUser) {
        this.users = users;
        this.context = context;
        this.onAddUser = onAddUser;
    }

    @Override
    public int getItemViewType(int position) {
        if (users == null) {
            if (position == 0) {
                return SHARE_VIEW;
            } else {
                return MANU_ADD;
            }
        }

        if (position == users.size()) {
            return SHARE_VIEW;
        } else if (position == users.size() + 1) {
            return MANU_ADD;
        } else {
            return USER_VIEW;
        }
    }

    public void addUser(List<ContantUsers.MobilesEntity> users) {
        if (this.users == null) {
            this.users = users;
        } else {
            this.users.addAll(users);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder vh = null;

        switch (viewType) {

            case USER_VIEW:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_invite, parent, false);
                vh = new AvailableUserViewHolder(v);
                return vh;

            case SHARE_VIEW:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_share, parent, false);
                vh = new ShareViewHolder(v) {
                };
                return vh;

            case MANU_ADD:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_manu_add, parent, false);
                vh = new ManuAddViewHolder(v) {
                };

            default:
                return vh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder == null) {
            return;
        }
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
        if (ShareViewHolder.class.isInstance(holder)) {
            ShareViewHolder vh = (ShareViewHolder) holder;
            vh.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetShareIntent.startShare(context);
                }
            });
        }
        if (ManuAddViewHolder.class.isInstance(holder)) {
            final ManuAddViewHolder manuAddViewHolder = (ManuAddViewHolder) holder;
            manuAddViewHolder.ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FindUser.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                        @Override
                        public void onOK(Object ts) {
                            OtherUserInfo otherUserInfo = (OtherUserInfo) ts;
                            if (onAddUser != null) {
                                onAddUser.add(otherUserInfo.id);
                            }
                        }

                        @Override
                        public void onError(String Message) {
                            Toast.makeText(context, Message, Toast.LENGTH_LONG).show();
                        }
                    }).finduserByAccount(manuAddViewHolder.phone.getText().toString());

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users == null ? 2 : users.size() + 2;
    }

    public interface OnAddUser {
        void add(String userID);
    }

    static class ShareViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public ShareViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    static class ManuAddViewHolder extends RecyclerView.ViewHolder {
        private EditText phone;
        private Button ok;

        public ManuAddViewHolder(View itemView) {
            super(itemView);
            phone = (EditText) itemView.findViewById(R.id.phonenumber);
            ok = (Button) itemView.findViewById(R.id.ok);
        }
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
}
