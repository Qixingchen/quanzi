package com.tizi.quanzi.ui.main;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.databinding.FragmentUserInfoSetBinding;
import com.tizi.quanzi.gson.AllTags;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.gson.UserTags;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.GetLocationFromBaidu;
import com.tizi.quanzi.network.GroupSetting;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.UserInfoSetting;
import com.tizi.quanzi.otto.ActivityResultAns;
import com.tizi.quanzi.otto.PermissionAnser;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.SaveImageToLeanCloud;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Timer;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.new_group.GroupTagFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import me.next.tagview.TagCloudView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoSetFragment extends BaseFragment implements View.OnClickListener {

    private TextView userNameTextView;
    private TextView userSexTextView;
    private TextView userAgeTextView;
    private TextView userLocationTextView;
    private ImageView userFaceImageView;
    private TextView userSignTextView;
    private Calendar calendar = Calendar.getInstance();
    private LocationManager locationManager;
    private RequreForImage requreForImage;
    private ProgressBar progressBar;
    private boolean isGetLocation;
    private FragmentUserInfoSetBinding binding;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            Log.i(TAG, "系统定位完成");
            try {
                double Latitude = location.getLatitude();
                double Longitude = location.getLongitude();
                List<Address> address = new Geocoder(mContext).getFromLocation(Latitude, Longitude, 1);
                String area = address.get(0).getAdminArea() + address.get(0).getLocality();
                setLocation(area, Longitude, Latitude);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private TagCloudView userTagView;
    private ArrayList<AllTags.TagsEntity> myTag;

    public UserInfoSetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info_set, container, false);
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_user_info_set, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void findViews(View view) {
        userFaceImageView = (ImageView) view.findViewById(R.id.user_face_image);
        view.findViewById(R.id.userface).setOnClickListener(this);
        view.findViewById(R.id.userName).setOnClickListener(this);
        userNameTextView = (TextView) view.findViewById(R.id.userNameTextView);
        view.findViewById(R.id.userSex).setOnClickListener(this);
        userSexTextView = (TextView) view.findViewById(R.id.userSexTextView);
        view.findViewById(R.id.userAge).setOnClickListener(this);
        userAgeTextView = (TextView) view.findViewById(R.id.userAgeTextView);
        view.findViewById(R.id.userLocation).setOnClickListener(this);
        userLocationTextView = (TextView) view.findViewById(R.id.userLocationTextView);
        view.findViewById(R.id.userSign).setOnClickListener(this);
        userSignTextView = (TextView) view.findViewById(R.id.userSignTextView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        userTagView = (TagCloudView) view.findViewById(R.id.user_tag_view);
        view.findViewById(R.id.user_tag).setOnClickListener(this);
    }

    @Override
    protected void initViewsAndSetEvent() {
        binding.setUser(MyUserInfo.getInstance().getUserInfo());

        GroupSetting.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                UserTags tags = (UserTags) ts;
                List<String> tagStrings = new ArrayList<>();
                myTag = new ArrayList<>(tags.usertags);
                for (AllTags.TagsEntity tag : tags.usertags) {
                    tagStrings.add(tag.tagName);
                }
                userTagView.setTags(tagStrings);
            }

            @Override
            public void onError(String Message) {

            }
        }).findUserTags(AppStaticValue.getUserID());
    }

    @Override
    public void onClick(View view) {

        final Login.UserEntity user = binding.getUser();

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        LayoutInflater inflater = mActivity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_one_line,
                (ViewGroup) mActivity.findViewById(R.id.dialog_one_line));
        final EditText input = (EditText) layout.findViewById(R.id.dialog_edit_text);
        builder.setView(layout);

        switch (view.getId()) {
            case R.id.userface:
                requreForImage = RequreForImage.getInstance(mActivity);
                requreForImage.showDialogAndCallIntent("选择头像",
                        StaticField.PermissionRequestCode.userInfoSetFragment_user_face_photo);
                break;
            case R.id.userName:
                input.setHint("输入昵称");
                input.setText(userNameTextView.getText());
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        UserInfoSetting.getNewInstance().changeName(name);
                        user.setUserName(name);
                    }
                });
                builder.setTitle("修改昵称").show();
                break;
            case R.id.userSex:
                final int[] position = {0};
                AlertDialog.Builder builder2 = new AlertDialog.Builder(mActivity);
                builder2.setSingleChoiceItems(new String[]{"男", "女"}, user.getSex(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        position[0] = 0;
                                        break;
                                    case 1:
                                        position[0] = 1;
                                        break;
                                }
                            }
                        }).setTitle("选择性别").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfoSetting.getNewInstance().changeSex(String.valueOf(position[0]));
                        user.setSex(position[0]);
                    }
                }).show();
                break;
            case R.id.userAge:
                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                if (new GregorianCalendar(year, monthOfYear + 1, dayOfMonth).getTime().getTime()
                                        > new Date().getTime()) {
                                    Snackbar.make(view, "您不能选择未来的时间", Snackbar.LENGTH_LONG).show();
                                }
                                UserInfoSetting.getNewInstance().chagngeBirthday(year, monthOfYear + 1, dayOfMonth);
                                user.setBirthday(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH));
                if (user.getBirthday() != null) {
                    String date[] = user.getBirthday().split("-");
                    int year = Integer.valueOf(date[0]);
                    int month = Integer.valueOf(date[1]);
                    int day = Integer.valueOf(date[2]);
                    datePickerDialog.updateDate(year, month - 1, day);
                }

                datePickerDialog.show();
                break;
            case R.id.userLocation:
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.w(TAG, "没有位置权限");
                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            StaticField.PermissionRequestCode.userInfoSetFragment_location);
                    return;
                }
                locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                Log.i(TAG, "定位中");
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, mLocationListener, null);
                progressBar.setVisibility(View.VISIBLE);
                isGetLocation = false;
                new Timer().setOnResult(new Timer.OnResult() {
                    @Override
                    public void OK() {
                        if (!isGetLocation) {
                            Log.w(TAG, "系统定位超时");
                            getLocationFromBaidu();
                        }
                    }

                    @Override
                    public void countdown(long remainingS, long goneS) {

                    }
                }).setTimer(10 * 1000).start();
                break;
            case R.id.userSign:
                input.setHint("输入签名");
                input.setText(userSignTextView.getText());
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfoSetting.getNewInstance().changeSign(input.getText().toString());
                        user.setSignature(input.getText().toString());
                    }
                });
                builder.setTitle("修改签名").show();
                break;
            case R.id.user_tag:
                GroupTagFragment groupTagFragment = GroupTagFragment.newInstance(myTag, false);
                groupTagFragment.setOnOK(new GroupTagFragment.OnOK() {
                    @Override
                    public void OK(ArrayList<AllTags.TagsEntity> tags) {
                        List<String> tagStrings = new ArrayList<>();
                        for (AllTags.TagsEntity tag : tags) {
                            tagStrings.add(tag.tagName);
                        }
                        userTagView.setTags(tagStrings);
                        myTag = tags;
                        UserInfoSetting.getNewInstance().changeTag(tags);
                        getFragmentManager().popBackStack();
                    }
                });
                getFragmentManager().beginTransaction().hide(this)
                        .add(R.id.fragment, groupTagFragment)
                        .addToBackStack("GroupTagFragment").commit();
                break;
        }
    }

    @Subscribe
    public void onActivityResult(ActivityResultAns activityResultAns) {
        Log.i(TAG, "头像获取");
        if (activityResultAns.resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (activityResultAns.requestCode) {
            case StaticField.PermissionRequestCode.userInfoSetFragment_user_face_photo:
                String ans = requreForImage.getFilePathFromIntentMaybeCamera(activityResultAns.data);
                requreForImage.startPhotoCrop(Uri.fromFile(new File(ans)), 1, 1,
                        StaticField.PermissionRequestCode.userInfoSetFragment_user_face_photo_crop);
                break;

            case StaticField.PermissionRequestCode.userInfoSetFragment_user_face_photo_crop:

                SaveImageToLeanCloud.getNewInstance().setGetImageUri(new SaveImageToLeanCloud.GetImageUri() {
                    @Override
                    public void onResult(String uri, boolean success, String errorMessage, AVFile avFile) {
                        if (success) {
                            binding.getUser().setIcon(uri);
                            UserInfoSetting.getNewInstance().changeFace(uri);
                        } else {
                            Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }).savePhoto(requreForImage.getCropImage().getPath(),
                        binding.getUser().getId() + "face.jpg");
                break;
        }
    }

    @Subscribe
    public void onPermissionAns(PermissionAnser permissionAnser) {
        if (!permissionAnser.allGreen) {
            getLocationFromBaidu();
            return;
        }

        switch (permissionAnser.requestCode) {
            case StaticField.PermissionRequestCode.userInfoSetFragment_location:
                locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                Log.i(TAG, "定位中");
                //noinspection ResourceType
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, mLocationListener, null);
                break;
            default:
                Log.e(TAG, "unKnow permission code" + permissionAnser.requestCode);
                break;
        }
    }

    private void setLocation(String area, double Longitude, double Latitude) {
        UserInfoSetting.getNewInstance().changeLatitude(Latitude);
        UserInfoSetting.getNewInstance().changeLongitude(Longitude);
        Log.i(TAG, String.format("Latitude:%f,Longitude:%f", Latitude, Longitude));
        UserInfoSetting.getNewInstance().changeArea(area);
        binding.getUser().setArea(area);
        if (!isAttached) {
            return;
        }
        isGetLocation = true;
        progressBar.setVisibility(View.GONE);
    }

    private void getLocationFromBaidu() {
        GetLocationFromBaidu.getNewInstance(new GetLocationFromBaidu.onResult() {
            @Override
            public void ok(String area, Double Longitude, Double Latitude) {
                Log.i(TAG, "百度定位完成");
                setLocation(area, Longitude, Latitude);
            }

            @Override
            public void error(String errorMessage) {
                Log.i(TAG, "百度定位失败");
                if (isAttached) {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
                }
            }
        }).getLocation();
    }

}

