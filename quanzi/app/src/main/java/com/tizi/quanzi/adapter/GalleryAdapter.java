package com.tizi.quanzi.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.GetThumbnailsUri;
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
        View v = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_big_pic, container, false);
        final SubsamplingScaleImageView image = (SubsamplingScaleImageView) v.findViewById(R.id.pic);

        GetVolley.getmInstance().getImageLoader().get(pics.get(position), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                if (bitmap != null) {
                    image.setImage(ImageSource.bitmap(bitmap));
                    image.setZoomEnabled(true);
                } else if (!image.isImageLoaded()) {
                    image.setImage(ImageSource.resource(R.drawable.face));
                    image.setZoomEnabled(false);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                image.setImage(ImageSource.resource(R.drawable.girl));
                image.setZoomEnabled(false);
            }
        }, GetThumbnailsUri.getPXs(activity, 360), GetThumbnailsUri.getPXs(activity, 640));
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String[] items = {"保存图片"};
                new AlertDialog.Builder(activity)
                        .setItems(items, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                GetVolley.getmInstance().getImageLoader().get(pics.get(position), new ImageLoader.ImageListener() {
                                                    @Override
                                                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                                        Bitmap bitmap = response.getBitmap();
                                                        if (bitmap != null) {
                                                            saveBitmap(bitmap, position);
                                                        }
                                                    }

                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

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

        container.addView(v);
        return v;
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
