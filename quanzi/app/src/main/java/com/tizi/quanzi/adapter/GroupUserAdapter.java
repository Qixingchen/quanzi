package com.tizi.quanzi.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.chat.GroupUserAdmin;
import com.tizi.quanzi.chat.SendMessage;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.gson.GroupUserInfo;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.FindUser;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.network.UserManageInGroup;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.QuanziZone.QuanziZoneActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qixingchen on 15/9/2.
 * 圈子用户
 */
public class GroupUserAdapter extends RecyclerView.Adapter<GroupUserAdapter.GroupUserViewHolder> {

    private Context mContext;
    private List<GroupUserInfo.MemlistEntity> memlist;
    private boolean isCreater;
    private String groupID;
    private static final String TAG = GroupUserAdapter.class.getSimpleName();

    public GroupUserAdapter(Context mContext, @Nullable List<GroupUserInfo.MemlistEntity> memlist, boolean isCreater) {
        this.mContext = mContext;
        this.memlist = memlist;
        this.isCreater = isCreater;
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
    public void onBindViewHolder(GroupUserViewHolder holder, int position) {
        holder.weibo_avatar_NetworkImageView.setOnLongClickListener(null);
        holder.weibo_avatar_NetworkImageView.setOnClickListener(null);
        if (memlist == null || position == memlist.size()) {
            holder.weibo_avatar_NetworkImageView.setDefaultImageResId(R.drawable.face);
            holder.weibo_avatar_NetworkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addUser(groupID);
                }
            });

        } else if (position < memlist.size()) {
            final GroupUserInfo.MemlistEntity mem = memlist.get(position);
            holder.weibo_avatar_NetworkImageView.setImageUrl(mem.icon,
                    GetVolley.getmInstance(mContext).getImageLoader());
            if (isCreater) {
                holder.weibo_avatar_NetworkImageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        deleteUser(mem.name, mem.id, groupID);
                        return false;
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
        if (memlist == null) {
            return 1;
        } else {
            return memlist.size() + 1;
        }
    }

    /**
     * 设置memlist
     */
    public void setMemlist(List<GroupUserInfo.MemlistEntity> memlist) {
        this.memlist = memlist;
        notifyDataSetChanged();
    }

    public void setIsCreater(boolean isCreater) {
        this.isCreater = isCreater;
        notifyDataSetChanged();
    }

    private void addUser(final String groupid) {
        final Activity activity = App.getActivity(QuanziZoneActivity.class.getSimpleName());
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.one_line_dialog,
                (ViewGroup) activity.findViewById(R.id.one_line_dialog));
        final EditText userid = (EditText) layout.findViewById(R.id.dialog_edit_text);
        TextView title = (TextView) layout.findViewById(R.id.dialog_title);
        title.setText("输入好友的账号");
        userid.setHint("账号");
        builder.setTitle("添加好友").setView(layout)
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String convID = GroupList.getInstance().getGroup(groupid).convId;

                        FindUser.getInstance().setFindUserListener(
                                new FindUser.FindUserListener() {
                                    @Override
                                    public void onOK(OtherUserInfo otherUserInfo) {
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
                                                .addMember(convID, groupid, otherUserInfo.id);
                                    }

                                    @Override
                                    public void onError(String errorMessage) {
                                        Toast.makeText(mContext, "添加失败,查找用户失败：" + errorMessage
                                                , Toast.LENGTH_LONG).show();
                                        Log.w(TAG, "添加失败,查找用户失败：" + errorMessage);
                                    }
                                }
                        ).finduser(userid.getText().toString());


                    }
                }).setNegativeButton("取消", null).show();
    }

    private void deleteUser(String UserName, final String userID, final String groupID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("删除好友").setMessage("确认删除好友: " + UserName + " 么？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //发送系统通知
                        String convID = GroupList.getInstance().getGroup(groupID).convId;
                        Map<String, Object> attr = SendMessage.setMessAttr();
                        attr = SendMessage.setGroupManageSysMessAttr(attr, convID,
                                StaticField.SystemMessAttrName.systemFlag.kicked);
                        SendMessage.getInstance().sendTextMessage(convID, "你被踢出了OAQ", attr);

                        //LeanCloud删除
                        List<String> userIds = new ArrayList<>();
                        userIds.add(userID);
                        AVIMConversation conversation = App.getImClient().getConversation(convID);
                        conversation.kickMembers(userIds, new AVIMConversationCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e != null) {
                                    Toast.makeText(mContext, "删除失败", Toast.LENGTH_LONG).show();
                                } else {
                                    Log.w(TAG, "LC删除好友成功");
                                }
                            }
                        });


                        //后台删除
                        UserManageInGroup.getInstance().setManageGroupListener(
                                new UserManageInGroup.ManageGroupListener() {
                                    @Override
                                    public void onOK() {
                                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onError() {
                                        Toast.makeText(mContext, "失败", Toast.LENGTH_LONG).show();
                                    }
                                }
                        ).deleteUser(groupID, userID);
                    }
                }).setNegativeButton("取消", null).show();
    }

    /**
     * 圈子用户的ViewHolder
     */
    public static class GroupUserViewHolder extends RecyclerView.ViewHolder {

        //界面元素
        private NetworkImageView weibo_avatar_NetworkImageView;

        /**
         * 为界面元素赋值
         *
         * @param v 布局
         */
        private void FindViewByID(View v) {
            weibo_avatar_NetworkImageView = (NetworkImageView) v.findViewById(R.id.pic);
        }

        public GroupUserViewHolder(View v) {
            super(v);
            FindViewByID(v);
        }
    }
}
