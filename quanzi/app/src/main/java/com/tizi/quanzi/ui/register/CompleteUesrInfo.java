package com.tizi.quanzi.ui.register;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.avos.avoscloud.AVFile;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.otto.ActivityResultAns;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.SaveImageToLeanCloud;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseFragment;

import java.io.File;

/**
 * 用户填写个人信息界面
 **/
public class CompleteUesrInfo extends BaseFragment {

    private TextInputLayout nickNameInputLayout;
    private ImageView UserPhotoImageView;
    private RadioGroup sexGroup;
    private Button submitButton;
    private String photoOnlineUri;

    private RequreForImage requreForImage;

    private AllDone allDone;

    public CompleteUesrInfo() {
        // Required empty public constructor
    }

    /**
     * 全部完成回调
     *
     * @see com.tizi.quanzi.ui.register.CompleteUesrInfo.AllDone
     */
    public void setAllDone(AllDone allDone) {
        this.allDone = allDone;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete_uesr_info, container, false);
    }

    @Override
    protected void findViews(View view) {
        nickNameInputLayout = (TextInputLayout) view.findViewById(R.id.nickNameInputLayout);
        UserPhotoImageView = (ImageView) view.findViewById(R.id.UserPhotoImageView);
        sexGroup = (RadioGroup) view.findViewById(R.id.sexGroup);
        submitButton = (Button) view.findViewById(R.id.submit_button);
    }

    @Override
    protected void initViewsAndSetEvent() {
        UserPhotoImageView.setImageResource(R.drawable.face);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = nickNameInputLayout.getEditText().getText().toString();
                if (username.compareTo("") == 0) {
                    Snackbar.make(view,
                            "用户名为空", Snackbar.LENGTH_LONG).show();
                    return;
                }
                int buttonID = sexGroup.getCheckedRadioButtonId();
                if (buttonID == -1) {
                    Snackbar.make(view,
                            "性别为空", Snackbar.LENGTH_LONG).show();
                    return;
                }
                int sexID = sexGroup.indexOfChild(sexGroup.findViewById(buttonID));
                if (photoOnlineUri == null || photoOnlineUri.compareTo("") == 0) {
                    Snackbar.make(view,
                            "头像为空", Snackbar.LENGTH_LONG).show();
                    return;
                }

                allDone.CompUserInfoOK(username, sexID, photoOnlineUri);
            }
        });
        UserPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requreForImage = RequreForImage.getInstance(mActivity);
                requreForImage.showDialogAndCallIntent("设置头像",
                        StaticField.PermissionRequestCode.register_user_face);
            }
        });
    }

    @Subscribe
    public void onResult(ActivityResultAns activityResultAns) {
        if (activityResultAns.resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (activityResultAns.requestCode) {
            case StaticField.PermissionRequestCode.register_user_face:
                String filePath = requreForImage.getFilePathFromIntent(activityResultAns.data);
                if (filePath == null) {
                    return;
                }
                requreForImage.startPhotoCrop(Uri.fromFile(new File(filePath)), 1, 1,
                        StaticField.PermissionRequestCode.register_user_face_crop);
                break;
            case StaticField.PermissionRequestCode.register_user_face_crop:


                SaveImageToLeanCloud.getNewInstance().setGetImageUri(new SaveImageToLeanCloud.GetImageUri() {
                    @Override
                    public void onResult(String uri, boolean success, String errorMessage, AVFile avFile) {
                        if (!success) {
                            Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        Picasso.with(mContext).load(uri).fit().into(UserPhotoImageView);
                        photoOnlineUri = uri;
                    }
                }).savePhoto(requreForImage.getCropImage().getPath());

                break;
        }
    }

    /**
     * 本界面完成回调借口
     */
    public interface AllDone {
        /**
         * @param userName 用户昵称
         * @param sex      性别
         * @param faceUri  头像
         */
        void CompUserInfoOK(String userName, int sex, String faceUri);
    }
}
