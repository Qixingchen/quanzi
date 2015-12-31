package com.tizi.quanzi.tool;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;

import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.tizi.quanzi.BuildConfig;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.log.Log;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by qixingchen on 15/12/15.
 * 解析多张照片
 */
public class GetMutipieImage {
    private final static String TAG = GetMutipieImage.class.getSimpleName();
    private OnImageGet onImageGet;

    public GetMutipieImage setOnImageGet(OnImageGet onImageGet) {
        this.onImageGet = onImageGet;
        return this;
    }

    public void getMutipieImage(Intent data) {
        getMutipieImage(data, Integer.MAX_VALUE);
    }

    public void getMutipieImage(Intent data, int max) {
        if (onImageGet == null) {
            Log.e(TAG, "onImageGet is Null");
            return;
        }

        ArrayList<Image> images = data
                .getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
        if (images != null) {
            if (images.size() > max) {
                onImageGet.Error(String.format("选择数量超过%d张,%d张未保存", max, images.size() - max));
                int overPhotoNum = images.size() - max;
                int deleteStart = images.size() - overPhotoNum;
                int deleteEnd = images.size() - 1;
                for (int i = deleteEnd; i >= deleteStart; i--) {
                    images.remove(i);
                }
            }
            for (final Image image : images) {
                onImageGet.OK(image.path);
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(ZipPic.getNewInstance().compressByWidth(image.path, StaticField.Limit.IMAGE_WIDTH,
                                StaticField.Limit.IMAGE_QUALITY));
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                onImageGet.OK(s);
                            }
                        });
            }
        } else {
            photoFromSystem(data, max);
        }
    }

    /**
     * 使用系统带的方法获取图片
     */
    private void photoFromSystem(Intent data, int max) {
        final ClipData clipData = data.getClipData();
        if (clipData != null) {
            int size = data.getClipData().getItemCount();
            if (size > max) {
                onImageGet.Error(String.format("选择数量超过%d张,%d张未保存", max, size - max));
            }
            final int finalSize = Math.min(size, max);
            Observable.create(new Observable.OnSubscribe<Uri>() {
                @Override
                public void call(Subscriber<? super Uri> subscriber) {
                    for (int i = 0; i < finalSize; i++) {
                        subscriber.onNext(clipData.getItemAt(i).getUri());
                    }
                    subscriber.onCompleted();
                }
            }).flatMap(new Func1<Uri, Observable<String>>() {
                @Override
                public Observable<String> call(final Uri uri) {
                    return Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(Subscriber<? super String> subscriber) {
                            subscriber.onNext(RequreForImage.getImageUrlWithAuthority(App.getApplication(), uri));
                        }
                    });
                }
            }).flatMap(new Func1<String, Observable<String>>() {
                @Override
                public Observable<String> call(final String s) {
                    return Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(Subscriber<? super String> subscriber) {
                            if (s == null) {
                                subscriber.onError(new Throwable("文件不存在"));
                            }
                            subscriber.onNext(ZipPic.getNewInstance().compressByWidth(s, StaticField.Limit.IMAGE_WIDTH,
                                    StaticField.Limit.IMAGE_QUALITY));
                        }
                    });
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            onImageGet.OK(s);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (BuildConfig.DEBUG) {
                                onImageGet.Error(throwable.getMessage());
                            } else {
                                onImageGet.Error("图片解析错误");
                            }
                            throwable.printStackTrace();
                        }
                    });
        } else {
            String filepath = RequreForImage.getFilePathFromIntent(data);
            filepath = ZipPic.getNewInstance().compressByWidth(filepath, StaticField.Limit.IMAGE_WIDTH,
                    StaticField.Limit.IMAGE_QUALITY);
            onImageGet.OK(filepath);
        }
    }

    public interface OnImageGet {
        void OK(String filePath);

        void Error(String errorMessage);
    }

}
