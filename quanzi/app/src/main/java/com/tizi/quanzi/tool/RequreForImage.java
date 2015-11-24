package com.tizi.quanzi.tool;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.soundcloud.android.crop.Crop;
import com.squareup.otto.Subscribe;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.otto.PermissionAnser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by qixingchen on 15/8/14.
 * 拍照或者选取图片
 */
public class RequreForImage {

    private static RequreForImage mInstance;
    private String[] items = new String[]{"选择图片", "拍照"};
    private String photoTakenUri;
    private Activity mActivity;
    private int lastEventCode;
    private Uri cropImage;

    private RequreForImage() {
        try {
            BusProvider.getInstance().register(this);
        } catch (IllegalArgumentException ignore) {
        }
    }

    public static RequreForImage getInstance(Activity activity) {
        if (mInstance == null) {
            synchronized (RequreForImage.class) {
                if (mInstance == null) {
                    mInstance = new RequreForImage();
                }
            }
        }
        mInstance.mActivity = activity;
        return mInstance;
    }

    /*get Image Path*/
    public static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bmp = BitmapFactory.decodeStream(is, null, options);
                return writeToTempImageAndGetPathUri(context, bmp, getImageFileName(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String writeToTempImageAndGetPathUri(Context context, Bitmap inImage, String fileName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String RootPath = context.getCacheDir().getAbsolutePath();
        String FilePath = RootPath + "/image/" + fileName;
        ZipPic.saveMyBitmap(FilePath, inImage, 100);

        return FilePath;
    }

    private static String getImageFileName(Uri uri) {
        String fileName = Tool.getFileName(uri.toString());
        if (!fileName.contains(".")) {
            fileName += ".jpg";
        }
        return fileName;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            BusProvider.getInstance().unregister(this);
        } catch (IllegalArgumentException ignore) {
        }
    }

    /**
     * 显示选择对话框(不允许多选)
     *
     * @param Title     对话框标题
     * @param eventCode 授权事件代码 {@link com.tizi.quanzi.tool.StaticField.PermissionRequestCode}
     */
    public void showDialogAndCallIntent(String Title, int eventCode) {
        showDialogAndCallIntent(Title, eventCode, false, 1);
    }

    /**
     * 显示选择对话框
     *
     * @param Title         对话框标题
     * @param eventCode     授权事件代码 {@link com.tizi.quanzi.tool.StaticField.PermissionRequestCode}
     * @param allowMultiple 是否允许多选
     * @param selectLimit   允许采集的最大数量
     */
    public void showDialogAndCallIntent(String Title, final int eventCode, final boolean allowMultiple, final int selectLimit) {
        lastEventCode = eventCode;
        new AlertDialog.Builder(mActivity)
                .setTitle(Title)
                .setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        if (allowMultiple) {
                                            intentForMultiple(selectLimit, eventCode);
                                        } else {
                                            intentForSingle(eventCode);
                                        }
                                        break;
                                    case 1:
                                        takePhoto(eventCode, false);
                                        break;
                                }
                            }
                        }

                ).show();
    }

    /*发起单选*/
    private void intentForSingle(int eventCode) {
        Intent intentFromGallery = new Intent(Intent.ACTION_GET_CONTENT, null);
        intentFromGallery.setType("image/*"); // 设置文件类型
        mActivity.startActivityForResult(intentFromGallery, eventCode);
    }

    /*发起多选,包括如果本身应该是多选,只是因为已经选了多张而使得limit=1的情况*/
    private void intentForMultiple(final int limit, final int eventCode) {
        int selector = AppStaticValue.getIntPrefer("MULTIPLE_SELECTOR", 9);
        if (selector == 9) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

            builder.setMessage("我们将优先使用系统照片选择器,如果您发现无法多选,请点击\"使用自带选择器\"")
                    .setTitle("选择器的提示");

            builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intentForSysMultiple(eventCode);
                }
            });
            builder.setNegativeButton("使用自带选择器", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intentFor3pMultiple(limit, eventCode);
                    AppStaticValue.setIntPrefer("MULTIPLE_SELECTOR", 0);
                }
            });
            builder.setNeutralButton("没遇到问题", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intentForSysMultiple(eventCode);
                    AppStaticValue.setIntPrefer("MULTIPLE_SELECTOR", 1);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (selector == 0) {
            intentFor3pMultiple(limit, eventCode);
        }
        if (selector == 1) {
            intentForSysMultiple(eventCode);
        }
    }

    /*系统多选*/
    private void intentForSysMultiple(int eventCode) {
        Intent intentFromGallery = new Intent(Intent.ACTION_GET_CONTENT, null);
        intentFromGallery.setType("image/*");
        intentFromGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        mActivity.startActivityForResult(intentFromGallery, eventCode);
    }

    /*第三方库多选*/
    private void intentFor3pMultiple(int limit, int eventCode) {
        Intent intentFromGallery = new Intent(mActivity, AlbumSelectActivity.class);
        intentFromGallery.putExtra(Constants.INTENT_EXTRA_LIMIT, limit);
        mActivity.startActivityForResult(intentFromGallery, eventCode);
    }

    /**
     * 裁剪图片
     *
     * @param file           图片地址
     * @param saveFile       储存图片地址
     * @param hei            图片高度(与宽度的比例)
     * @param wei            图片宽度
     * @param permissionCode 授权回调码
     */
    public void startPhotoCrop(Uri file, Uri saveFile, int hei, int wei, int permissionCode) {
        cropImage = saveFile;
        Crop.of(file, saveFile).withAspect(wei, hei).start(mActivity, permissionCode);
    }

    public void startPhotoCrop(Uri file, int hei, int wei, int permissionCode) {
        startPhotoCrop(file, getZoomedPicPath(file), hei, wei, permissionCode);
    }

    /**
     * 获取裁剪后的图片
     */
    public Uri getCropImage() {
        return cropImage;
    }

    /**
     * 获取需裁剪图片的储存位置
     */
    private Uri getZoomedPicPath(Uri file) {

        String RootPath = mActivity.getCacheDir().getAbsolutePath();
        String FilePath = RootPath + "/image/" + "crop_" + getImageFileName(file);
        File saved = new File(FilePath);
        if (!saved.getParentFile().exists()) {
            saved.getParentFile().mkdirs();
        }
        try {
            saved.createNewFile();
        } catch (IOException e) {
            Log.e("在保存图片时出错：", e.toString());
        }
        return Uri.fromFile(saved);
    }

    /**
     * 根据用户ID和时间建立临时图片
     *
     * @param isExternal 是否储存在外置空间
     *
     * @return 创建的文件
     */
    private File createImageFile(boolean isExternal) {
        // Create an image file name
        String imageFileName = String.valueOf(new Date().getTime() / 1000) + ".jpg";
        String rootDir;
        if (isExternal) {
            rootDir = mActivity.getExternalCacheDir().getAbsolutePath();
        } else {
            rootDir = mActivity.getCacheDir().getAbsolutePath();
        }

        String Dirpath = rootDir + "/image/" + AppStaticValue.getUserID();
        File storageDir = new File(Dirpath);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        photoTakenUri = image.getAbsolutePath();
        return image;
    }

    /**
     * 获取本地图片并压缩
     *
     * @param data 图片信息
     */
    public String getFilePathFromIntent(Intent data) {
        return getFilePathFromIntent(data, true);
    }

    public String getFilePathFromIntent(Intent data, boolean needZip) {

        String FilePath;
        if (data == null || data.getData() == null) {
            FilePath = photoTakenUri;
        } else {
            FilePath = getImageUrlWithAuthority(mActivity, data.getData());
        }

        if (ZipPic.getSize(FilePath) < 150 * 1024) {
            return FilePath;
        }

        boolean compass = true;
        if (compass && needZip) {
            FilePath = ZipPic.saveMyBitmap(mActivity, ZipPic.compressBySize(FilePath, 960), 50);
        }
        return FilePath;

    }

    private void takePhoto(int eventCode, boolean ignorePermission) {
        if (!ignorePermission) {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, eventCode);
                return;
            }
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, eventCode);
                return;
            }
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(
                mActivity.getPackageManager()) != null) {
            Uri uri;

            if (hasPermission()) {
                // Create the File where the photo should go
                File photoFile = createImageFile(true);
                uri = Uri.fromFile(photoFile);
            } else {
                File photoFile = createImageFile(false);
                uri = FileProvider.getUriForFile(mActivity,
                        App.getApplication().getPackageName(), photoFile);
                mActivity.grantUriPermission(App.getApplication().getPackageName(),
                        uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            // Continue only if the File was successfully created
            if (uri != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                mActivity.startActivityForResult(takePictureIntent, eventCode);
            }
        } else {
            Toast.makeText(mActivity, "诶!没有发现相机呢!", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 取得授权
     *
     * @param permission 需要授权的权限
     */
    private void requestPermission(String permission, int eventCode) {
        ActivityCompat.requestPermissions(mActivity, new String[]{permission}, eventCode);
    }

    /*授权回调*/
    @Subscribe
    public void onRequestPermissionsResult(PermissionAnser permissionAnser) {
        if (lastEventCode == permissionAnser.requestCode) {
            takePhoto(lastEventCode, true);
        }
    }

    private boolean hasPermission() {

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
}