package com.tizi.quanzi.ui.dyns;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.avos.avoscloud.AVFile;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.GroupSelectAdapter;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.gson.Pics;
import com.tizi.quanzi.network.DynamicAct;
import com.tizi.quanzi.otto.ActivityResultAns;
import com.tizi.quanzi.tool.GetMutipieImage;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.SaveImageToLeanCloud;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendDynFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendDynFragment extends BaseFragment {
    private static final String THEME_STRING = "themeString";
    private static final String THEME_ID = "themeID";
    private static final String GROUP_ID = "groupID";
    private static final String IS_USER = "isUser";
    private static final int IMAGE_MAX_SIZE = StaticField.Limit.DYN_IMAGE_SIZE;

    private String themeString;
    private String themeID;
    private String groupID;
    private boolean isUser;


    private android.widget.EditText dynComment;
    private View[] weibo_pics = new View[IMAGE_MAX_SIZE];
    private android.support.v7.widget.RecyclerView groupItemRecyclerview;
    private GroupSelectAdapter groupSelectAdapter;
    private ImageButton selectPhoto;
    private String selectGroupID;
    private ArrayMap<AVFile, Boolean> photoUploading = new ArrayMap<>(IMAGE_MAX_SIZE + 6);
    private ArrayMap<AVFile, String> photoLocal = new ArrayMap<>(3 * IMAGE_MAX_SIZE);


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
    public static SendDynFragment newInstance(String themeString, String groupID, String ThemeID, boolean isUser) {
        SendDynFragment fragment = new SendDynFragment();
        Bundle args = new Bundle();
        args.putString(THEME_STRING, themeString);
        args.putString(THEME_ID, groupID);
        args.putString(GROUP_ID, ThemeID);
        args.putBoolean(IS_USER, isUser);
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
        if (photoUploading.containsValue(false)) {
            Snackbar.make(view, "正在上传照片中!", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (comment.compareTo("") == 0) {
            Snackbar.make(view, "状态为空", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (!isUser && selectGroupID == null) {
            Snackbar.make(view, "没有选择群", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (photoUploading.size() == 0) {
            DynamicAct.getNewInstance(isUser).addDYn(themeID, selectGroupID, comment);
        } else {
            ArrayList<Pics> pics = new ArrayList<>();
            for (AVFile photo : photoUploading.keySet()) {
                pics.add(new Pics(photo.getUrl()));
            }
            DynamicAct.getNewInstance(isUser).addDYn(themeID, selectGroupID, comment,
                    new Gson().toJson(pics));
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            themeString = getArguments().getString(THEME_STRING);
            groupID = getArguments().getString(THEME_ID);
            themeID = getArguments().getString(GROUP_ID);
            isUser = getArguments().getBoolean(IS_USER, false);
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
        if (groupID == null && !isUser) {
            groupSelectAdapter = new GroupSelectAdapter(GroupList.getInstance().getGroupList(), mActivity, null,
                    GroupSelectAdapter.Dyn_Select_Group);
            groupSelectAdapter.setOnclick(new GroupSelectAdapter.Onclick() {
                @Override
                public void itemClick(String groupID) {
                    selectGroupID = groupID;
                }
            });
            groupItemRecyclerview.setAdapter(groupSelectAdapter);
            groupItemRecyclerview.setLayoutManager(new GridLayoutManager(mActivity, Tool.getSrceenWidthDP() / 90));
        } else {
            selectGroupID = groupID;
            groupItemRecyclerview.setVisibility(View.GONE);
            view.findViewById(R.id.select_group).setVisibility(View.GONE);
        }

        for (int i = 0; i < IMAGE_MAX_SIZE; i++) {
            final int finalI = i;
            weibo_pics[i].findViewById(R.id.delete_photo).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    photoUploading.keyAt(finalI).cancel();
                    photoUploading.keyAt(finalI).deleteInBackground();
                    photoUploading.removeAt(finalI);
                    flushImages();
                }
            });
        }

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoUploading.size() >= IMAGE_MAX_SIZE) {
                    Snackbar.make(view, String.format("您已经选择%d张照片了~", IMAGE_MAX_SIZE), Snackbar.LENGTH_LONG).show();
                    return;
                }
                RequreForImage.getInstance(mActivity).showDialogAndCallIntent("选择照片",
                        StaticField.PermissionRequestCode.send_dyn, true, IMAGE_MAX_SIZE - photoUploading.size());
            }
        });

    }

    @Subscribe
    public void onActivityResult(ActivityResultAns activityResultAns) {
        if (activityResultAns.requestCode == StaticField.PermissionRequestCode.send_dyn
                && activityResultAns.resultCode == Activity.RESULT_OK && activityResultAns.data != null) {
            new GetMutipieImage().setOnImageGet(new GetMutipieImage.OnImageGet() {
                @Override
                public void OK(String FilePath) {
                    savePhoto(FilePath);
                }

                @Override
                public void Error(String errorMessage) {
                    Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
                }
            }).getMutipieImage(activityResultAns.data, IMAGE_MAX_SIZE - photoUploading.size());
        }
    }


    private void flushImages() {
        int size = photoUploading.size();
        for (int i = 0; i < Math.min(9, size); i++) {
            weibo_pics[i].setVisibility(View.VISIBLE);
            Picasso.with(mContext).load("file://" + photoLocal.get(photoUploading.keyAt(i)))
                    .fit().centerInside()
                    .into((ImageView) (weibo_pics[i].findViewById(R.id.pic)));
            if (photoUploading.valueAt(i)) {
                (weibo_pics[i].findViewById(R.id.pic)).setAlpha(1.0f);
            } else {
                (weibo_pics[i].findViewById(R.id.pic)).setAlpha(0.3f);
            }
        }
        for (int i = size; i < 9; i++) {
            weibo_pics[i].setVisibility(View.GONE);
        }
    }

    private void savePhoto(String filepath) {
        AVFile avFile = SaveImageToLeanCloud.getNewInstance().setGetImageUri(new SaveImageToLeanCloud.GetImageUri() {
            @Override
            public void onResult(String uri, boolean success, String errormess, AVFile avFile1) {
                if (success) {
                    int index = photoUploading.indexOfKey(avFile1);
                    if (index < 0) {
                        return;
                    }
                    photoUploading.setValueAt(index, true);
                    flushImages();
                } else {
                    Snackbar.make(view, errormess, Snackbar.LENGTH_LONG).show();
                }
            }
        }).savePhoto(filepath);
        photoUploading.put(avFile, false);
        photoLocal.put(avFile, filepath);
        flushImages();

    }

}
