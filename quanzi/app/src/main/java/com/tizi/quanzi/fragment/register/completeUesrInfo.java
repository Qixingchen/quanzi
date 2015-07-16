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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tizi.quanzi.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteUesrInfo extends Fragment {

    private Activity mActivity;

    private TextInputLayout nickNameInputLayout;
    private ImageView UserPhotoImageView;
    private RadioGroup sexGroup;
    private String UserfaceUri;

    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private String[] items = new String[]{"选择本地图片", "拍照"};
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";

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
        UserPhotoImageView = (ImageView) mActivity.findViewById(R.id.UserPhotoImageView);
        sexGroup = (RadioGroup) mActivity.findViewById(R.id.sexGroup);
        Button submitButton = (Button) mActivity.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allDone.CompUserInfoOK(nickNameInputLayout.getEditText().getText().toString(),
                        sexGroup.getCheckedRadioButtonId(), UserfaceUri);
            }
        });
        UserPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public interface AllDone {
        void CompUserInfoOK(String userNane, int sex, String faceUri);
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
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*"); // 设置文件类型
                                intentFromGallery
                                        .setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intentFromGallery,
                                        IMAGE_REQUEST_CODE);
                                break;
                            case 1:

                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);

                                startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                intentFromCapture.putExtra(
                                        MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(new File(
                                                mActivity.getCacheDir(), IMAGE_FILE_NAME
                                        )));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == mActivity.RESULT_OK) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (Tool.hasSdcard()) {
                        File tempFile = new File(
                                mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    }

                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(context
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                UserPhoneNumber)));
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            fbm = drawable;
            faceImage.setImageURI(Uri.fromFile(new File(context
                    .getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    UserPhoneNumber)));
            File oldphoto = new File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    IMAGE_FILE_NAME);
            if (oldphoto.exists()) {
                oldphoto.deleteOnExit();
            }
        }
    }

}
