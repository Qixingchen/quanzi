package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.HotDyns;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.network.NetWorkAbs;
import com.tizi.quanzi.network.ThemeActs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/9/11.
 */
public class ThemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Theme.ActsEntity> acts;

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
        return 1;
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
            case 1:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tuo_dan_zuo_zan_item, parent, false);
                viewHolder = new TuoDanZuoZanViewHolder(v, mContext);
                break;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.dyns_item, parent, false);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TuoDanZuoZanViewHolder.class.isInstance(holder)) {
            TuoDanZuoZanViewHolder tuodan = (TuoDanZuoZanViewHolder) holder;
            tuodan.participantsNum.setText(String.valueOf(acts.get(position).participantsNum));
            tuodan.detail.setText(acts.get(position).content);
            tuodan.themeIcon.setImageUrl(acts.get(position).icon, GetVolley.getmInstance().getImageLoader());
        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return acts == null ? 0 : acts.size();
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
            initViews();
        }

        protected void findViews() {
            detail = (TextView) view.findViewById(R.id.detail_text_view);
            themeIcon = (NetworkImageView) view.findViewById(R.id.theme_icon);
            participateButton = (Button) view.findViewById(R.id.participate_button);
            participantsNum = (TextView) view.findViewById(R.id.num_of_participants);
            dynsViewPager = (ViewPager) view.findViewById(R.id.dynsViewPager);
        }

        protected void initViews() {

            //todo test

            HotDyns hotDyns = new HotDyns();
            hotDyns.Dyns = new ArrayList<>();
            HotDyns.DynEntity dyn = new HotDyns.DynEntity();
            dyn.icon = "http://i.ytimg.com/vi/3btAtK3zllE/maxresdefault.jpg";
            dyn.text = "niconiconi";
            hotDyns.Dyns.add(dyn);

            HotDyns.DynEntity dyn2 = new HotDyns.DynEntity();
            dyn2.icon = "http://i.ytimg.com/vi/3btAtK3zllE/maxresdefault.jpg";
            dyn2.text = "niconiconi";
            hotDyns.Dyns.add(dyn2);

            HotDyns.DynEntity dyn3 = new HotDyns.DynEntity();
            dyn3.icon = "http://i.ytimg.com/vi/3btAtK3zllE/maxresdefault.jpg";
            dyn3.text = "niconiconi";
            hotDyns.Dyns.add(dyn3);

            HotDynsAdapter hotDynsAdapter = new HotDynsAdapter(hotDyns.Dyns);
            dynsViewPager.setAdapter(hotDynsAdapter);

            ThemeActs.getInstance(mContext).setNetworkListener(
                    new NetWorkAbs.NetworkListener() {
                        @Override
                        public void onOK(Object ts) {
                            HotDyns hotDyns = new Gson().fromJson((String) ts, HotDyns.class);
                            hotDyns = new HotDyns();
                            hotDyns.Dyns = new HotDyns().Dyns;
                            HotDyns.DynEntity dyn = new HotDyns.DynEntity();
                            dyn.icon = "http://i2.minus.com/iqHelcW4Lh80T.png";
                            dyn.text = "niconiconi";
                            hotDyns.Dyns.add(dyn);
                            HotDynsAdapter hotDynsAdapter = new HotDynsAdapter(hotDyns.Dyns);
                            dynsViewPager.setAdapter(hotDynsAdapter);
                        }

                        @Override
                        public void onError(String Message) {

                        }
                    }
            );
            //todo .getHotDyns();

        }
    }
}
