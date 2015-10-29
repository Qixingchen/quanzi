package com.tizi.quanzi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.AllTags;

import java.util.List;

/**
 * Created by qixingchen on 15/10/29.
 */
public class TagSelectAdapter extends RecyclerViewAdapterAbs {

    private List<AllTags.TagsEntity> tags;
    private OnSelect onSelect;

    public TagSelectAdapter(List<AllTags.TagsEntity> tags, OnSelect onSelect) {
        this.tags = tags;
        this.onSelect = onSelect;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tag, parent, false);
        return new TagViewHolder(v);
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (TagViewHolder.class.isInstance(holder)) {
            TagViewHolder tagViewHolder = (TagViewHolder) holder;
            tagViewHolder.tag.setText(tags.get(position).tagName);
            tagViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSelect != null) {
                        onSelect.select(tags.get(position));
                    }
                }
            });
        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return tags == null ? 0 : tags.size();
    }

    public interface OnSelect {
        void select(AllTags.TagsEntity tag);
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {

        private TextView tag;

        public TagViewHolder(View itemView) {
            super(itemView);
            tag = (TextView) itemView.findViewById(R.id.tag_text_view);
        }
    }
}
