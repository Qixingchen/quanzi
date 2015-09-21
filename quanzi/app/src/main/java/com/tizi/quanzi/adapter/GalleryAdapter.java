package com.tizi.quanzi.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.TouchImageView;

import java.util.List;

/**
 * Created by qixingchen on 15/9/21.
 * 图片查看
 */
public class GalleryAdapter extends PagerAdapter {
    private List<String> pics;
    private static final String TAG = GalleryAdapter.class.getSimpleName();
    private Activity activity;

    public GalleryAdapter(List<String> pics, Activity activity) {
        this.pics = pics;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return pics == null ? 0 : pics.size();
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
                .inflate(R.layout.item_big_pic, container, false);
        TouchImageView image = (TouchImageView) v.findViewById(R.id.pic);
        Log.i(TAG, "load:" + pics.get(position));
        Picasso.with(activity)
                .load(pics.get(position))
                .into(image);
        container.addView(v);
        return v;
    }
}
