package com.tizi.quanzi.tool;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.squareup.otto.Subscribe;
import com.tizi.quanzi.app.AppStaticValue;
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

    private static final String IMAGE_FILE_NAME = "faceImage";
    private String[] items = new String[]{"选择本地图片", "拍照"};
    private String photoTakenUri;
    private Activity mActivity;
    private String lastTitle;
    private int lastSelectLimit;
    private boolean lastAllowMultiple;


    public RequreForImage(Activity mActivity) {
        this.mActivity = mActivity;
        try {
            BusProvider.getInstance().register(this);
        } catch (IllegalArgumentException ignore) {
        }
    }

    /*For Google Photo*/
    private static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp);
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

    private static String writeToTempImageAndGetPathUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

        String RootPath = context.getCacheDir().getAbsolutePath();
        String FilePath = RootPath + "/image/" + "temp_group_image.jpg";
        ZipPic.saveMyBitmap(FilePath, inImage, 100);


        return FilePath;
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
        lastTitle = Title;
        lastSelectLimit = selectLimit;
        lastAllowMultiple = allowMultiple;

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
                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        // Ensure that there's a camera activity to handle the intent
                                        if (takePictureIntent.resolveActivity(
                                                mActivity.getPackageManager()) != null) {
                                            // Create the File where the photo should go
                                            File photoFile = createImageFile();

                                            // Continue only if the File was successfully created
                                            if (photoFile != null) {
                                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                        Uri.fromFile(photoFile));
                                                mActivity.startActivityForResult(takePictureIntent,
                                                        StaticField.ImageRequreCode.CAMERA_REQUEST_CODE);
                                            }
                                        }
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
    }

    /*发起单选*/
    private void intentForSingle(int eventCode) {
        Intent intentFromGallery = new Intent(Intent.ACTION_GET_CONTENT, null);
        intentFromGallery.setType("image/*"); // 设置文件类型
        mActivity.startActivityForResult(intentFromGallery, eventCode);
    }

    /*发起多选,包括如果本身应该是多选,只是因为已经选了多张而使得limit=1的情况*/
    private void intentForMultiple(final int limit, final int eventCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intentFor3pMultiple(limit, eventCode);
        }
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
     * todo 裁剪图片方法实现
     *
     * @param uri 图片地址
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        String path = GetFilePath.getPath(mActivity, uri);
        intent.setDataAndType(Uri.parse(path), "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mActivity
                .getExternalCacheDir(), IMAGE_FILE_NAME + "123333")));
        mActivity.startActivityForResult(intent, StaticField.ImageRequreCode.CUT_REQUEST_CODE);

    }

    /**
     * 根据用户ID和时间建立临时图片
     *
     * @return 创建的文件
     */
    private File createImageFile() {
        // Create an image file name
        String imageFileName = String.valueOf(new Date().getTime() / 1000) + ".jpg";

        String Dirpath = mActivity.getExternalCacheDir().getAbsolutePath() + "/image/" + AppStaticValue.getUserID();
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
    public String ZipedFilePathFromIntent(Intent data) {

        String FilePath;
        if (data == null || data.getData() == null) {
            FilePath = photoTakenUri;
        } else {
            FilePath = GetFilePath.getPath(mActivity, data.getData());
            if (FilePath == null) {
                return getImageUrlWithAuthority(mActivity, data.getData());
            }
        }

        if (ZipPic.getSize(FilePath) < 150 * 1024) {
            return FilePath;
        }

        boolean compass = true;
        if (compass) {
            FilePath = ZipPic.saveMyBitmap(mActivity, ZipPic.compressBySize(FilePath, 960), 50);
        }
        return FilePath;
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
        if (!permissionAnser.allGreen) {
            return;
        }
        if (StaticField.PermissionRequestCode.isImagePermissionEvent(permissionAnser.requestCode)) {

            showDialogAndCallIntent(lastTitle, permissionAnser.requestCode, lastAllowMultiple, lastSelectLimit);

        }
    }
}
