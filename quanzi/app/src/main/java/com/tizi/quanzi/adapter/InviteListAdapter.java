package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.gson.ContantUsers;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.network.FindUser;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.tool.GetShareIntent;
import com.tizi.quanzi.tool.ReadContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/9/18.
 * 邀请好友
 */
public class InviteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int USER_VIEW = 1, SHARE_VIEW = 2, MANU_ADD = 3;
    List<String> nowUsers = new ArrayList<>();
    private List<ContantUsers.MobilesEntity> users;
    private List<ContantUsers.MobilesEntity> backUpUsers;
    private Context context;
    private OnAddUser onAddUser;

    public InviteListAdapter(List<ContantUsers.MobilesEntity> users, List<String> nowUsers, Context context, OnAddUser onAddUser) {
        this.users = users;
        this.backUpUsers = users;
        this.nowUsers = nowUsers;
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

        if (position == 0) {
            return MANU_ADD;
        } else if (position == users.size() + 1) {
            return SHARE_VIEW;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        if (AvailableUserViewHolder.class.isInstance(holder)) {
            final AvailableUserViewHolder vh = (AvailableUserViewHolder) holder;
            final ContantUsers.MobilesEntity mobile = users.get(position - 1);
            vh.userPhone.setText(mobile.mobile);
            vh.userName.setText(String.format("%s名字: %s",
                    App.getApplication().getString(R.string.app_name), mobile.userName));
            vh.contactUserName.setText(ReadContact.findName(mobile.mobile));
            Picasso.with(vh.userFace.getContext()).load(mobile.icon)
                    .resizeDimen(R.dimen.invite_user_face, R.dimen.invite_user_face)
                    .into(vh.userFace);

            vh.setNotAdded();
            for (String id : nowUsers) {
                if (mobile.userId.equals(id)) {
                    vh.setAdded();
                    break;
                }
            }

            vh.inviteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAddUser != null) {
                        onAddUser.add(mobile.userId);
                    }
                    nowUsers.add(mobile.userId);
                    vh.setAdded();
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
            final ManuAddViewHolder vh = (ManuAddViewHolder) holder;

            vh.phone.requestFocus();

            vh.phone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().equals("")) {
                        users = backUpUsers;
                        notifyDataSetChanged();
                        return;
                    }
                    selectUsers(s.toString());
                }
            });

            vh.ok.setOnClickListener(new View.OnClickListener() {
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
                    }).finduserByAccount(vh.phone.getText().toString());

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users == null ? 2 : users.size() + 2;
    }

    private void selectUsers(String input) {
        List<ContantUsers.MobilesEntity> selectUsers = new ArrayList<>();
        for (ContantUsers.MobilesEntity mobiles : backUpUsers) {
            if (mobiles.mobile.contains(input)) {
                selectUsers.add(mobiles);
            }
        }
        users = selectUsers;
        notifyDataSetChanged();
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
        private TextView contactUserName, userName;
        private ImageView userFace;
        private View view;

        public AvailableUserViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            findViews();
        }

        private void findViews() {
            userPhone = (TextView) view.findViewById(R.id.phonenumber);
            inviteButton = (Button) view.findViewById(R.id.invite_button);
            contactUserName = (TextView) view.findViewById(R.id.contact_user_name);
            userName = (TextView) view.findViewById(R.id.user_name_text_view);
            userFace = (ImageView) view.findViewById(R.id.user_face_image);
        }

        private void setAdded() {
            inviteButton.setEnabled(false);
            inviteButton.setText("已添加");
        }

        private void setNotAdded() {
            inviteButton.setEnabled(true);
            inviteButton.setText("添加");
        }
    }
}
