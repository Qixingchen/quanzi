package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.tool.GetThumbnailsUri;

import java.util.List;

/**
 * Created by qixingchen on 15/9/14.
 * 主题报名选择群
 */
public class ThemeSignUpGroupAdapter extends RecyclerView.Adapter<ThemeSignUpGroupAdapter.GroupViewHolder> {

    private List<GroupClass> groups;
    private Context mContext;

    public ThemeSignUpGroupAdapter(List<GroupClass> groups, Context mContext) {
        this.groups = groups;
        this.mContext = mContext;
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
                .inflate(R.layout.theme_sign_up_group_item, parent, false);
        return new GroupViewHolder(v);
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        Picasso.with(mContext).load(groups.get(position).Face)
                .resize(GetThumbnailsUri.getPXs(mContext, 127), GetThumbnailsUri.getPXs(mContext, 127))
                .into(holder.groupFace);
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return groups == null ? 0 : groups.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        private ImageView groupFace;
        private boolean isSelect = false;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupFace = (ImageView) itemView.findViewById(R.id.group_face_image_view);
        }
    }
}
