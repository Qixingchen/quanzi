package com.tizi.quanzi.ui.main;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.UserInfoSetting;
import com.tizi.quanzi.ui.BaseFragment;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoSetFragment extends BaseFragment implements View.OnClickListener {

    private TextView userFaceTextView;
    private TextView userNameTextView;
    private TextView userSexTextView;
    private TextView userAgeTextView;
    private TextView userLocationTextView;
    private TextView userSignTextView;
    private Calendar calendar = Calendar.getInstance();
    private LocationManager locationManager;


    public UserInfoSetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info_set, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void findViews(View view) {
        view.findViewById(R.id.userFace).setOnClickListener(this);
        userFaceTextView = (TextView) view.findViewById(R.id.userFaceTextView);
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
    }

    @Override
    protected void initViewsAndSetEvent() {
        userFaceTextView.setText(MyUserInfo.getInstance().getUserInfo().getIcon());
        userNameTextView.setText(MyUserInfo.getInstance().getUserInfo().getUserName());
        userSexTextView.setText(String.valueOf(MyUserInfo.getInstance().getUserInfo().getSex()));
        userAgeTextView.setText(MyUserInfo.getInstance().getUserInfo().getBirthday());
        userLocationTextView.setText(MyUserInfo.getInstance().getUserInfo().getArea());
        userSignTextView.setText(MyUserInfo.getInstance().getUserInfo().getSignature());
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        LayoutInflater inflater = mActivity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_one_line,
                (ViewGroup) mActivity.findViewById(R.id.one_line_dialog));
        final EditText input = (EditText) layout.findViewById(R.id.dialog_edit_text);
        TextView title = (TextView) layout.findViewById(R.id.dialog_title);
        builder.setView(layout).setNegativeButton("取消", null);

        switch (view.getId()) {
            case R.id.userFace:
                title.setText("输入好友的账号");
                input.setHint("账号");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userFaceTextView.setText(input.getText().toString());
                    }
                });
                builder.setTitle("添加好友").show();
                break;
            case R.id.userName:
                title.setText("输入好友的账号");
                input.setHint("账号");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userNameTextView.setText(input.getText().toString());
                    }
                });
                builder.setTitle("添加好友").show();
                break;
            case R.id.userSex:
                title.setText("输入好友的账号");
                input.setHint("账号");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userSexTextView.setText(input.getText().toString());
                    }
                });
                builder.setTitle("添加好友").show();
                break;
            case R.id.userAge:
                new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        userAgeTextView.setText(String.valueOf(year)
                                + String.valueOf(monthOfYear + 1) + String.valueOf(dayOfMonth));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.userLocation:
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.w(TAG, "没有位置权限");
                    return;
                }
                Log.i(TAG, "定位中");
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, mLocationListener, null);

                break;
            case R.id.userSign:
                title.setText("输入好友的账号");
                input.setHint("账号");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userSignTextView.setText(input.getText().toString());
                    }
                });
                builder.setTitle("添加好友").show();
                break;
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            Log.i(TAG, "定位完成");
            try {
                List<Address> address = new Geocoder(mContext).getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String area = address.get(0).getAdminArea() + address.get(0).getLocality();
                userLocationTextView.setText(area);
                UserInfoSetting.getNewInstance().changeArea(area);
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


}

