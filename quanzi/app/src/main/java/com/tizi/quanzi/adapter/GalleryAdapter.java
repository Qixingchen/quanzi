package com.tizi.quanzi.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.tool.ShareImage;
import com.tizi.quanzi.tool.Tool;

import java.util.List;

/**
 * Created by qixingchen on 15/9/21.
 * 图片查看
 */
public class GalleryAdapter extends PagerAdapter {
    private static final String TAG = GalleryAdapter.class.getSimpleName();
    private List<String> pics;
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
    public Object instantiateItem(ViewGroup container, final int position) {
        final View vRoot = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_big_pic, container, false);
        final ImageView image = (ImageView) vRoot.findViewById(R.id.pic);
        final Bitmap[] mBitmap = {null};

        Picasso.with(activity).load(pics.get(position))
                .placeholder(R.drawable.face)
                .into(image);
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                if (mBitmap[0] == null) {
                    return false;
                }
                String[] items = {"保存图片", "分享图片"};
                new AlertDialog.Builder(activity)
                        .setItems(items, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                ShareImage.getInstance().saveImage(activity, mBitmap[0],
                                                        Tool.getFileName(pics.get(position)));
                                                break;
                                            case 1:
                                                ShareImage.getInstance().shareImage(mBitmap[0],
                                                        Tool.getFileName(pics.get(position)));
                                                break;
                                        }
                                    }
                                }

                        ).show();
                return false;
            }
        });

        container.addView(vRoot);
        return vRoot;
    }

}
