package com.tizi.quanzi.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.tool.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by qixingchen on 15/9/14.
 * 主题报名选择群
 */
public class ThemeSignUpGroupAdapter extends RecyclerView.Adapter<ThemeSignUpGroupAdapter.GroupViewHolder> {

    private List<GroupClass> groups;
    private List<Boolean> isSignedIn;
    private Context mContext;
    private String actID;

    public ThemeSignUpGroupAdapter(List<GroupClass> groups, Context mContext, String actID) {
        this.groups = groups;
        isSignedIn = new ArrayList<>();
        for (int i = 0; i < groups.size(); i++) {
            isSignedIn.add(false);
        }
        this.mContext = mContext;
        this.actID = actID;
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
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_theme_sign_up_group, parent, false);
        return new GroupViewHolder(v);
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(final GroupViewHolder holder, final int position) {
        Picasso.with(mContext).load(groups.get(position).Face)
                .resize(GetThumbnailsUri.getPXs(mContext, 127), GetThumbnailsUri.getPXs(mContext, 127))
                .into(holder.groupFace);
        holder.groupFace.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.progressBar.setVisibility(View.VISIBLE);
                        holder.groupFace.setClickable(false);

                        ThemeActs.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                            @Override
                            public void onOK(Object ts) {
                                Timer timer = new Timer();
                                timer.setOnResult(new Timer.OnResult() {
                                    @Override
                                    public void OK() {
                                        boolean selectAfter = !isSignedIn.get(position);
                                        isSignedIn.remove(position);
                                        isSignedIn.add(position, selectAfter);
                                        holder.setBorder(mContext, selectAfter);
                                        holder.progressBar.setVisibility(View.GONE);
                                        holder.groupFace.setClickable(true);
                                    }
                                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1200);
                            }

                            @Override
                            public void onError(String Message) {
                                Timer timer = new Timer();
                                timer.setOnResult(new Timer.OnResult() {
                                    @Override
                                    public void OK() {
                                        holder.progressBar.setVisibility(View.GONE);
                                    }
                                }).execute(2000);
                            }
                        }).signUP(actID, groups.get(position).ID, isSignedIn.get(position) ? 0 : 1);

                    }
                }
        );
        holder.setBorder(mContext, isSignedIn.get(position));
        holder.groupName.setText(groups.get(position).Name);
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

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView groupFace;
        private ProgressBar progressBar;
        private TextView groupName;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupFace = (CircleImageView) itemView.findViewById(R.id.group_face_image_view);
            progressBar = (ProgressBar) itemView.findViewById(R.id.sign_up_process_bar);
            groupName = (TextView) itemView.findViewById(R.id.group_name_text_view);
        }

        public void setBorder(Context mContext, boolean isSelect) {
            if (isSelect) {
                groupFace.setBorderColor(mContext.getResources().getColor(R.color.md_yellow_400));
            } else {
                groupFace.setBorderColor(mContext.getResources().getColor(R.color.md_grey_800));
            }
        }
    }
}
