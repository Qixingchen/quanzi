package com.tizi.quanzi.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.HotDyns;
import com.tizi.quanzi.network.GetVolley;

import java.util.List;

/**
 * Created by qixingchen on 15/9/11.
 * 热门动态
 */
public class HotDynsAdapter extends PagerAdapter {
    List<HotDyns.DynsEntity> dyns;
    private int LastCount;

    public HotDynsAdapter(List<HotDyns.DynsEntity> dyns) {
        this.dyns = dyns;
    }

    @Override
    public int getCount() {
        return dyns == null ? 0 : dyns.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = LayoutInflater.from(container.getContext())
                .inflate(R.layout.topic_hot_dyns_item, container, false);
        HotDyns.DynsEntity dyn = dyns.get(position);
        NetworkImageView dynPic = (NetworkImageView) v.findViewById(R.id.dyn_pic);
        TextView dynText = (TextView) v.findViewById(R.id.dyn_text);
        LinearLayout pointGroup = (LinearLayout) v.findViewById(R.id.point_group);

        for (int i = 0; i < getCount(); i++) {
            View view = new View(container.getContext());
            view.setBackgroundResource(R.drawable.point_background);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            params.leftMargin = 10;
            if (position != i) {
                view.setEnabled(false);
            } else {
                view.setEnabled(true);
            }
            view.setLayoutParams(params);
            pointGroup.addView(view);// 向线性布局中添加点
        }

        if (dyn.pics.size() == 0) {
            dynPic.setImageUrl(dyn.icon, GetVolley.getmInstance().getImageLoader());
        } else {
            dynPic.setImageUrl(dyn.pics.get(0).url, GetVolley.getmInstance().getImageLoader());
        }
        dynText.setText(dyn.content);
        container.addView(v);
        return v;
    }
}
