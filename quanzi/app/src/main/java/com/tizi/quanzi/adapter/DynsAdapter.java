package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.GetVolley;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by qixingchen on 15/8/19.
 * 动态
 */
public class DynsAdapter extends RecyclerView.Adapter<DynsAdapter.DynsViewHolder> {

    private List<Dyns.DynsEntity> dynsList;
    private Context mContext;
    private NeedMore needMore;
    private static final String TAG = DynsAdapter.class.getSimpleName();

    /**
     * @param dynsList 动态List
     * @param context  上下文
     *
     * @see com.tizi.quanzi.gson.Dyns.DynsEntity
     */
    public DynsAdapter(List<Dyns.DynsEntity> dynsList, Context context) {
        this.dynsList = dynsList;
        this.mContext = context;
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
    public DynsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dyns_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        DynsViewHolder vh = new DynsViewHolder(v);
        return vh;
    }


    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(DynsViewHolder holder, int position) {
        final Dyns.DynsEntity dyns = dynsList.get(position);
        holder.weibo_avatar_NetworkImageView.setImageUrl(dyns.icon,
                GetVolley.getmInstance(mContext).getImageLoader());
        holder.userNameTextView.setText(dyns.nickName);
        holder.contentTextView.setText(dyns.content);
        holder.dateTextView.setText(dyns.createTime);
        holder.attitudesTextView.setText(String.valueOf(dyns.zan));
        holder.commentsTextView.setText(String.valueOf(dyns.commentNum));
        int picsNum = dyns.pics.size();
        for (int i = 0; i < picsNum; i++) {
            holder.weibo_pics_NetworkImageView[i].setImageUrl(dyns.pics.get(i).url,
                    GetVolley.getmInstance(mContext).getImageLoader());
        }
        holder.setPicUnvisable(picsNum);
        if (position == dynsList.size() - 1) {
            if (needMore != null) {
                needMore.needMore();
            } else {
                Log.w(TAG, "needMore 回调为空");
            }
        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return dynsList == null ? 0 : dynsList.size();
    }


    /**
     * 为 dynsList 添加内容
     * 如果 dynsList 为空则新建。
     *
     * @param dynsEntities 需要添加的内容List
     */
    public void addItems(List<Dyns.DynsEntity> dynsEntities) {
        if (dynsList == null) {
            dynsList = new ArrayList<>();
        }
        for (Dyns.DynsEntity dynsEntity : dynsEntities) {
            dynsList.add(dynsEntity);
        }
        notifyDataSetChanged();
    }

    public void setNeedMore(NeedMore needMore) {
        this.needMore = needMore;
    }

    public interface NeedMore {
        void needMore();
    }

    /**
     * 动态界面的ViewHolder
     */
    public static class DynsViewHolder extends RecyclerView.ViewHolder {

        //界面元素
        private NetworkImageView weibo_avatar_NetworkImageView;
        private TextView userNameTextView, contentTextView, dateTextView,
                attitudesTextView, commentsTextView;
        private NetworkImageView[] weibo_pics_NetworkImageView = new NetworkImageView[9];
        private LinearLayout weibo_pics_linearLayout;

        /**
         * 为界面元素赋值
         *
         * @param v 布局
         */
        private void FindViewByID(View v) {
            weibo_avatar_NetworkImageView = (NetworkImageView) v.findViewById(R.id.weibo_avatar);
            userNameTextView = (TextView) v.findViewById(R.id.weibo_name);
            contentTextView = (TextView) v.findViewById(R.id.weibo_content);
            dateTextView = (TextView) v.findViewById(R.id.weibo_date);
            attitudesTextView = (TextView) v.findViewById(R.id.weibo_attitudes);
            commentsTextView = (TextView) v.findViewById(R.id.weibo_comments);
            weibo_pics_NetworkImageView[0] = (NetworkImageView) v.findViewById(R.id.weibo_pic0);
            weibo_pics_NetworkImageView[1] = (NetworkImageView) v.findViewById(R.id.weibo_pic1);
            weibo_pics_NetworkImageView[2] = (NetworkImageView) v.findViewById(R.id.weibo_pic2);
            weibo_pics_NetworkImageView[3] = (NetworkImageView) v.findViewById(R.id.weibo_pic3);
            weibo_pics_NetworkImageView[4] = (NetworkImageView) v.findViewById(R.id.weibo_pic4);
            weibo_pics_NetworkImageView[5] = (NetworkImageView) v.findViewById(R.id.weibo_pic5);
            weibo_pics_NetworkImageView[6] = (NetworkImageView) v.findViewById(R.id.weibo_pic6);
            weibo_pics_NetworkImageView[7] = (NetworkImageView) v.findViewById(R.id.weibo_pic7);
            weibo_pics_NetworkImageView[8] = (NetworkImageView) v.findViewById(R.id.weibo_pic8);
            weibo_pics_linearLayout = (LinearLayout) v.findViewById(R.id.weibo_pics);
        }

        /**
         * 将多余的图片设置成不可见
         * 如果没有图片，则将 weibo_pics_linearLayout 也设置为不可见
         *
         * @param picsNum 图片数量
         */
        public void setPicUnvisable(int picsNum) {
            if (picsNum == 0) {
                weibo_pics_linearLayout.setVisibility(View.GONE);
                return;
            }
            for (int i = picsNum; i < 9; i++) {
                weibo_pics_NetworkImageView[i].setVisibility(View.GONE);
            }
        }

        public DynsViewHolder(View v) {
            super(v);
            FindViewByID(v);
        }
    }

}
