package com.tizi.quanzi.fragment.register;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.GetFilePath;

import java.io.File;
import java.io.IOException;

public class CompleteUesrInfo extends Fragment {

    private Activity mActivity;

    private TextInputLayout nickNameInputLayout;
    private NetworkImageView UserPhotoImageView;
    private RadioGroup sexGroup;

    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private String[] items = new String[]{"选择本地图片", "拍照"};
    private static final String IMAGE_FILE_NAME = "faceImage";
    private String photoTakenUri;
    private String photoOnlineUri;

    private AllDone allDone;

    public void setAllDone(AllDone allDone) {
        this.allDone = allDone;
    }

    public CompleteUesrInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete_uesr_info, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        nickNameInputLayout = (TextInputLayout) mActivity.findViewById(R.id.nickNameInputLayout);
        UserPhotoImageView = (NetworkImageView) mActivity.findViewById(R.id.UserPhotoImageView);
        UserPhotoImageView.setImageResource(R.drawable.face);
        sexGroup = (RadioGroup) mActivity.findViewById(R.id.sexGroup);
        Button submitButton = (Button) mActivity.findViewById(R.id.submit_button);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allDone.CompUserInfoOK(nickNameInputLayout.getEditText().getText().toString(),
                        sexGroup.getCheckedRadioButtonId(), photoOnlineUri);
            }
        });
        UserPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    /**
     * 本界面完成回调借口
     */
    public interface AllDone {
        void CompUserInfoOK(String userName, int sex, String faceUri);
    }

    /**
     * 显示选择对话框
     */
    private void showDialog() {

        new AlertDialog.Builder(mActivity)
                .setTitle("设置头像")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentFromGallery = new Intent(Intent.ACTION_GET_CONTENT, null);
                                intentFromGallery.setType("image/*"); // 设置文件类型
                                mActivity.startActivityForResult(intentFromGallery,
                                        IMAGE_REQUEST_CODE);
                                break;
                            case 1:
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // Ensure that there's a camera activity to handle the intent
                                if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                                    // Create the File where the photo should go
                                    File photoFile = null;
                                    photoFile = createImageFile();
                                    // Continue only if the File was successfully created
                                    if (photoFile != null) {
                                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                Uri.fromFile(photoFile));
                                        mActivity.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                                    }
                                }
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    public void onIntentResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == mActivity.RESULT_OK) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    savePhoto(GetFilePath.getPath(mActivity, data.getData()));
                    break;
                case CAMERA_REQUEST_CODE:
                    savePhoto(photoTakenUri);
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
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
        mActivity.startActivityForResult(intent, RESULT_REQUEST_CODE);

    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            //todo 提交图像
            Snackbar.make(mActivity.findViewById(R.id.register_fragment),
                    "照片处理完成", Snackbar.LENGTH_LONG).show();
        }
    }

    private File createImageFile() {
        // Create an image file name
        String imageFileName = "JPEG_" + "123";
        File storageDir = mActivity.getCacheDir();
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

    private void savePhoto(String filepath) {
        AVFile file = null;
        try {
            file = AVFile.withAbsoluteLocalPath(App.getUserID() + "face.jpg",
                    filepath);
            final AVFile finalFile = file;
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e != null) {
                        //上传失败
                    } else {
                        String photoUri = finalFile.getThumbnailUrl(false, 200, 200);
                        UserPhotoImageView.setImageUrl(photoUri,
                                GetVolley.getmInstance(mActivity).getImageLoader());
                        photoOnlineUri = photoUri;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
