package com.tizi.quanzi.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.tool.ShareImage;
import com.tizi.quanzi.tool.Tool;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
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
    private ProgressDialog progressDialog;

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
        final String imageUri;
        if (Uri.parse(pics.get(position)).getScheme().equals("file")) {
            imageUri = pics.get(position);
        } else {
            imageUri = GetThumbnailsUri.getWebPUri(pics.get(position));
        }
        Picasso.with(activity).load(imageUri)
                .placeholder(R.drawable.ic_photo_loading)
                .centerInside()
                .fit()
                .into(image);
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                progressDialog = showDialog();

                final String filename = Tool.getFileName(pics.get(position));

                if (Uri.parse(pics.get(position)).getScheme().equals("file")) {
                    callAlertDialog(pics.get(position).replace("file://", ""));
                } else {
                    Observable.create(new Observable.OnSubscribe<File>() {
                        @Override
                        public void call(final Subscriber<? super File> subscriber) {

                            Request request = new Request.Builder().url(imageUri).build();
                            new OkHttpClient().newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Request request, IOException e) {
                                    // handle failure
                                }

                                @Override
                                public void onResponse(Response response) throws IOException {
                                    File outputFile = new File(Tool.getCacheCacheDir(), filename);
                                    BufferedSource source = response.body().source();
                                    Sink sink = Okio.sink(outputFile);
                                    source.readAll(sink);
                                    source.close();
                                    sink.close();
                                    subscriber.onNext(outputFile);
                                }
                            });

                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<File>() {
                                @Override
                                public void call(File file) {
                                    callAlertDialog(file.getAbsolutePath());
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    throwable.printStackTrace();
                                    progressDialog.cancel();
                                }
                            });
                }
                return false;
            }
        });

        container.addView(vRoot);
        return vRoot;
    }

    /**
     * 调起
     */
    private void callAlertDialog(final String filePath) {

        if (progressDialog != null) {
            progressDialog.cancel();
        }

        String[] items = {"保存图片", "分享图片"};
        new AlertDialog.Builder(activity)
                .setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        ShareImage.getInstance().saveImage(activity, filePath);
                                        break;
                                    case 1:
                                        ShareImage.getInstance().shareImage(activity, filePath);
                                        break;
                                }
                            }
                        }
                ).show();
    }

    /**
     * 进度条
     */
    private ProgressDialog showDialog() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("准备中");

        progressDialog.show();

        return progressDialog;
    }


}
