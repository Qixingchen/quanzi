package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.chat.SendMessage;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.gson.GroupSignUPThemeAns;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Timer;
import com.tizi.quanzi.widget.ForegroundImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qixingchen on 15/9/14.
 * 主题报名选择群
 * 发状态选择群
 */
public class GroupSelectAdapter extends RecyclerViewAdapterAbs {

    public static final int Dyn_Select_Group = 1;
    public static final int Theme_Sign_UP_Group = 0;
    private List<GroupClass> groups;
    private List<Boolean> isSignedIn;
    private Context mContext;
    private Theme.ActsEntity act;
    private int style;

    private Onclick onclick;

    /**
     * @param groups   群列表
     * @param mContext 上下文
     * @param actID    活动,如果是发状态选择,可以为null
     * @param style    是发状态还是报名
     */
    public GroupSelectAdapter(List<GroupClass> groups, Context mContext, final String actID, int style) {
        this.groups = groups;
        isSignedIn = new ArrayList<>();
        for (int i = 0; i < groups.size(); i++) {
            isSignedIn.add(false);
        }
        this.mContext = mContext;
        ThemeActs.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                Theme themes = (Theme) ts;
                for (Theme.ActsEntity temp : themes.acts) {
                    if (temp.id.equals(actID)) {
                        act = temp;
                    }
                }
            }

