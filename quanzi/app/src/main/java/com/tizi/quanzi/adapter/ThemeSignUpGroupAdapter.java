package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.tool.GetThumbnailsUri;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by qixingchen on 15/9/14.
 * 主题报名选择群
 */
public class ThemeSignUpGroupAdapter extends RecyclerView.Adapter<ThemeSignUpGroupAdapter.GroupViewHolder> {

    private List<GroupClass> groups;
    private Context mContext;
    private String actID;

    public ThemeSignUpGroupAdapter(List<GroupClass> groups, Context mContext, String actID) {
        this.groups = groups;
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

                        ThemeActs.getNewInstance(mContext).setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                            @Override
                            public void onOK(Object ts) {
                                //todo 加长延迟时间
                                holder.isSelect = !holder.isSelect;
                                holder.setBorder(mContext);
                                holder.progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(String Message) {
                                holder.progressBar.setVisibility(View.GONE);
                            }
                        }).signUP(actID, groups.get(position).ID, holder.isSelect ? 0 : 1);

                    }
                }
        );
        holder.setBorder(mContext);
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return groups == null ? 0 : groups.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView groupFace;
        private boolean isSelect = false;
        private ProgressBar progressBar;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupFace = (CircleImageView) itemView.findViewById(R.id.group_face_image_view);
            progressBar = (ProgressBar) itemView.findViewById(R.id.sign_up_process_bar);
        }

        public void setBorder(Context mContext) {
            if (isSelect) {
                groupFace.setBorderColor(mContext.getResources().getColor(R.color.md_yellow_400));
            } else {
                groupFace.setBorderColor(mContext.getResources().getColor(R.color.md_grey_800));
            }
        }
    }
}
