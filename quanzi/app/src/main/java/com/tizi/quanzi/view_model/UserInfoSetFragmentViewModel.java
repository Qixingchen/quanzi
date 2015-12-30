package com.tizi.quanzi.view_model;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
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

import com.avos.avoscloud.AVFile;
import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.GetLocationFromBaidu;
import com.tizi.quanzi.network.UserInfoSetting;
import com.tizi.quanzi.otto.ActivityResultAns;
import com.tizi.quanzi.otto.PermissionAnser;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.SaveImageToLeanCloud;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Timer;
import com.tizi.quanzi.ui.main.UserInfoSetFragment;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by qixingchen on 15/12/30.
 */
public class UserInfoSetFragmentViewModel extends BaseViewModel<UserInfoSetFragment> implements Observable {

    private final static String TAG = UserInfoSetFragmentViewModel.class.getSimpleName();
    private PropertyChangeRegistry pcr = new PropertyChangeRegistry();
    private Login.UserEntity user;
    private RequreForImage requreForImage;
    private boolean isGetLocation;
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

    public UserInfoSetFragmentViewModel(UserInfoSetFragment userInfoSetFragment, Login.UserEntity user) {
        super(userInfoSetFragment);
        this.user = user;
    }

    @Bindable
    public View.OnClickListener getUserFaceOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requreForImage = RequreForImage.getInstance(mActivity);
                requreForImage.showDialogAndCallIntent("选择头像",
                        StaticField.PermissionRequestCode.userInfoSetFragment_user_face_photo);
            }
        };
    }

    @Bindable
    public View.OnClickListener getUserNameOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                LayoutInflater inflater = mActivity.getLayoutInflater();
                final View layout = inflater.inflate(R.layout.dialog_one_line,
                        (ViewGroup) mActivity.findViewById(R.id.dialog_one_line));
                final EditText input = (EditText) layout.findViewById(R.id.dialog_edit_text);
                builder.setView(layout);

                input.setHint("输入昵称");
                input.setText(user.getUserName());
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        UserInfoSetting.getNewInstance().changeName(name);
                        user.setUserName(name);
                    }
                });
                builder.setTitle("修改昵称").show();
            }
        };
    }

    @Bindable
    public View.OnClickListener getUserSexOnClick() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] position = {0};
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setSingleChoiceItems(new String[]{"男", "女"}, user.getSex(),
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
            }
        };
    }

    @Bindable
    public View.OnClickListener getUserAgeOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                if (new GregorianCalendar(year, monthOfYear + 1, dayOfMonth).getTime().getTime()
                                        > new Date().getTime()) {
                                    Snackbar.make(mActivity.view, "您不能选择未来的时间", Snackbar.LENGTH_LONG).show();
                                    return;
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
            }
        };
    }

    @Bindable
    public View.OnClickListener getUserLocationOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.w(TAG, "没有位置权限");
                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            StaticField.PermissionRequestCode.userInfoSetFragment_location);
                    return;
                }
                LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                Log.i(TAG, "定位中");
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, mLocationListener, null);
                mFragment.setProgressBarVisibility(View.VISIBLE);
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
            }
        };
    }

    @Bindable
    public View.OnClickListener getUserSignOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                LayoutInflater inflater = mActivity.getLayoutInflater();
                final View layout = inflater.inflate(R.layout.dialog_one_line,
                        (ViewGroup) mActivity.findViewById(R.id.dialog_one_line));
                final EditText input = (EditText) layout.findViewById(R.id.dialog_edit_text);
                builder.setView(layout);

                input.setHint("输入签名");
                input.setText(user.getSignature());
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfoSetting.getNewInstance().changeSign(input.getText().toString());
                        user.setSignature(input.getText().toString());
                    }
                });
                builder.setTitle("修改签名").show();
            }
        };
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
                if (mFragment.isAttached) {
                    mFragment.setProgressBarVisibility(View.GONE);
                    Snackbar.make(mActivity.view, errorMessage, Snackbar.LENGTH_LONG).show();
                }
            }
        }).getLocation();
    }

    private void setLocation(String area, double Longitude, double Latitude) {
        UserInfoSetting.getNewInstance().changeLatitude(Latitude);
        UserInfoSetting.getNewInstance().changeLongitude(Longitude);
        Log.i(TAG, String.format("Latitude:%f,Longitude:%f", Latitude, Longitude));
        UserInfoSetting.getNewInstance().changeArea(area);
        user.setArea(area);
        if (!mFragment.isAttached) {
            return;
        }
        isGetLocation = true;
        mFragment.setProgressBarVisibility(View.GONE);
    }

    @Subscribe
    public void onImageGet(ActivityResultAns activityResultAns) {
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
                            user.setIcon(uri);
                            UserInfoSetting.getNewInstance().changeFace(uri);
                        } else {
                            Snackbar.make(mActivity.view, errorMessage, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }).savePhoto(requreForImage.getCropImage().getPath(),
                        user.getId() + "face.jpg");
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
                LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                Log.i(TAG, "定位中");
                //noinspection ResourceType
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, mLocationListener, null);
                break;
            default:
                Log.e(TAG, "unKnow permission code" + permissionAnser.requestCode);
                break;
        }
    }


    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        pcr.add(callback);
    }

    /**
     * Removes a callback from those listening for changes.
     *
     * @param callback The callback that should stop listening.
     */
    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        pcr.remove(callback);
    }
}
