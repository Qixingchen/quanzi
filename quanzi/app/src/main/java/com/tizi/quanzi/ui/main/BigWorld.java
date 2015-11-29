package com.tizi.quanzi.ui.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.network.UserInfoSetting;
import com.tizi.quanzi.otto.ActivityResultAns;
import com.tizi.quanzi.tool.GetShareIntent;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.SaveImageToLeanCloud;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.dyns.DynsActivity;
import com.tizi.quanzi.ui.login.LoginActivity;

import java.io.File;

/**
 */
public class BigWorld extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView userName, userSign;
    private NetworkImageView userFace;
    private ImageView userSex, userBackground;
    private View Share, Setting, userInfoLayout, friendZone;
    private Button logout;
    private RequreForImage requreForImage;


    public BigWorld() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment BigWorld.
     */
    public static BigWorld newInstance(String param1, String param2) {
        BigWorld fragment = new BigWorld();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_big_world, container,
                false);
        return rootView;
    }


    @Override
    protected void findViews(View view) {
        userFace = (NetworkImageView) view.findViewById(R.id.userFace);
        userName = (TextView) view.findViewById(R.id.userName);
        userSex = (ImageView) view.findViewById(R.id.userSex);
        userSign = (TextView) view.findViewById(R.id.userSign);
        Share = view.findViewById(R.id.share);
        Setting = view.findViewById(R.id.setting);
        userInfoLayout = view.findViewById(R.id.userInfoLayout);
        logout = (Button) view.findViewById(R.id.log_out);
        userBackground = (ImageView) view.findViewById(R.id.user_background);
        friendZone = view.findViewById(R.id.friend_zone);
    }

    @Override
    protected void initViewsAndSetEvent() {

        if (Tool.isGuest()) {
            logout.setText("登录");
        }

        Login.UserEntity userInfo = MyUserInfo.getInstance().getUserInfo();
        if (userInfo == null || userInfo.getIcon() == null) {
            return;
        }
        userFace.setImageUrl(userInfo.getIcon(),
                GetVolley.getmInstance().getImageLoader());

        if (userInfo.bg == null) {
            Picasso.with(mContext).load(R.drawable.face).resize(1080, 608).into(userBackground);
        } else {
            Picasso.with(mContext).load(userInfo.bg).resize(1080, 608).into(userBackground);
        }

        userName.setText(userInfo.getUserName());

        if (userInfo.getSex() == 0) {
            Picasso.with(mActivity).load(R.drawable.man).into(userSex);
        } else {
            Picasso.with(mActivity).load(R.drawable.girl).into(userSex);
        }
        userSign.setText(userInfo.getSignature());
        userInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tool.isGuest()) {
                    Tool.GuestAction(mActivity);
                } else {
                    ((MainActivity) AppStaticValue.getActivity(MainActivity.class.getSimpleName()))
                            .StartUserInfoSet();
                }
            }
        });

        friendZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tool.isGuest()) {
                    Tool.GuestAction(mContext);
                }
                Intent dyn = new Intent(mContext, DynsActivity.class);
                dyn.putExtra("isUser", true);
                startActivity(dyn);
            }
        });

        Setting.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SystemSettingFragment fragment = new SystemSettingFragment();
                        getParentFragment().getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.disapear,
                                        R.anim.no_change, R.anim.slide_out_to_bottom)
                                .replace(R.id.fragment, fragment).addToBackStack("SystemSettingFragment")
                                .commit();
                    }
                }
        );

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetShareIntent.startShare(mContext);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Tool.isGuest()) {
                    signOut();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("确认登出").setPositiveButton("登出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                    }
                }).setNegativeButton("取消", null).show();
            }
        });

        userBackground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                requreForImage = RequreForImage.getInstance(mActivity);
                requreForImage.showDialogAndCallIntent("选择背景", StaticField.PermissionRequestCode.user_back_ground);

                return false;
            }
        });

    }

    private void signOut() {
        AppStaticValue.setStringPrefer(StaticField.Preferences.PASSWORD, "");
        AppStaticValue.getImClient().close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e != null) {
                    Snackbar.make(view, "退出失败:" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
        Intent log_in = new Intent(mActivity, LoginActivity.class);
        log_in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(log_in);
    }

    @Subscribe
    public void onResult(ActivityResultAns ans) {

        if (ans.resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (ans.requestCode) {
            case StaticField.PermissionRequestCode.user_back_ground:

                String filepath = requreForImage.getFilePathFromIntent(ans.data, false);
                File file = new File(filepath);
                requreForImage.startPhotoCrop(Uri.fromFile(file), 9, 16,
                        StaticField.PermissionRequestCode.user_back_ground_crop);
                break;

            case StaticField.PermissionRequestCode.user_back_ground_crop:

                filepath = requreForImage.getCropImage().getPath();

                SaveImageToLeanCloud.getNewInstance().setGetImageUri(new SaveImageToLeanCloud.GetImageUri() {
                    @Override
                    public void onResult(String uri, boolean success, String errorMessage) {
                        if (success) {
                            Picasso.with(mContext).load(uri).into(userBackground);
                            MyUserInfo.getInstance().getUserInfo().bg = uri;
                            UserInfoSetting.getNewInstance().changeBackground(uri);
                        } else {
                            Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }).savePhoto(filepath);

                break;
            default:

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
