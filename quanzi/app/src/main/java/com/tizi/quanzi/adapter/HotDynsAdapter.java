package com.tizi.quanzi.adapter;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.ui.dyns.DynsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/9/11.
 * 热门动态
 */
public class HotDynsAdapter extends PagerAdapter {
    ArrayList<Dyns.DynsEntity> dyns;
    private int LastCount;

    public HotDynsAdapter(ArrayList<Dyns.DynsEntity> dyns) {
        this.dyns = dyns;
    }


    public HotDynsAdapter(List<Dyns.DynsEntity> dyns) {
        this.dyns = new ArrayList<>(dyns);
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
                .inflate(R.layout.item_topic_hot_dyns, container, false);
        Dyns.DynsEntity dyn = dyns.get(position);
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

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dynsIntent = new Intent(App.getApplication(), DynsActivity.class);
                //                Bundle bundle = new Bundle();
                //                bundle.putParcelableArrayList("dyns", dyns);
                //                dynsIntent.putExtras(bundle);
                dynsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getApplication().startActivity(dynsIntent);
            }
        });

        return v;
    }
}
