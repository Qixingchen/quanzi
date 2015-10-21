package com.tizi.quanzi.ui.dyns;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.toolbox.NetworkImageView;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.GroupSelectAdapter;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.gson.Pics;
import com.tizi.quanzi.network.DynamicAct;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.otto.ActivityResultAns;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.SaveImageToLeanCloud;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendDynFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendDynFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String themeString;
    private String themeID;
    private String groupID;


    private android.widget.EditText dynComment;
    private View[] weibo_pics = new View[9];
    private android.support.v7.widget.RecyclerView groupItemRecyclerview;
    private GroupSelectAdapter groupSelectAdapter;
    private ImageButton selectPhoto;
    private String selectGroupID;
    private ArrayList<String> photoUrls = new ArrayList<>();

    private int photoCount = 0;//用于统计已选择的照片数,photoUrls未上传时不会被添加


    public SendDynFragment() {
        // Required empty public constructor
    }

    /**
     * @param themeString 活动主题的文字
     * @param groupID     发起的群ID,可为null
     * @param ThemeID     主题ID
     *
     * @return A new instance of fragment SendDynFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SendDynFragment newInstance(String themeString, String groupID, String ThemeID) {
        SendDynFragment fragment = new SendDynFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, themeString);
        args.putString(ARG_PARAM2, groupID);
        args.putString(ARG_PARAM3, ThemeID);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 发送动态
     *
     * @return 是否可以发送了
     */
    public boolean SendDyn() {
        String comment = dynComment.getText().toString();
        if (comment.compareTo("") == 0 || selectGroupID == null) {
            Snackbar.make(view, "状态为空或没有选择群", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (photoCount != photoUrls.size()) {
            Snackbar.make(view, "正在上传照片中!", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (photoUrls.size() == 0) {
            DynamicAct.getNewInstance().addDYn(themeID, selectGroupID, comment);
        } else {
            ArrayList<Pics> pics = new ArrayList<>();
            for (String photoUrl : photoUrls) {
                pics.add(new Pics(photoUrl));
            }
            DynamicAct.getNewInstance().addDYn(themeID, selectGroupID, comment,
                    new Gson().toJson(pics));
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            themeString = getArguments().getString(ARG_PARAM1);
            groupID = getArguments().getString(ARG_PARAM2);
            themeID = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_dyn, container, false);
    }

    @Override
    protected void findViews(View view) {

        this.groupItemRecyclerview = (RecyclerView) view.findViewById(R.id.group_item_recycler_view);
        weibo_pics[0] = view.findViewById(R.id.weibo_pic0);
        weibo_pics[1] = view.findViewById(R.id.weibo_pic1);
        weibo_pics[2] = view.findViewById(R.id.weibo_pic2);
        weibo_pics[3] = view.findViewById(R.id.weibo_pic3);
        weibo_pics[4] = view.findViewById(R.id.weibo_pic4);
        weibo_pics[5] = view.findViewById(R.id.weibo_pic5);
        weibo_pics[6] = view.findViewById(R.id.weibo_pic6);
        weibo_pics[7] = view.findViewById(R.id.weibo_pic7);
        weibo_pics[8] = view.findViewById(R.id.weibo_pic8);
        this.dynComment = (EditText) view.findViewById(R.id.dyn_comment);
        dynComment.setText(themeString);
        selectPhoto = (ImageButton) view.findViewById(R.id.select_picture);
        flushImages();
    }

    @Override
    protected void initViewsAndSetEvent() {
        if (groupID == null) {
            groupSelectAdapter = new GroupSelectAdapter(GroupList.getInstance().getGroupList(), mActivity, null,
                    GroupSelectAdapter.Dyn_Select_Group);
            groupSelectAdapter.setOnclick(new GroupSelectAdapter.Onclick() {
                @Override
                public void itemClick(String groupID) {
                    selectGroupID = groupID;
                }
            });
            groupItemRecyclerview.setAdapter(groupSelectAdapter);
            groupItemRecyclerview.setLayoutManager(new GridLayoutManager(mActivity, 4));
        } else {
            selectGroupID = groupID;
            groupItemRecyclerview.setVisibility(View.GONE);
        }

        for (int i = 0; i < 9; i++) {
            final int finalI = i;
            weibo_pics[i].findViewById(R.id.delete_photo).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    photoUrls.remove(finalI);
                    photoCount--;
                    flushImages();
                }
            });
        }

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequreForImage(mActivity).showDialogAndCallIntent("选择照片",
                        StaticField.PermissionRequestCode.send_dyn, true, 9 - photoCount);
            }
        });

    }

    @Subscribe
    public void onActivityResult(ActivityResultAns activityResultAns) {
        if (activityResultAns.requestCode == StaticField.PermissionRequestCode.send_dyn
                && activityResultAns.resultCode == Activity.RESULT_OK && activityResultAns.data != null) {
            ArrayList<Image> images = activityResultAns.data
                    .getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            photoCount += images.size();
            for (Image image : images) {
                SaveImageToLeanCloud.getNewInstance().setGetImageUri(new SaveImageToLeanCloud.GetImageUri() {
                    @Override
                    public void onResult(String uri, boolean success) {
                        if (success) {
                            photoUrls.add(uri);
                            flushImages();
                        }
                    }
                }).savePhoto(image.path, image.name);
            }
        }
    }

    private void flushImages() {
        int size = photoUrls.size();
        for (int i = 0; i < size; i++) {
            weibo_pics[i].setVisibility(View.VISIBLE);
            String uri = GetThumbnailsUri.maxHeiAndWei(photoUrls.get(i), 270, 480);
            ((NetworkImageView) weibo_pics[i].findViewById(R.id.pic)).setImageUrl(uri, GetVolley.getmInstance().getImageLoader());
        }
        for (int i = size; i < 9; i++) {
            weibo_pics[i].setVisibility(View.GONE);
        }
    }

}
