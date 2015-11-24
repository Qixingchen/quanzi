package com.tizi.quanzi.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.chat.GroupUserAdmin;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.gson.ContantUsers;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.FindUser;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.otto.PermissionAnser;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.quanzi_zone.QuanziZoneActivity;
import com.tizi.quanzi.ui.user_zone.UserZoneActivity;
import com.tizi.quanzi.widget.AskForContact;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by qixingchen on 15/9/2.
 * 圈子用户
 */
public class GroupUserAdapter extends RecyclerView.Adapter<GroupUserAdapter.GroupUserViewHolder> {

    private static final String TAG = GroupUserAdapter.class.getSimpleName();
    private Context mContext;
    private SortedList<GroupAllInfo.MemberEntity> memlist = new SortedList<>(GroupAllInfo.MemberEntity.class,
            new SortedList.Callback<GroupAllInfo.MemberEntity>() {
                @Override
                public int compare(GroupAllInfo.MemberEntity o1, GroupAllInfo.MemberEntity o2) {
                    if (o1.id.equals(createrID)) {
                        return -1;
                    }
                    if (o2.id.equals(createrID)) {
                        return 1;
                    }
                    return 0;
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
                public boolean areContentsTheSame(GroupAllInfo.MemberEntity oldItem, GroupAllInfo.MemberEntity newItem) {
                    return oldItem.name.equals(newItem.name);
                }

                @Override
                public boolean areItemsTheSame(GroupAllInfo.MemberEntity item1, GroupAllInfo.MemberEntity item2) {
                    return item1.id.equals(item2.id);
                }
            });
    private boolean isCreater, isMember;
    private String groupID, createrID;

    public GroupUserAdapter(Context mContext, @Nullable List<GroupAllInfo.MemberEntity> memlist,
                            boolean isCreater, String createrID, String groupID) {
        this.mContext = mContext;
        if (memlist != null) {
            this.memlist.beginBatchedUpdates();
            this.memlist.addAll(memlist);
            this.memlist.endBatchedUpdates();
        }
        this.createrID = createrID;
        this.isCreater = isCreater;
        this.groupID = groupID;
        try {
            BusProvider.getInstance().register(this);
        } catch (Exception ignore) {
        }

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            BusProvider.getInstance().unregister(this);
        } catch (Exception ignore) {
        }
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public void setCreaterID(String createrID) {
        this.createrID = createrID;
    }

    /**
     * @param parent   需要创建ViewHolder的 ViewGroup
     * @param viewType 样式类型
     *
     * @return ViewHolder
     */
    @Override
    public GroupUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weibo_pic, parent, false);
        GroupUserViewHolder vh = new GroupUserViewHolder(v);
        return vh;
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(GroupUserViewHolder holder, final int position) {
        holder.weibo_avatar_NetworkImageView.setOnLongClickListener(null);
        holder.weibo_avatar_NetworkImageView.setOnClickListener(null);
        if (memlist == null || position == memlist.size()) {
            holder.weibo_avatar_NetworkImageView.setDefaultImageResId(R.drawable.ic_add_24dp);
            holder.weibo_avatar_NetworkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addUser(groupID, false);
                }
            });

        } else if (position < memlist.size()) {
            final GroupAllInfo.MemberEntity mem = memlist.get(position);
            holder.weibo_avatar_NetworkImageView.setImageUrl(mem.icon,
                    GetVolley.getmInstance().getImageLoader());
            //自己是群主并且点击的不是自己：长按删除
            if (isCreater && mem.id.compareTo(MyUserInfo.getInstance().getUserInfo().getId()) != 0) {
                holder.weibo_avatar_NetworkImageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        deleteUser(mem.name, mem.id, groupID, position);
                        return false;
                    }
                });
            }
            //点击的是别人：进入空间
            if (mem.id.compareTo(MyUserInfo.getInstance().getUserInfo().getId()) != 0) {
                holder.weibo_avatar_NetworkImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FindUser.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                            @Override
                            public void onOK(Object ts) {
                                OtherUserInfo otherUserInfo = (OtherUserInfo) ts;
                                Intent otherUser = new Intent(mContext, UserZoneActivity.class);
                                otherUser.putExtra(StaticField.IntentName.OtherUserInfo, (Parcelable) otherUserInfo);
                                mContext.startActivity(otherUser);
                            }

                            @Override
                            public void onError(String Message) {
                                Toast.makeText(mContext, "此用户已不存在", Toast.LENGTH_LONG).show();
                            }
                        }).findUserByID(mem.id);
                    }
                });
            }

        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        int addtion = isMember ? 1 : 0;
        addtion = memlist != null && memlist.size() < 10 ? addtion : 0;
        return memlist == null ? addtion : memlist.size() + addtion;
    }

    /**
     * 设置memlist
     */
    public void setMemlist(List<GroupAllInfo.MemberEntity> memlist) {
        this.memlist.beginBatchedUpdates();
        this.memlist.clear();
        this.memlist.addAll(memlist);
        this.memlist.endBatchedUpdates();
    }

    /**
     * 设置当前用户是否是创建者
     */
    public void setIsCreater(boolean isCreater) {
        this.isCreater = isCreater;
        notifyDataSetChanged();
    }

    /**
     * 设置当前用户是否是成员
     */
    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
        notifyDataSetChanged();
    }

    /**
     * 邀请用户
     *
     * @param groupid 当前组ID
     */
    private void addUser(final String groupid, boolean notHavePermissionButIgnore) {

        final Activity activity = AppStaticValue.getActivity(QuanziZoneActivity.class.getSimpleName());
        if (!notHavePermissionButIgnore && ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            AskForContact.getNewInstance(new AskForContact.OnResult() {
                @Override
                public void deny() {
                    addUser(groupid, true);
                }
            }).AskForContact(activity, StaticField.PermissionRequestCode.addContactUsersInQuanziZone);
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_invite_list,
                (ViewGroup) activity.findViewById(R.id.dialog_invite_list));

        final RecyclerView phoneList = (RecyclerView) layout.findViewById(R.id.invite_item_recycler_view);
        phoneList.setLayoutManager(new LinearLayoutManager(activity));

        final InviteListAdapter.OnAddUser onAddUser = new InviteListAdapter.OnAddUser() {
            @Override
            public void add(String userID) {
                final String convID = GroupList.getInstance().getGroup(groupid).convId;

                GroupUserAdmin.getInstance(mContext)
                        .setOnResult(new GroupUserAdmin.OnResult() {
                            @Override
                            public void OK() {
                                Toast.makeText(mContext, "添加请求发送成功", Toast.LENGTH_LONG).show();
                                Log.i(TAG, "添加请求发送成功");
                            }

                            @Override
                            public void error(String errorMessage) {
                                Toast.makeText(mContext, "添加失败" + errorMessage
                                        , Toast.LENGTH_LONG).show();
                                Log.w(TAG, "添加失败" + errorMessage);
                            }
                        })
                        .addMember(convID, groupid, userID);
            }
        };


        List<String> nowUsers = new ArrayList<>();
        for (int i = 0; i < memlist.size(); i++) {
            nowUsers.add(memlist.get(i).id);
        }

        final InviteListAdapter inviteListAdapter = new InviteListAdapter(null, nowUsers, activity, onAddUser);
        phoneList.setAdapter(inviteListAdapter);
        AlertDialog alertDialog;
        alertDialog = builder.setView(layout).create();
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        if (notHavePermissionButIgnore) {
            return;
        }

        FindUser.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                ContantUsers users = (ContantUsers) ts;
                if (users.mobiles.size() == 0) {
                    Toast.makeText(mContext, "没有好友", Toast.LENGTH_LONG).show();
                    return;
                }
                inviteListAdapter.addUser(users.mobiles);
            }

            @Override
            public void onError(String Message) {

            }
        }).findContactUsers();
    }

    /**
     * 删除用户
     *
     * @param UserName 将被删除的用户的名字
     * @param userID   将被删除的用户ID
     * @param groupID  当前组ID
     * @param postion  将被删除的用户目前的位置
     */
    private void deleteUser(String UserName, final String userID, final String groupID, final int postion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("删除好友").setMessage("确认删除好友: " + UserName + " 么？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ConvID = GroupList.getInstance().getGroup(groupID).convId;
                        GroupUserAdmin.getInstance(mContext).deleteMemberByRX(ConvID, groupID, userID)
                                .subscribe(new Action1<Object>() {
                                    @Override
                                    public void call(Object o) {
                                        memlist.removeItemAt(postion);
                                        notifyItemRemoved(postion);
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }).setNegativeButton("取消", null).show();
    }

    @Subscribe
    public void onpermissionAns(PermissionAnser permissionAnser) {
        if (permissionAnser.requestCode == StaticField.PermissionRequestCode.addContactUsersInQuanziZone) {
            if (permissionAnser.allGreen) {
                addUser(groupID, false);
            } else {
                addUser(groupID, true);
            }
        }

    }

    /**
     * 圈子用户的ViewHolder
     */
    public static class GroupUserViewHolder extends RecyclerView.ViewHolder {

        //界面元素
        private NetworkImageView weibo_avatar_NetworkImageView;

        public GroupUserViewHolder(View v) {
            super(v);
            FindViewByID(v);
        }

        /**
         * 为界面元素赋值
         *
         * @param v 布局
         */
        private void FindViewByID(View v) {
            weibo_avatar_NetworkImageView = (NetworkImageView) v.findViewById(R.id.pic);
        }
    }
}
