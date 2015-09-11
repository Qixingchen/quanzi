package com.tizi.quanzi.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    List<HotDyns.DynEntity> dyns;

    public HotDynsAdapter(List<HotDyns.DynEntity> dyns) {
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
        NetworkImageView dynPic = (NetworkImageView) v.findViewById(R.id.dyn_pic);
        TextView dynText = (TextView) v.findViewById(R.id.dyn_text);

        dynPic.setImageUrl(dyns.get(position).icon, GetVolley.getmInstance().getImageLoader());
        dynText.setText(dyns.get(position).text);
        container.addView(v);
        return v;
        //super.instantiateItem(container, position);
    }
}
