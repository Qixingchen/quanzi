package com.tizi.quanzi.Intent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/7/21.
 * 主界面唤起
 */
public class StartMainActivity {

    /**
     * 使用 groupEntityList 唤起主界面
     * <p/>
     * Intent: group : ArrayList<GroupClass>
     *
     * @param groupEntityList GroupEntity 的List
     * @param context         上下文
     *
     * @see com.tizi.quanzi.gson.Login.GroupEntity
     */
    public static void startByLoginGroup(List<Login.GroupEntity> groupEntityList, Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        Bundle bundle = new Bundle();

        ArrayList<GroupClass> groupClassArrayList = GroupClass.
                getGroupArrayListByEntityList(groupEntityList);

        bundle.putParcelableArrayList("group", groupClassArrayList);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }


}