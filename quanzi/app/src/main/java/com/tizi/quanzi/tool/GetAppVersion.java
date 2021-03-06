package com.tizi.quanzi.tool;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.tizi.quanzi.BuildConfig;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.ApiInfoGson;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.ApiInfo;
import com.tizi.quanzi.network.RetrofitNetworkAbs;

/**
 * Created by qixingchen on 15/9/23.
 * 获取 APP 的最新版本，并发出弹窗
 *
 * @deprecated 市场政策不允许
 */
public class GetAppVersion {

    private static final String TAG = GetAppVersion.class.getSimpleName();

    public static void doit(final Context context) {
        if (!BuildConfig.AllowUpdate || BuildConfig.DEBUG) {
            return;
        }
        ApiInfo.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                int lastUpdate = AppStaticValue.getIntPrefer(StaticField.Preferences.LastAppUpDateTime, 0);
                if (Tool.getBeijinTime() / 1000 - lastUpdate < 3600) {
                    return;
                }
                //                if (AppStaticValue.getStringPrefer(StaticField.Preferences.AllowAppUpDate)
                //                        .compareTo(String.valueOf(false)) == 0) {
                //                    Log.i(TAG, "不允许更新");
                //                    return;
                //                }
                final ApiInfoGson apiInfo = (ApiInfoGson) ts;
                String appVer = BuildConfig.VERSION_NAME;
                if (isNewer(appVer, apiInfo.info.androidVersion)) {
                    Log.i(TAG, "发现更新");
                    new AlertDialog.Builder(context)
                            .setTitle("发现更新")
                            .setMessage(apiInfo.info.content)
                            .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent installApk = new Intent(Intent.ACTION_VIEW);
                                    installApk.setData(Uri.parse(apiInfo.info.androidUrl));
                                    context.startActivity(installApk);
                                }
                            }).setNegativeButton("一小时后提示", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AppStaticValue.setIntPrefer(StaticField.Preferences.LastAppUpDateTime,
                                            (int) (Tool.getBeijinTime() / 1000));
                                    dialog.dismiss();
                                }
                            }
                    )
                            //                            .setNegativeButton("不再提示", new DialogInterface.OnClickListener() {
                            //
                            //                                        @Override
                            //                                        public void onClick(DialogInterface dialog, int which) {
                            //                                            AppStaticValue.setStringPrefer(StaticField.Preferences.AllowAppUpDate,
                            //                                                    String.valueOf(false));
                            //                                            dialog.dismiss();
                            //                                        }
                            //                                    }
                            //
                            //                            )
                            .setNeutralButton("下次启动时再说", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    Log.i(TAG, "未发现更新");
                }
            }

            @Override
            public void onError(String Message) {
            }
        }).getAPiinfo();
    }

    /**
     * 判断服务器版本是否比本地应用更新
     *
     * @param app    本地版本
     * @param server 服务器版本
     *
     * @return 服务器更新则为真，否则为否
     */
    private static boolean isNewer(String app, String server) {
        String[] apps = app.split("\\.");
        String[] servers = server.split("\\.");
        int length = Math.min(apps.length, servers.length);
        for (int i = 0; i < length; i++) {
            if (Integer.parseInt(servers[i]) > Integer.parseInt(apps[i].split("-")[0])) {
                return true;
            }
        }
        return false;
    }

}
