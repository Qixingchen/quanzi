package com.tizi.quanzi.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tizi.quanzi.R;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.tool.ZipPic;

import java.io.File;
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
        final SubsamplingScaleImageView image = (SubsamplingScaleImageView) vRoot.findViewById(R.id.pic);

        Picasso.with(activity).load(pics.get(position)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                image.setImage(ImageSource.bitmap(bitmap));
                image.setZoomEnabled(true);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                image.setImage(ImageSource.resource(R.drawable.girl));
                image.setZoomEnabled(false);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                image.setZoomEnabled(false);
            }
        });
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                String[] items = {"保存图片"};
                new AlertDialog.Builder(activity)
                        .setItems(items, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                Picasso.with(activity).load(pics.get(position)).into(new Target() {
                                                    @Override
                                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                        saveBitmap(bitmap, position);
                                                    }

                                                    @Override
                                                    public void onBitmapFailed(Drawable errorDrawable) {
                                                        Snackbar.make(vRoot, "下载失败", Snackbar.LENGTH_LONG).show();
                                                    }

                                                    @Override
                                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                                        Snackbar.make(vRoot, "下载失败", Snackbar.LENGTH_LONG).show();
                                                    }
                                                });
                                                break;
                                        }
                                    }
                                }

                        ).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }

                ).show();
                return false;
            }
        });

        container.addView(vRoot);
        return vRoot;
    }

    private void saveBitmap(Bitmap bitmap, int position) {
        String RootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        String FilePath = RootPath + "/" + StaticField.AppName.AppEngName + "/" + Tool.getFileName(pics.get(position));
        ZipPic.saveMyBitmap(FilePath, bitmap, 100);
        Toast.makeText(activity, "保存成功在：" + FilePath, Toast.LENGTH_LONG).show();

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(FilePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);


    }

}
