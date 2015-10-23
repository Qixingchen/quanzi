package com.tizi.quanzi.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.otto.BusProvider;

import java.util.List;

/**
 * Created by qixingchen on 15/9/3.
 * 私信、系统消息列表
 */
public class PrivateMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = PrivateMessageAdapter.class.getSimpleName();

    private List<PrivateMessPair> privateMessPairs;
    private Context mContext;
    private Onclick onclick;


    /**
     * @param privateMessPairs 私信列表
     * @param mContext         上下文
     * @param onclick          被点击时的回调
     */
    public PrivateMessageAdapter(List<PrivateMessPair> privateMessPairs, Context mContext, Onclick onclick) {
        this.privateMessPairs = privateMessPairs;
        this.mContext = mContext;
        this.onclick = onclick;
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onChanged(PrivateMessPairList list) {
        notifyDataSetChanged();
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

        View v;
        RecyclerView.ViewHolder vh;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_private_mess, parent, false);
        vh = new PrivateViewHolder(v);

        return vh;
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



        /*私信*/
        if (PrivateViewHolder.class.isInstance(holder)) {
            final PrivateViewHolder privateVH = (PrivateViewHolder) holder;
            final PrivateMessPair privateMessPair = privateMessPairs.get(position);
            privateVH.mUserFaceImageNetworkImageView.setImageUrl(privateMessPair.Face,
                    GetVolley.getmInstance().getImageLoader());
            privateVH.mUserNameTextViewTextView.setText(privateMessPair.Name);
            privateVH.mMessTextViewTextView.setText(privateMessPair.lastMess);
            if (privateMessPair.UnreadCount != 0) {
                privateVH.mMessTextViewTextView.setTypeface(Typeface.DEFAULT_BOLD);
                privateVH.mMessTextViewTextView.setTextColor(mContext.getResources().getColor(R.color.md_black));
                privateVH.mUserNameTextViewTextView.setTypeface(Typeface.DEFAULT_BOLD);
                privateVH.mUserNameTextViewTextView.setTextColor(mContext.getResources().getColor(R.color.md_black));
            } else {
                privateVH.mMessTextViewTextView.setTypeface(Typeface.DEFAULT);
                privateVH.mMessTextViewTextView.setTextColor(mContext.getResources().getColor(R.color.md_grey_600));
                privateVH.mUserNameTextViewTextView.setTypeface(Typeface.DEFAULT);
                privateVH.mUserNameTextViewTextView.setTextColor(mContext.getResources().getColor(R.color.md_grey_600));
            }
            privateVH.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onclick != null) {
                        onclick.priMessClick(privateMessPair);
                    } else {
                        Log.w(TAG, "私聊消息被点击,但是没有 onclick 回调");
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
        return privateMessPairs == null ? 0 : privateMessPairs.size();
    }

    /**
     * 点击接口
     */
    public interface Onclick {

        /**
         * 项目被点击
         *
         * @param privateMessPair 被点击的私聊消息组
         */
        void priMessClick(PrivateMessPair privateMessPair);
    }

    public static class PrivateViewHolder extends RecyclerView.ViewHolder {

        private NetworkImageView mUserFaceImageNetworkImageView;
        private TextView mUserNameTextViewTextView;
        private TextView mMessTextViewTextView;
        private View itemView;

        public PrivateViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mUserFaceImageNetworkImageView = (NetworkImageView) itemView.findViewById(R.id.user_face_image);
            mUserNameTextViewTextView = (TextView) itemView.findViewById(R.id.user_name_text_view);
            mMessTextViewTextView = (TextView) itemView.findViewById(R.id.mess_text_view);
        }
    }

}
