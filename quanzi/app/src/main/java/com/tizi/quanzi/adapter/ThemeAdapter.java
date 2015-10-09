package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.HotDyns;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/9/11.
 */
public class ThemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = ThemeAdapter.class.getSimpleName();
    private Context mContext;
    private List<Theme.ActsEntity> acts;
    private OnClick onClick;

    public ThemeAdapter(List<Theme.ActsEntity> acts, Context mContext) {
        this.mContext = mContext;
        this.acts = acts;
    }

    /**
     * 判定类型
     *
     * @param position 判定位置
     *
     * @return 类型
     */
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            //敬请期待
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * 创建 ViewHolder
     *
     * @param parent   需要创建ViewHolder的 ViewGroup
     * @param viewType 记录类型
     *
     * @return ViewHolder
     */
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case -1:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hope_for_next, parent, false);
                viewHolder = new HopeForNext(v, mContext);
                break;
            case 1:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_tuo_dan_zuo_zan, parent, false);
                viewHolder = new TuoDanZuoZanViewHolder(v, mContext);
                break;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_dyns, parent, false);
                viewHolder = new DefaultViewHolder(v, mContext);
        }
        return viewHolder;
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (TuoDanZuoZanViewHolder.class.isInstance(holder)) {
            Log.i(TAG, position + "is TuoDanZuoZanViewHolder");
            TuoDanZuoZanViewHolder tuodan = (TuoDanZuoZanViewHolder) holder;
            tuodan.participantsNum.setText(String.valueOf(acts.get(position).participantsNum));
            tuodan.detail.setText(acts.get(position).content);
            tuodan.themeIcon.setImageUrl(acts.get(position).icon, GetVolley.getmInstance().getImageLoader());
            tuodan.addHotDyns(acts.get(position).id);
            tuodan.participateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClick == null) {
                        Log.w(TAG, "报名但没有回调监听:" + position);
                        return;
                    }
                    onClick.SignUP(acts.get(position));
                }
            });
            tuodan.themeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClick == null) {
                        Log.w(TAG, "没有回调监听:" + position);
                        return;
                    }
                    onClick.EnterTheme(acts.get(position));
                }
            });
        }
        if (HopeForNext.class.isInstance(holder)) {
            Log.i(TAG, position + "is HopeForNext");
        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return acts == null ? 1 : acts.size() + 1;
    }

    /**
     * 添加纪录
     */
    public void addItem(Theme.ActsEntity act) {
        if (acts == null) {
            acts = new ArrayList<>();
        }
        acts.add(act);
        notifyDataSetChanged();
    }

    public ThemeAdapter setOnClick(OnClick onClick) {
        this.onClick = onClick;
        return this;
    }

    /**
     * 报名监听
     */
    public interface OnClick {
        /*报名*/
        void SignUP(Theme.ActsEntity act);

        /*进入活动*/
        void EnterTheme(Theme.ActsEntity act);
    }

    /**
     * 敬请期待的ViewHolder
     */
    public static class HopeForNext extends RecyclerView.ViewHolder {
        protected ImageView icon;

        public HopeForNext(View itemView, Context context) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.pic);


        }
    }

    /**
     * 脱单作战的VIewHolder
     */
    public static class TuoDanZuoZanViewHolder extends DefaultViewHolder {

        public TuoDanZuoZanViewHolder(View itemView, Context context) {
            super(itemView, context);
        }
    }

    /**
     * 基础的ViewHolder
     */
    public static class DefaultViewHolder extends RecyclerView.ViewHolder {

        protected TextView detail, participantsNum;
        protected Button participateButton;
        protected NetworkImageView themeIcon;
        protected View view;
        protected ViewPager dynsViewPager;

        protected Context mContext;

        public DefaultViewHolder(View itemView, Context context) {
            super(itemView);
            this.view = itemView;
            findViews();
        }

        protected void findViews() {
            detail = (TextView) view.findViewById(R.id.detail_text_view);
            themeIcon = (NetworkImageView) view.findViewById(R.id.theme_icon);
            participateButton = (Button) view.findViewById(R.id.participate_button);
            participantsNum = (TextView) view.findViewById(R.id.num_of_participants);
            dynsViewPager = (ViewPager) view.findViewById(R.id.dynsViewPager);
        }

        protected void addHotDyns(String themeID) {

            ThemeActs.getNewInstance().setNetworkListener(
                    new RetrofitNetworkAbs.NetworkListener() {
                        @Override
                        public void onOK(Object ts) {
                            HotDyns hotDyns = (HotDyns) ts;
                            HotDynsAdapter hotDynsAdapter = new HotDynsAdapter(hotDyns.dyns);
                            dynsViewPager.setAdapter(hotDynsAdapter);
                        }

                        @Override
                        public void onError(String Message) {

                        }
                    }).getHotDyns(themeID);
        }
    }
}
