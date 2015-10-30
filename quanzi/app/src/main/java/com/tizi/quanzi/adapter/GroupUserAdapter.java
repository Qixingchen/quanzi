package com.tizi.quanzi.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import rx.functions.Action1;

/**
 * Created by qixingchen on 15/9/2.
 * 圈子用户
 */
public class GroupUserAdapter extends RecyclerView.Adapter<GroupUserAdapter.GroupUserViewHolder> {

    private static final String TAG = GroupUserAdapter.class.getSimpleName();
    private Context mContext;
    private List<GroupAllInfo.MemlistEntity> memlist;
    private boolean isCreater;
    private String groupID;

    public GroupUserAdapter(Context mContext, @Nullable List<GroupAllInfo.MemlistEntity> memlist,
                            boolean isCreater, String groupID) {
        this.mContext = mContext;
        this.memlist = memlist;
        this.isCreater = isCreater;
        this.groupID = groupID;
        BusProvider.getInstance().register(this);
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
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
            holder.weibo_avatar_NetworkImageView.setDefaultImageResId(R.drawable.face);
            holder.weibo_avatar_NetworkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addUser(groupID, false);
                }
            });

        } else if (position < memlist.size()) {
            final GroupAllInfo.MemlistEntity mem = memlist.get(position);
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
                                otherUser.putExtra(StaticField.IntentName.OtherUserInfo, otherUserInfo);
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
        return memlist == null ? 1 : memlist.size() + 1;
    }

    /**
     * 设置memlist
     */
    public void setMemlist(List<GroupAllInfo.MemlistEntity> memlist) {
        this.memlist = memlist;
        notifyDataSetChanged();
    }

    /**
     * 设置当前用户是否是创建者
     */
    public void setIsCreater(boolean isCreater) {
        this.isCreater = isCreater;
        notifyDataSetChanged();
    }

    /**
     * 邀请用户
     *
     * @param groupid 当前组ID
     */
    private void addUser(final String groupid, boolean dontHavePermissionButIgnore) {

        final Activity activity = AppStaticValue.getActivity(QuanziZoneActivity.class.getSimpleName());
        if (!dontHavePermissionButIgnore && ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            int code = StaticField.PermissionRequestCode.addContactUsersInQuanziZone;
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, code);
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

        if (dontHavePermissionButIgnore) {
            InviteListAdapter inviteListAdapter = new InviteListAdapter(null, activity, onAddUser);
            phoneList.setAdapter(inviteListAdapter);
            builder.setView(layout).show();
            return;
        }

        FindUser.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                ContantUsers users = (ContantUsers) ts;
                if (users.mobiles.size() == 0) {
                    Toast.makeText(mContext, "没有好友", Toast.LENGTH_LONG).show();
                }
                InviteListAdapter inviteListAdapter = new InviteListAdapter(users.mobiles, activity, onAddUser);
                phoneList.setAdapter(inviteListAdapter);
                builder.setView(layout).show();
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
                                        memlist.remove(postion);
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
