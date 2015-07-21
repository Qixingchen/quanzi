package com.tizi.quanzi.Intent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.ui.MainActivity;

import java.util.List;

/**
 * Created by qixingchen on 15/7/21.
 */
public class StartMainActivity {

    public static void startByLoginGroup(List<Login.GroupEntity> groupEntityList, Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();

        int length = groupEntityList.size();

        String[] ids = new String[length];
        String[] icons = new String[length];
        String[] groupNames = new String[length];
        String[] types = new String[length];

        for (int i = 0; i < length; i++) {
            ids[i] = groupEntityList.get(i).getId();
            icons[i] = groupEntityList.get(i).getIcon();
            groupNames[i] = groupEntityList.get(i).getGroupName();
            types[i] = groupEntityList.get(i).getType();
        }
        intent.putExtra("groupids", ids);
        intent.putExtra("groupicons", icons);
        intent.putExtra("groupgroupNames", groupNames);
        intent.putExtra("grouptypes", types);
        context.startActivity(intent);
    }

}
