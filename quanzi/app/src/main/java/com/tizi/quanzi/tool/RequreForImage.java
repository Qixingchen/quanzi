package com.tizi.quanzi.tool;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

import com.tizi.quanzi.app.App;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by qixingchen on 15/8/14.
 */
public class RequreForImage {

    private String[] items = new String[]{"选择本地图片", "拍照"};
    private static final String IMAGE_FILE_NAME = "faceImage";
    private String photoTakenUri;

    public RequreForImage(Activity mActivity) {
        this.mActivity = mActivity;
    }

    private Activity mActivity;


    /**
     * 显示选择对话框
     */
    public void showDialogAndCallIntent(String Title) {

        new AlertDialog.Builder(mActivity)
                .setTitle(Title)
                .setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intentFromGallery = new Intent(Intent.ACTION_GET_CONTENT, null);
                                        intentFromGallery.setType("image/*"); // 设置文件类型
                                        mActivity.startActivityForResult(intentFromGallery,
                                                StaticField.ImageRequreCode.IMAGE_REQUEST_CODE);
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


    /**
     * todo 裁剪图片方法实现
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
     * todo 保存裁剪之后的图片数据
     */
    public void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            //todo 提交图像
        }
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private File createImageFile() {
        // Create an image file name
        String imageFileName = String.valueOf(new Date().getTime() / 1000) + ".jpg";

        File file = new File(mActivity.getExternalCacheDir().getAbsolutePath()
                + "/image/" + App.getUserID(), imageFileName);


        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }


        photoTakenUri = file.getAbsolutePath();


        File storageDir = mActivity.getExternalCacheDir();
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        photoTakenUri = image.getAbsolutePath();
        return image;
    }

    public String FilePathFromIntent(Intent data) {
        if (data.getData() == null) {
            return photoTakenUri;
        }
        return GetFilePath.getPath(mActivity, data.getData());
    }

}
