package com.tizi.quanzi.tool;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.tizi.quanzi.log.Log;

import java.io.IOException;

/**
 * Created by qixingchen on 15/9/22.
 * 将图片储存至LC服务器，并返回地址
 */
public class SaveImageToLeanCloud {

    private static final String TAG = SaveImageToLeanCloud.class.getSimpleName();
    private GetImageUri getImageUri;

    public static SaveImageToLeanCloud getNewInstance() {
        return new SaveImageToLeanCloud();
    }

    public SaveImageToLeanCloud setGetImageUri(GetImageUri getImageUri) {
        this.getImageUri = getImageUri;
        return this;
    }

    /**
     * 将图片储存到LeanCloud
     * 图片名称使用文件名
     * 不限制图片最大大小
     *
     * @param filePath 图片地址
     */
    public void savePhoto(String filePath) {
        savePhoto(filePath, Tool.getFileName(filePath));
    }

    /**
     * 将图片储存到LeanCloud
     * 不限制图片最大大小
     *
     * @param filePath 图片地址
     * @param fileName 图片储存的名称
     */
    public void savePhoto(String filePath, String fileName) {
        savePhoto(filePath, fileName, 0, 0);
    }

    /**
     * 将图片储存到LeanCloud
     *
     * @param filePath 图片地址 图片名称使用文件名
     * @param maxWei   获取的图片链接最大宽度
     * @param maxHei   获取的图片链接最大高度
     */
    public void savePhoto(String filePath, int maxWei, int maxHei) {
        savePhoto(filePath, Tool.getFileName(filePath), maxWei, maxHei);
    }

    /**
     * 将图片储存到LeanCloud
     *
     * @param filePath 图片地址
     * @param fileName 图片储存的名称
     * @param maxWei   获取的图片链接最大宽度
     * @param maxHei   获取的图片链接最大高度
     */
    public void savePhoto(final String filePath, final String fileName, final int maxWei, final int maxHei) {
        AVFile file;
        try {
            file = AVFile.withAbsoluteLocalPath(fileName, filePath);
            final AVFile finalFile = file;
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e != null) {
                        Log.w(TAG, String.format("图片%s:%s储存失败", filePath, fileName));
                        if (getImageUri != null) {
                            getImageUri.onResult(null, false);
                        }
                    } else {
                        String photoUri;

                        if (maxWei != 0) {
                            photoUri = finalFile.getThumbnailUrl(false, maxWei, maxHei);
                        } else {
                            photoUri = finalFile.getUrl();
                        }
                        if (getImageUri != null) {
                            getImageUri.onResult(photoUri, true);
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            if (getImageUri != null) {
                getImageUri.onResult(null, false);
            }
        }
    }

    public interface GetImageUri {
        void onResult(String uri, boolean success);
    }
}