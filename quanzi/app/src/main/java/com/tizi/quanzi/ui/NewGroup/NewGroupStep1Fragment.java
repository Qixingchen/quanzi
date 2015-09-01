package com.tizi.quanzi.ui.NewGroup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.tool.RequreForImage;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewGroupStep1Fragment extends Fragment {

    private Activity mActivity;
    private android.support.design.widget.TextInputLayout quanziNameInputLayout, quanziSignInputLayout;
    private ImageView UserPhotoImageView;
    RequreForImage requreForImage;

    private String groupFaceUri;

    public NewGroupStep1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_group_step_1, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        requreForImage = new RequreForImage(mActivity);
        quanziNameInputLayout = (TextInputLayout) mActivity.findViewById(R.id.quanziNameInputLayout);
        quanziSignInputLayout = (TextInputLayout) mActivity.findViewById(R.id.quanziSignInputLayout);
        UserPhotoImageView = (ImageView) mActivity.findViewById(R.id.UserPhotoImageView);
        UserPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requreForImage.showDialogAndCallIntent("圈子头像");
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
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
                savePhoto(filePath);
            }
        }
    }

    /**
     * 授权回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requreForImage.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 将图片储存到LeanCloud
     *
     * @param filepath 图片地址
     */
    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    private void savePhoto(String filepath) {
        AVFile file = null;
        try {
            file = AVFile.withAbsoluteLocalPath(App.getUserID() + "group.jpg",
                    filepath);
            final AVFile finalFile = file;
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e != null) {
                        //上传失败
                    } else {
                        String photoUri = finalFile.getThumbnailUrl(false, 200, 200);
                        Picasso.with(mActivity).load(photoUri)
                                //todo getsize
                                .resize(GetThumbnailsUri.getPXs(mActivity, 48), GetThumbnailsUri.getPXs(mActivity, 48))
                                .into(UserPhotoImageView);
                        groupFaceUri = photoUri;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NewGroupStep1Ans getNewGroupAns() {
        NewGroupStep1Ans ans = new NewGroupStep1Ans();
        ans.groupName = quanziNameInputLayout.getEditText().getText().toString();
        ans.groupSign = quanziSignInputLayout.getEditText().getText().toString();
        ans.groupFaceUri = groupFaceUri;
        ans.complete = (!ans.groupSign.isEmpty() && !ans.groupName.isEmpty() && !ans.groupFaceUri.isEmpty());
        // TODO: 15/8/28 test
        ans.complete = true;
        return ans;
    }

    public class NewGroupStep1Ans {
        public boolean complete;
        public String groupName;
        public String groupSign;
        public String groupFaceUri;

    }

}
