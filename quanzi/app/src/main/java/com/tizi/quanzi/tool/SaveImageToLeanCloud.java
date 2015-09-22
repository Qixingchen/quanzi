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

<<<<<<< HEAD
    private GetImageUri getImageUri;
    private static final String TAG = SaveImageToLeanCloud.class.getSimpleName();
=======
    private static final String TAG = SaveImageToLeanCloud.class.getSimpleName();
    private GetImageUri getImageUri;

    public static SaveImageToLeanCloud getNewInstance() {
        return new SaveImageToLeanCloud();
    }
>>>>>>> origin/master

    public SaveImageToLeanCloud setGetImageUri(GetImageUri getImageUri) {
        this.getImageUri = getImageUri;
        return this;
    }

<<<<<<< HEAD
    public static SaveImageToLeanCloud getNewInstance() {
        return new SaveImageToLeanCloud();
    }

=======
>>>>>>> origin/master
    /**
     * 将图片储存到LeanCloud
     * 图片名称使用文件名
     * 不限制图片最大大小
     *
     * @param filePath 图片地址
     */
    public void savePhoto(String filePath) {
        savePhoto(filePath, getFileName(filePath));
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
        savePhoto(filePath, getFileName(filePath), maxWei, maxHei);
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
<<<<<<< HEAD
        AVFile file = null;
=======
        AVFile file;
>>>>>>> origin/master
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
<<<<<<< HEAD
                        String photoUri = "";
=======
                        String photoUri;
>>>>>>> origin/master
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

<<<<<<< HEAD

    public interface GetImageUri {
        void onResult(String uri, boolean success);
    }

=======
    /**
     * 从图片地址获取文件名
     *
     * @param filePath 文件地址
     *
     * @return 文件名
     */
>>>>>>> origin/master
    private String getFileName(String filePath) {
        int last = filePath.lastIndexOf("/");
        return filePath.substring(last + 1);
    }
<<<<<<< HEAD
=======

    public interface GetImageUri {
        void onResult(String uri, boolean success);
    }
>>>>>>> origin/master
}
