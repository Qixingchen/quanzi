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
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.chat.GroupUserAdmin;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.FindUser;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.ui.quanzi_zone.QuanziZoneActivity;

import java.util.List;

/**
 * Created by qixingchen on 15/9/2.
 * 圈子用户
 */
public class GroupUserAdapter extends RecyclerView.Adapter<GroupUserAdapter.GroupUserViewHolder> {

    private Context mContext;
    private List<GroupAllInfo.MemlistEntity> memlist;
    private boolean isCreater;
    private String groupID;
    private static final String TAG = GroupUserAdapter.class.getSimpleName();

    public GroupUserAdapter(Context mContext, @Nullable List<GroupAllInfo.MemlistEntity> memlist, boolean isCreater) {
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
    public void onBindViewHolder(GroupUserViewHolder holder, final int position) {
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
            final GroupAllInfo.MemlistEntity mem = memlist.get(position);
            holder.weibo_avatar_NetworkImageView.setImageUrl(mem.icon,
                    GetVolley.getmInstance(mContext).getImageLoader());
            if (isCreater) {
                holder.weibo_avatar_NetworkImageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        deleteUser(mem.name, mem.id, groupID, position);
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
    private void addUser(final String groupid) {
        final Activity activity = AppStaticValue.getActivity(QuanziZoneActivity.class.getSimpleName());
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
                        GroupUserAdmin.getInstance(mContext).setOnResult(
                                new GroupUserAdmin.OnResult() {
                                    @Override
                                    public void OK() {
                                        memlist.remove(postion);
                                    }

                                    @Override
                                    public void error(String errorMessage) {

                                    }
                                }
                        ).deleteMember(ConvID, groupID, userID);
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
