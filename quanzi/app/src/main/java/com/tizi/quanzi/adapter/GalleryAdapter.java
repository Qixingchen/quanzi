package com.tizi.quanzi.adapter;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.tool.ShareImage;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
    private BaseActivity activity;

    public GalleryAdapter(List<String> pics, BaseActivity activity) {
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
        final String[] imageLocalPath = new String[1];
        if (Uri.parse(pics.get(position)).getScheme().equals("file")) {
            imageLocalPath[0] = pics.get(position).replace("file://", "");
            image.setImage(ImageSource.uri(imageLocalPath[0]));
            image.setZoomEnabled(true);
        } else {
            image.setImage(ImageSource.resource(R.drawable.ic_photo_loading));
            image.setZoomEnabled(false);
            Uri imageUrl = Uri.parse(GetThumbnailsUri.getWebPUri(pics.get(position)));
            downloadFile(imageUrl).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            imageLocalPath[0] = file.getAbsolutePath();
                            image.setImage(ImageSource.uri(imageLocalPath[0]));
                            image.setZoomEnabled(true);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Snackbar.make(activity.view, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
                            throwable.printStackTrace();
                        }
                    });
        }
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                callAlertDialog(imageLocalPath[0]);
                return true;
            }

        });
        container.addView(vRoot);
        return vRoot;
    }

    /**
     * 调起
     */
    private void callAlertDialog(final String imageLocalPath) {

        String[] items = {"保存图片", "分享图片"};
        new AlertDialog.Builder(activity)
                .setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        saveImage(imageLocalPath);
                                        break;
                                    case 1:
                                        shareImage(imageLocalPath);
                                        break;
                                }
                            }
                        }
                ).show();
    }

    private void saveImage(String imageLocalPath) {
        if (imageLocalPath != null) {
            ShareImage.getInstance().saveImage(activity, imageLocalPath);
        } else {
            Snackbar.make(activity.view, "图片下载失败", Snackbar.LENGTH_LONG).show();
        }
    }

    private void shareImage(String imageLocalPath) {
        if (imageLocalPath != null) {
            ShareImage.getInstance().shareImage(activity, imageLocalPath);
        } else {
            Snackbar.make(activity.view, "图片下载失败", Snackbar.LENGTH_LONG).show();
        }
    }

    private Observable<File> downloadFile(final Uri imageUri) {
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(final Subscriber<? super File> subscriber) {
                final File outputFile = new File(Tool.getCacheDir(),
                        Tool.getFileName(imageUri.toString()));
                if (outputFile.exists()) {
                    subscriber.onNext(outputFile);
                    return;
                }

                Request request = new Request.Builder().url(String.valueOf(imageUri)).build();
                new OkHttpClient().newCall(request)
                        .enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                subscriber.onError(e);
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                BufferedSource source = response.body().source();
                                Sink sink = Okio.sink(outputFile);
                                source.readAll(sink);
                                sink.flush();
                                source.close();
                                sink.close();
                                subscriber.onNext(outputFile);
                            }
                        });

            }
        })
                .subscribeOn(Schedulers.io());
    }

}
