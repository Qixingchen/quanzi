package com.tizi.quanzi.widget;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;

/**
 * Created by qixingchen on 15/11/18.
 * 询问是否可以使用通讯录
 */
public class AskForContact {

    private OnResult onResult;

    public static AskForContact getNewInstance(OnResult onResult) {
        AskForContact askForContact = new AskForContact();
        askForContact.onResult = onResult;
        return askForContact;
    }

    public void AskForContact(final Activity activity, final int code) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(activity).setTitle(StaticField.AppName.AppZHName + "请求您的通讯录权限")
                    .setMessage("您的通讯录将帮助您找到您的好友，通讯录即传即用，我们不会储存您的通讯录")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.READ_CONTACTS}, code);
                        }
                    }).setNegativeButton("不好", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onResult.deny();
                }
            })
                    .setNeutralButton("查看权限说明", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent permissionNotice = new Intent(Intent.ACTION_VIEW);
                            permissionNotice.setData(Uri.parse("https://github.com/Qixingchen/quanzi_public/wiki/uses-permission"));
                            if (Tool.isIntentSafe(activity, permissionNotice)) {
                                activity.startActivity(permissionNotice);
                            }
                        }
                    }).show();
        }
    }

    public interface OnResult {
        void deny();
    }
}
