package com.tizi.quanzi.tool;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.tizi.quanzi.BuildConfig;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.otto.PermissionAnser;
import com.tizi.quanzi.ui.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by qixingchen on 15/11/13.
 * 分享图片
 */
public class ShareImage {

    private static ShareImage mInstance;
    private String lastFilePath;
    private Activity lastActivity;

    private ShareImage() {
        try {
            BusProvider.getInstance().register(this);
        } catch (Exception ignore) {
        }
    }

    public static ShareImage getInstance() {
        if (mInstance == null) {
            synchronized (ShareImage.class) {
                mInstance = new ShareImage();
            }
        }
        return mInstance;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            BusProvider.getInstance().unregister(this);
        } catch (Exception ignore) {
        }
    }

    public void shareImage(final Activity activity, final Bitmap bitmap, final String fileName) {
        String RootPath = App.getApplication().getCacheDir().getAbsolutePath();
        String filePath = RootPath + "/image/" + fileName;
        ZipPic.saveMyBitmap(filePath, bitmap, 100);
        shareSavedImage(activity, filePath);
    }

    public void shareImage(final Activity activity, final String srcFilePath) {
        String RootPath = App.getApplication().getCacheDir().getAbsolutePath();
        String filePath = RootPath + "/image/" + Tool.getFileName(srcFilePath);
        try {
            copy(new File(srcFilePath), new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
            String errorString;
            if (BuildConfig.DEBUG) {
                errorString = e.getMessage();
            } else {
                errorString = "保存错误";
            }
            Snackbar.make(((BaseActivity) activity).view, errorString, Snackbar.LENGTH_LONG).show();
            return;
        }
        shareSavedImage(activity, filePath);
    }

    private void shareSavedImage(Activity activity, String filePath) {
        Intent shareIntent = new Intent();

        Uri contentUri = FileProvider.getUriForFile(App.getApplication(),
                App.getApplication().getPackageName(), new File(filePath));
        App.getApplication().grantUriPermission(App.getApplication().getPackageName(),
                contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        shareIntent.setData(contentUri);
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.setType("image/*");

        activity.startActivity(Intent.createChooser(shareIntent, "分享图像"));
    }

    public void saveImage(Activity activity, Bitmap bitmap, String fileName) {

        String RootPath = Tool.getCacheCacheDir().getAbsolutePath();
        String FilePath = RootPath + "/" + StaticField.AppName.AppEngName + "/" + fileName;
        ZipPic.saveMyBitmap(FilePath, bitmap, 100);

        saveImage(activity, FilePath);
    }

    public void saveImage(Activity activity, String srcFilePath) {

        if (ActivityCompat.checkSelfPermission(App.getApplication(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            lastFilePath = srcFilePath;
            lastActivity = activity;
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    StaticField.PermissionRequestCode.saveImageToExternalStorage);
            return;
        }
        String fileName = Tool.getFileName(srcFilePath);
        String RootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        String dstfilePath = RootPath + "/" + StaticField.AppName.AppEngName + "/" + fileName;
        File dstFile = new File(dstfilePath);

        try {
            copy(new File(srcFilePath), dstFile);
        } catch (IOException e) {
            e.printStackTrace();
            String errorString;
            if (BuildConfig.DEBUG) {
                errorString = e.getMessage();
            } else {
                errorString = "保存错误";
            }
            Snackbar.make(((BaseActivity) activity).view, errorString, Snackbar.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(activity, "保存成功在：" + dstfilePath, Toast.LENGTH_LONG).show();

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(dstFile);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }


    private void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    @Subscribe
    public void onPermission(PermissionAnser permissionAnser) {
        switch (permissionAnser.requestCode) {
            case StaticField.PermissionRequestCode.saveImageToExternalStorage:
                if (permissionAnser.allGreen) {
                    saveImage(lastActivity, lastFilePath);
                }
                lastActivity = null;
                lastFilePath = null;
                break;
        }

    }

}
