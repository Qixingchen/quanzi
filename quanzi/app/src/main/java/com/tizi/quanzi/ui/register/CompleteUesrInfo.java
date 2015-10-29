package com.tizi.quanzi.ui.register;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.SaveImageToLeanCloud;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * 用户填写个人信息界面
 **/
public class CompleteUesrInfo extends BaseFragment {

    /* 请求码 */
    private static final String IMAGE_FILE_NAME = "faceImage";
    private TextInputLayout nickNameInputLayout;
    private NetworkImageView UserPhotoImageView;
    private RadioGroup sexGroup;
    private Button submitButton;
    private String photoTakenUri;
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
        UserPhotoImageView = (NetworkImageView) view.findViewById(R.id.UserPhotoImageView);
        sexGroup = (RadioGroup) view.findViewById(R.id.sexGroup);
        submitButton = (Button) view.findViewById(R.id.submit_button);
    }

    @Override
    protected void initViewsAndSetEvent() {
        UserPhotoImageView.setImageResource(R.drawable.face);
        requreForImage = new RequreForImage(mActivity);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = nickNameInputLayout.getEditText().getText().toString();
                if (username.compareTo("") == 0) {
                    Snackbar.make(mActivity.findViewById(R.id.register_fragment),
                            "用户名为空", Snackbar.LENGTH_LONG).show();
                    return;
                }
                int buttonID = sexGroup.getCheckedRadioButtonId();
                if (buttonID == -1) {
                    Snackbar.make(mActivity.findViewById(R.id.register_fragment),
                            "性别为空", Snackbar.LENGTH_LONG).show();
                    return;
                }
                int sexID = sexGroup.indexOfChild(sexGroup.findViewById(buttonID));
                if (photoOnlineUri == null || photoOnlineUri.compareTo("") == 0) {
                    Snackbar.make(mActivity.findViewById(R.id.register_fragment),
                            "头像为空", Snackbar.LENGTH_LONG).show();
                    return;
                }

                allDone.CompUserInfoOK(username, sexID, photoOnlineUri);
            }
        });
        UserPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requreForImage.showDialogAndCallIntent("设置头像",
                        StaticField.PermissionRequestCode.register_user_face);
            }
        });
    }

    /**
     * Intent回调
     *
     * @param requestCode 请求码
     * @param resultCode  结果码（是否完成）
     * @param data        数据
     */
    public void onIntentResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String filePath = requreForImage.ZipedFilePathFromIntent(data);
            if (filePath != null) {
                SaveImageToLeanCloud.getNewInstance().setGetImageUri(new SaveImageToLeanCloud.GetImageUri() {
                    @Override
                    public void onResult(String uri, boolean success, String errorMessage) {
                        if (!success) {
                            Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        UserPhotoImageView.setBackground(null);
                        UserPhotoImageView.setImageUrl(uri,
                                GetVolley.getmInstance().getImageLoader());
                        photoOnlineUri = uri;
                    }
                }).savePhoto(filePath, 200, 200);
            }
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
