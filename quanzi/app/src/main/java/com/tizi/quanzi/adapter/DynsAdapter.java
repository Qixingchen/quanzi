package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.widget.DynItem;

import java.util.List;


/**
 * Created by qixingchen on 15/8/19.
 * 动态
 */
public class DynsAdapter extends RecyclerViewAdapterAbs {

    private static final String TAG = DynsAdapter.class.getSimpleName();
    private static final int AD = 1;
    private static final int DYN_ITEM = 2;
    private SortedList<Dyns.DynsEntity> dynsList = new SortedList<>(Dyns.DynsEntity.class,
            new SortedList.Callback<Dyns.DynsEntity>() {
                @Override
                public int compare(Dyns.DynsEntity o1, Dyns.DynsEntity o2) {
                    long o1t = FriendTime.getTimeFromServerString(o1.createTime);
                    long o2t = FriendTime.getTimeFromServerString(o2.createTime);
                    return (int) ((o2t - o1t) / 1000);
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
                public boolean areContentsTheSame(Dyns.DynsEntity oldItem, Dyns.DynsEntity newItem) {
                    return oldItem.content != null && oldItem.content.equals(newItem.content);
                }

                @Override
                public boolean areItemsTheSame(Dyns.DynsEntity item1, Dyns.DynsEntity item2) {
                    return item1.dynid.equals(item2.dynid);
                }
            });
    private Context mContext;
    private NeedMore needMore;
    private Onclick onclick;
    private boolean showUser = false;
    private boolean isUser = false;
    private String themeADImage;

    /**
     * @param dynsList     动态List
     * @param themeAdImage 活动的广告图片,null表示没有广告
     * @param context      上下文
     * @param isUser       是否是用户动态
     *
     * @see com.tizi.quanzi.gson.Dyns.DynsEntity
     */
    public DynsAdapter(List<Dyns.DynsEntity> dynsList, String themeAdImage, Context context, boolean isUser) {
        if (dynsList != null) {
            this.dynsList.beginBatchedUpdates();
            this.dynsList.addAll(dynsList);
            this.dynsList.endBatchedUpdates();
        }
        this.themeADImage = themeAdImage;
        this.mContext = context;
        this.isUser = isUser;
    }

    public void setShowUser(boolean showUser) {
        this.showUser = showUser;
        notifyDataSetChanged();
    }

    /**
     * @param parent   需要创建ViewHolder的 ViewGroup
     * @param viewType 样式类型
     *
     * @return ViewHolder
     *
     * @see com.tizi.quanzi.adapter.DynsAdapter.DynsViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder vh;
        switch (viewType) {
            case DYN_ITEM:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_dyn, parent, false);
                vh = new DynsViewHolder(v);
                break;
            case AD:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_theme_ad, parent, false);
                vh = new ADViewHolder(v);
                break;
            default:
                vh = null;
        }
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && themeADImage != null) {
            return AD;
        }
        return DYN_ITEM;
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param viewHolder 被绑定的ViewHolder
     * @param position   列表位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (ADViewHolder.class.isInstance(viewHolder)) {
            ADViewHolder vh = (ADViewHolder) viewHolder;
            Picasso.with(vh.adImageView.getContext()).load(themeADImage).into(vh.adImageView);
            return;
        }


        if (DynsViewHolder.class.isInstance(viewHolder)) {
            DynsViewHolder holder = (DynsViewHolder) viewHolder;
            int addtion = themeADImage == null ? 0 : 1;
            final Dyns.DynsEntity dyns = dynsList.get(position - addtion);
            DynItem dynItem = new DynItem(dyns, holder.view, showUser, isUser, mContext);

            //点击回调
            dynItem.contentTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onclick != null) {
                        onclick.click(dyns);
                    } else {
                        Log.w(TAG, "Onclick 回调为空");
                    }
                }
            });

            dynItem.contentTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        if (onclick != null) {
                            onclick.click(dyns);
                        } else {
                            Log.w(TAG, "Onclick 回调为空");
                        }
                    }
                }
            });

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onclick != null) {
                        onclick.click(dyns);
                    } else {
                        Log.w(TAG, "Onclick 回调为空");
                    }
                }
            });

            //加载更多
            if (position == dynsList.size() - 1) {
                if (needMore != null) {
                    needMore.needMore();
                } else {
                    Log.w(TAG, "needMore 回调为空");
                }
            }
        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        int addtion = themeADImage == null ? 0 : 1;
        return dynsList == null ? addtion : dynsList.size() + addtion;
    }

    /**
     * 为 dynsList 添加内容
     * 如果 dynsList 为空则新建。
     *
     * @param dynsEntities 需要添加的内容List
     */
    public void addItems(List<Dyns.DynsEntity> dynsEntities) {
        this.dynsList.beginBatchedUpdates();
        this.dynsList.addAll(dynsEntities);
        this.dynsList.endBatchedUpdates();
    }

    public void setNeedMore(NeedMore needMore) {
        this.needMore = needMore;
    }

    public void setOnclick(Onclick onclick) {
        this.onclick = onclick;
    }

    public interface NeedMore {
        void needMore();
    }

    public interface Onclick {
        void click(Dyns.DynsEntity dyn);
    }

    /**
     * 动态界面的ViewHolder
     */
    public static class DynsViewHolder extends RecyclerView.ViewHolder {

        //界面元素
        private View view;

        public DynsViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    class ADViewHolder extends RecyclerView.ViewHolder {
        private ImageView adImageView;

        public ADViewHolder(View itemView) {
            super(itemView);
            adImageView = (ImageView) itemView.findViewById(R.id.theme_ad_image);
        }
    }

}
