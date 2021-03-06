package com.tizi.quanzi.ui.new_group;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.AllTags;
import com.tizi.quanzi.otto.ActivityResultAns;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.SaveImageToLeanCloud;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewGroupStep1Fragment extends BaseFragment {

    private RequreForImage requreForImage;
    private android.support.design.widget.TextInputLayout quanziNameInputLayout, quanziSignInputLayout;
    private ImageView UserPhotoImageView;
    private String groupFaceUri;
    private TextView tagView;


    private ArrayList<AllTags.TagsEntity> selectTags = new ArrayList<>();

    public NewGroupStep1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_group_step_1, container, false);
    }

    @Override
    protected void findViews(View view) {

        quanziNameInputLayout = (TextInputLayout) mActivity.findViewById(R.id.quanziNameInputLayout);
        quanziSignInputLayout = (TextInputLayout) mActivity.findViewById(R.id.quanziSignInputLayout);
        UserPhotoImageView = (ImageView) mActivity.findViewById(R.id.UserPhotoImageView);
        tagView = (TextView) view.findViewById(R.id.quanzi_tag);
    }

    @Override
    protected void initViewsAndSetEvent() {

        UserPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requreForImage = RequreForImage.getInstance(mActivity);
                requreForImage.showDialogAndCallIntent("圈子头像",
                        StaticField.PermissionRequestCode.new_group_face_photo);
            }
        });

        tagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewGroupActivity) mActivity).CallForTagFragment(selectTags);
            }
        });
    }

    /**
     * Intent回调
     */
    @Subscribe
    public void onIntentResult(ActivityResultAns activityResultAns) {

        if (activityResultAns.resultCode != Activity.RESULT_OK || activityResultAns.data == null) {
            return;
        }
        switch (activityResultAns.requestCode) {
            case StaticField.PermissionRequestCode.new_group_face_photo:
                String filePath = requreForImage.getFilePathFromIntentMaybeCamera(activityResultAns.data);

                requreForImage.startPhotoCrop(Uri.fromFile(new File(filePath)), 1, 1,
                        StaticField.PermissionRequestCode.new_group_face_photo_crop);
                break;

            case StaticField.PermissionRequestCode.new_group_face_photo_crop:

                SaveImageToLeanCloud.getNewInstance().setGetImageUri(new SaveImageToLeanCloud.GetImageUri() {
                    @Override
                    public void onResult(String uri, boolean success, String errorMessage, AVFile avFile) {
                        if (!success) {
                            Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        String photoUri = uri;
                        //finalFile.getThumbnailUrl(false, 200, 200);
                        Picasso.with(mActivity).load(photoUri)
                                .fit()
                                .into(UserPhotoImageView);
                        groupFaceUri = photoUri;
                    }
                }).savePhoto(requreForImage.getCropImage().getPath(), AppStaticValue.getUserID() + "group.jpg");

                break;
        }
    }

    /**
     * 获取本页的结果
     *
     * @return NewGroupStep1Ans
     */
    public NewGroupStep1Ans getNewGroupAns() {
        NewGroupStep1Ans ans = new NewGroupStep1Ans();
        ans.groupName = quanziNameInputLayout.getEditText().getText().toString();
        ans.groupSign = quanziSignInputLayout.getEditText().getText().toString();
        ans.groupFaceUri = groupFaceUri;
        ans.tags = selectTags;
        if (ans.groupName == null || ans.groupFaceUri == null) {
            ans.complete = false;
            return ans;
        }
        ans.complete = (!ans.groupName.isEmpty() && !ans.groupFaceUri.isEmpty());
        return ans;
    }

    public void setTags(ArrayList<AllTags.TagsEntity> tags) {
        selectTags = tags;
        String tagsString = "";
        for (AllTags.TagsEntity tag : tags) {
            tagsString += tag.tagName + ",";
        }
        if (tagsString.equals("")) {
            tagsString = "圈子标签";
        }
        tagView.setText(tagsString);
    }

    /**
     * 本页的结果组
     */
    public class NewGroupStep1Ans {
        public boolean complete;
        public String groupName;
        public String groupSign;
        public String groupFaceUri;
        public ArrayList<AllTags.TagsEntity> tags;

    }

}
