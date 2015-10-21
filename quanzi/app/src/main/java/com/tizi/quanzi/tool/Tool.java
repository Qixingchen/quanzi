package com.tizi.quanzi.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.gson.ApiInfoGson;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.ApiInfo;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.ui.LoginActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by qixingchen on 15/7/20.
 * 工具类
 */
public class Tool {

    private static final String TAG = Tool.class.getSimpleName();

    /**
     * 检查是否存在SDCard
     *
     * @return SD是否可用
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取本设备应当显示的图像大小（0.6屏幕）
     *
     * @param context 上下文
     * @param Heigh   图片高
     * @param Weith   图片宽
     *
     * @return 计算得到的图片高，宽
     */
    public static int[] getImagePixel(Context context, int Heigh, int Weith) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        //宽度最大0.6
        int imageMaxWidth = dm.widthPixels * 2 / 5;
        int[] imagePixel = new int[2];
        if (Weith > imageMaxWidth) {
            imagePixel[1] = imageMaxWidth;
            Double imageHei = imageMaxWidth * 1.0 / Weith * Heigh;
            imagePixel[0] = imageHei.intValue();
        } else {
            imagePixel[0] = Heigh;
            imagePixel[1] = Weith;
        }
        return imagePixel;
    }

    /**
     * 判断当前用户是不是游客
     */
    public static boolean isGuest() {
        return MyUserInfo.getInstance().getUserInfo().getAccount().compareTo(StaticField.GuestUser.Account) == 0;
    }

    public static void GuestAction(final Context context) {
        new AlertDialog.Builder(context).setTitle("此功能需要登录")
                .setMessage("AAAAA")
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent login = new Intent(context, LoginActivity.class);
                        context.startActivity(login);
                    }
                })
                .setNegativeButton("再看看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    /**
     * utf-8 转换
     */
    public static String getUTF_8String(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 手机号码转换
     * 去除所有的 － 和“ ”
     * 去除＋86
     * 取最后11位
     * 如果不是手机号，则返回null
     *
     * @param phoneNum 需处理的手机号
     *
     * @return 处理后的手机号，如不是手机号，则返回null
     */
    @Nullable
    public static String getPhoneNum(String phoneNum) {
        String ans = phoneNum.replaceAll("-", "");
        ans = ans.replaceAll(" ", "");
        ans = ans.replaceFirst("/+86", "");
        if (ans.length() > 11) {
            int len = ans.length();
            ans = ans.substring(len - 11);
        }
        if (ans.length() == 11 && ans.startsWith("1")) {
            return ans;
        } else {
            return null;
        }
    }

    /**
     * 刷新时间差
     */
    public static void flushTimeDifference() {
        final long beforeMS = Calendar.getInstance().getTimeInMillis();
        ApiInfo.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                ApiInfoGson apiInfo = (ApiInfoGson) ts;
                long afterMS = Calendar.getInstance().getTimeInMillis();
                AppStaticValue.timeAddtion = Long.parseLong(apiInfo.info.time) - beforeMS - (afterMS - beforeMS) / 2;
                Log.i("时间差：", String.valueOf(AppStaticValue.timeAddtion));
            }

            @Override
            public void onError(String Message) {

            }
        }).getAPiinfo();
    }

    /**
     * 从地址获取文件名
     *
     * @param filePath 文件地址
     *
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        int last = filePath.lastIndexOf("/");
        return filePath.substring(last + 1);
    }

    public static long getBeijinTime() {
        return Calendar.getInstance().getTimeInMillis() + AppStaticValue.timeAddtion;
    }

    /**
     * Hide keyboard on touch of UI
     */
    public static void hideKeyboard(View view, final Activity activity) {

        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                hideKeyboard(innerView, activity);
            }
        }
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(v, activity);
                    return false;
                }

            });
        }

    }

    /**
     * Hide keyboard while focus is moved
     */
    private static void hideSoftKeyboard(View view, Activity activity) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                if (android.os.Build.VERSION.SDK_INT < 11) {
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                            0);
                } else {
                    if (activity.getCurrentFocus() != null) {
                        inputManager.hideSoftInputFromWindow(activity
                                        .getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    view.clearFocus();
                }
                view.clearFocus();
            }
        }
    }


}
