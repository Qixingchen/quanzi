package com.tizi.quanzi.tool;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
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

    public void shareImage(Activity activity, Bitmap bitmap, String fileName) {
        shareImage(activity, bitmap, fileName, false);
    }

    public void shareImage(final Activity activity, final Bitmap bitmap, final String fileName, boolean ignorePermission) {
        String RootPath;
        if (!ignorePermission && ActivityCompat.checkSelfPermission(App.getApplication(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            lastBitMap = bitmap;
            lastFileName = fileName;
            lastActivity = activity;

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(String.format("%s申请储存权限", StaticField.AppName.AppZHName))
                    .setMessage(String.format(activity.getString(R.string.storage_detail),
                            StaticField.AppName.AppZHName))
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    StaticField.PermissionRequestCode.shareImage);
                        }
                    }).setNegativeButton("不好", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    shareImage(activity, bitmap, fileName, true);
                }
            }).setNeutralButton("查看权限说明", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent permissionNotice = new Intent(Intent.ACTION_VIEW);
                    permissionNotice.setData(Uri.parse(activity.getString(R.string.uses_permission)));
                    if (Tool.isIntentSafe(activity, permissionNotice)) {
                        activity.startActivity(permissionNotice);
                    }
                }
            }).show();


            return;
        }
        if (useProvider()) {
            RootPath = App.getApplication().getCacheDir().toString();
        } else {
            RootPath = App.getApplication().getExternalCacheDir().toString();
        }
        String filePath = RootPath + "/image/" + fileName;
        ZipPic.saveMyBitmap(filePath, bitmap, 100);
        shareImage(activity, filePath);
    }

    public void shareImage(Activity activity, String filePath) {
        Intent shareIntent = new Intent();
        if (useProvider()) {
            Uri contentUri = FileProvider.getUriForFile(App.getApplication(),
                    App.getApplication().getPackageName(), new File(filePath));
            App.getApplication().grantUriPermission(App.getApplication().getPackageName(),
                    contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            shareIntent.setData(contentUri);
            shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
            shareIntent.setData(Uri.parse(filePath));
            shareIntent.setType("image/*");
        }
        activity.startActivity(Intent.createChooser(shareIntent, "分享图像"));
    }

    /**
     * 是否使用内容提供器(仅在M上未获得储存权限时使用)
     */
    private boolean useProvider() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat
                .checkSelfPermission(App.getApplication(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(App.getApplication(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
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
            case StaticField.PermissionRequestCode.shareImage:
                shareImage(lastActivity, lastBitMap, lastFileName, true);
        }

    }

}