            @Override
            public void onError(String Message) {

            }
        }).getThemes();
        this.style = style;
    }

    @Override
    public int getItemViewType(int position) {
        return style;
    }


    /**
     * 创建 ViewHolder
     *
     * @param viewGroup 需要创建ViewHolder的 ViewGroup
     * @param viewType  记录类型
     *
     * @return ViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        switch (viewType) {
            case Theme_Sign_UP_Group:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_theme_sign_up_group, viewGroup, false);
                return new SignUPGroupViewHolder(v);
            case Dyn_Select_Group:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_dyn_select_group, viewGroup, false);
                return new DynSelectGroup(v);
            default:
                return null;
        }
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param viewHolder 被绑定的ViewHolder
     * @param position   列表位置
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final GroupClass group = groups.get(position);
        if (SignUPGroupViewHolder.class.isInstance(viewHolder)) {
            final SignUPGroupViewHolder signUpGroupVH = (SignUPGroupViewHolder) viewHolder;

            Picasso.with(mContext).load(group.Face)
                    .fit()
                    .into(signUpGroupVH.groupFace);
            signUpGroupVH.groupFace.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (act == null) {
                                Toast.makeText(mContext, "网络错误", Toast.LENGTH_LONG).show();
                                return;
                            }
                            signUpGroupVH.progressBar.setVisibility(View.VISIBLE);
                            signUpGroupVH.groupFace.setClickable(false);

                            ThemeActs.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                                @Override
                                public void onOK(Object ts) {
                                    GroupSignUPThemeAns ans = (GroupSignUPThemeAns) ts;

                                    Login.UserEntity my = MyUserInfo.getInstance().getUserInfo();
                                    String username = my == null ? "" : my.userName;

                                    boolean selectAfter = !isSignedIn.get(position);
                                    String text;
                                    if (selectAfter) {
                                        text = String.format("%s报名了活动[%s]", username, act.title);
                                        if (ans.oldActName != null) {
                                            text = String.format("%s,原活动[%s]取消", text, ans.oldActName);
                                        }

                                    } else {
                                        text = String.format("%s取消了报名[%s]", username, act.title);
                                    }
                                    new Timer().setOnResult(new Timer.OnResult() {
                                        @Override
                                        public void OK() {
                                            boolean selectAfter = !isSignedIn.get(position);
                                            isSignedIn.remove(position);
                                            isSignedIn.add(position, selectAfter);
                                            signUpGroupVH.setBorder(mContext, selectAfter);
                                            signUpGroupVH.progressBar.setVisibility(View.GONE);
                                            signUpGroupVH.groupFace.setClickable(true);
                                        }

                                        @Override
                                        public void countdown(long remainingS, long goneS) {

                                        }
                                    }).setTimer(1200, 1200).start();
                                    SendMessage.getInstance().sendTextMessage(group.convId, text,
                                            SendMessage.setMessAttr(group.ID, StaticField.ConvType.GROUP, true));
                                }

                                @Override
                                public void onError(final String Message) {
                                    new Timer().setOnResult(new Timer.OnResult() {
                                        @Override
                                        public void OK() {
                                            signUpGroupVH.progressBar.setVisibility(View.GONE);
                                            signUpGroupVH.groupFace.setClickable(true);
                                            Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void countdown(long remainingS, long goneS) {

                                        }

                                    }).setTimer(1200, 1200).start();
                                }
                            }).signUP(act.id, group.ID, isSignedIn.get(position) ? 0 : 1);

                        }
                    }
            );
            signUpGroupVH.setBorder(mContext, isSignedIn.get(position));
            signUpGroupVH.groupName.setText(group.Name);
        }

        if (DynSelectGroup.class.isInstance(viewHolder)) {
            final DynSelectGroup dynSelectGroup = (DynSelectGroup) viewHolder;
            Picasso.with(mContext).load(group.Face.toString())
                    .fit()
                    .into(dynSelectGroup.groupFaceImageView);
            dynSelectGroup.groupNameTextview.setText(group.Name);
            dynSelectGroup.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DynSelectGroup.lastPositon != null) {
                        DynSelectGroup.lastPositon.itemView.setBackgroundColor(
                                mContext.getResources().getColor(R.color.md_grey_800)
                        );
                    }
                    onclick.itemClick(group.ID);
                    dynSelectGroup.itemView.setBackgroundColor(mContext.getResources()
                            .getColor(R.color.md_yellow_400));
                    DynSelectGroup.lastPositon = dynSelectGroup;
                }
            });
        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return groups == null ? 0 : groups.size();
    }

    /**
     * 已经参加活动的群添加标志
     */
    public void setGroupsSignedIn(Map<String, Boolean> signedGroups) {
        for (int i = 0; i < groups.size(); i++) {
            if (signedGroups.containsKey(groups.get(i).ID)) {
                isSignedIn.remove(i);
                isSignedIn.add(i, true);
            }
        }
        notifyDataSetChanged();
    }

    public void setOnclick(Onclick onclick) {
        this.onclick = onclick;
    }

    /**
     * 点击接口
     */
    public interface Onclick {
        /**
         * 项目被点击
         *
         * @param groupID 被点击的GroupID
         */
        void itemClick(String groupID);
    }

    static class SignUPGroupViewHolder extends RecyclerView.ViewHolder {

        private ForegroundImageView groupFace;
        private ProgressBar progressBar;
        private TextView groupName;

        public SignUPGroupViewHolder(View itemView) {
            super(itemView);
            groupFace = (ForegroundImageView) itemView.findViewById(R.id.group_face_image_view);
            progressBar = (ProgressBar) itemView.findViewById(R.id.sign_up_process_bar);
            groupName = (TextView) itemView.findViewById(R.id.group_name_text_view);
        }

        public void setBorder(Context mContext, boolean isSelect) {
            if (isSelect) {
                groupFace.setForeground(mContext.getResources().getDrawable(R.drawable.group_signed_up));
            } else {
                groupFace.setForeground(null);
            }
        }
    }

    static class DynSelectGroup extends RecyclerView.ViewHolder {

        public static DynSelectGroup lastPositon = null;
        public ImageView groupFaceImageView;
        public TextView groupNameTextview;
        public View itemView;

        public DynSelectGroup(View itemView) {
            super(itemView);
            this.itemView = itemView;
            groupFaceImageView = (ImageView) itemView.findViewById(R.id.group_face_image_view);
            groupNameTextview = (TextView) itemView.findViewById(R.id.group_name_text_view);
        }
    }
}
