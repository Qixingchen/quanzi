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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.tool.ShareImage;
import com.tizi.quanzi.tool.Tool;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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

        Picasso.with(activity).load(pics.get(position))
                .placeholder(R.drawable.ic_photo_loading)
                .into(image);
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                Observable.create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(Subscriber<? super Bitmap> subscriber) {
                        try {
                            Bitmap bitmap = Picasso.with(activity).load(pics.get(position)).get();
                            subscriber.onNext(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Bitmap>() {
                            @Override
                            public void call(Bitmap bitmap) {
                                callAlertDialog(bitmap, pics.get(position));
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_LONG).show();
                                throwable.printStackTrace();
                            }
                        });
                return false;
            }
        });

        container.addView(vRoot);
        return vRoot;
    }

    /**
     * 调起
     */
    private void callAlertDialog(final Bitmap bitmap, final String filePath) {
        String[] items = {"保存图片", "分享图片"};
        new AlertDialog.Builder(activity)
                .setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        ShareImage.getInstance().saveImage(activity, bitmap,
                                                Tool.getFileName(filePath));
                                        break;
                                    case 1:
                                        ShareImage.getInstance().shareImage(activity, bitmap,
                                                Tool.getFileName(filePath));
                                        break;
                                }
                            }
                        }
                ).show();
    }

}
