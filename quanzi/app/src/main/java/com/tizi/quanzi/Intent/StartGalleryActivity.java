package com.tizi.quanzi.Intent;

import android.content.Context;
import android.content.Intent;

import com.tizi.quanzi.ui.gallery.GalleryActivity;

import java.util.ArrayList;

/**
 * Created by qixingchen on 15/9/21.
 * 启动相册界面
 */
public class StartGalleryActivity {

    public static void startByStringList(ArrayList<String> pics, Context context) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putStringArrayListExtra("pics", pics);
        context.startActivity(intent);
    }
}
