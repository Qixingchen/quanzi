package com.tizi.quanzi.tool;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.otto.PermissionAnser;

import java.io.File;

/**
 * Created by qixingchen on 15/11/13.
 * 分享图片
 */
public class ShareImage {

    private static ShareImage mInstance;
    private Bitmap lastBitMap;
    private String lastFileName;
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
        shareImage(activity, filePath);
    }

    private void shareImage(Activity activity, String filePath) {
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

        if (ActivityCompat.checkSelfPermission(App.getApplication(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            lastBitMap = bitmap;
            lastFileName = fileName;
            lastActivity = activity;
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    StaticField.PermissionRequestCode.saveImageToExternalStorage);
            return;
        }
        String RootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        String FilePath = RootPath + "/" + StaticField.AppName.AppEngName + "/" + fileName;
        ZipPic.saveMyBitmap(FilePath, bitmap, 100);
        Toast.makeText(activity, "保存成功在：" + FilePath, Toast.LENGTH_LONG).show();

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(FilePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }

    @Subscribe
    public void onPermission(PermissionAnser permissionAnser) {
        switch (permissionAnser.requestCode) {
            case StaticField.PermissionRequestCode.saveImageToExternalStorage:
                if (permissionAnser.allGreen) {
                    saveImage(lastActivity, lastBitMap, lastFileName);
                }
                lastActivity = null;
                lastFileName = null;
                lastBitMap = null;
                break;
        }

    }

}
