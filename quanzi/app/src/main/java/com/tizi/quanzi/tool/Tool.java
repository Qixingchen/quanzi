package com.tizi.quanzi.tool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.gson.ApiInfoGson;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.ApiInfo;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.ui.LoginActivity;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

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
        int imageMaxWidth = dm.widthPixels * 3 / 5;
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
     * 获取签名MAP
     */
    public static Map<String, String> getSignMap() {
        Map<String, String> para = new TreeMap<>();
        para.put("ts", String.valueOf(System.currentTimeMillis() / 1000L));
        para.put("uid", AppStaticValue.getUserID());
        para.put("sign", getSignString(para.get("ts"), AppStaticValue.getUserID()));
        return para;
    }

    /**
     * 获取签名串
     * Token 将从App.getUserToken() 加载
     *
     * @param ts     签名串的ts
     * @param userid 签名串的Userid
     *
     * @return sign的值
     */
    private static String getSignString(String ts, String userid) {

        String para = "ts=" + ts + "&uid=" + userid;
        para += AppStaticValue.getUserToken();

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert md != null;
        byte[] Md5 = md.digest(para.getBytes());
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : Md5) {
            int bt = b & 0xff;
            if (bt < 16) {
                stringBuffer.append(0);
            }
            stringBuffer.append(Integer.toHexString(bt));
        }
        String sign = stringBuffer.toString();
        return sign;
    }

    /**
     * utf-8 转换
     */
    public static String getUTF_8String(String value) {
        return URLEncoder.encode(value);
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
}
