package com.tizi.quanzi.Intent;

import android.content.Context;
import android.content.Intent;

import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.ui.main.MainActivity;

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
     * @param groupEntityList GroupEntity {@link com.tizi.quanzi.gson.Login.GroupEntity}  的List
     * @param context         上下文
     */
    public static void startByLoginGroup(List<Login.GroupEntity> groupEntityList, Context context) {

        //set group
        ArrayList<GroupClass> groupClassArrayList = GroupClass.
                getGroupArrayListByEntityList(groupEntityList);

        for (GroupClass groupClass : groupClassArrayList) {
            ChatMessage chatMessage = DBAct.getInstance().queryNewestMessage(groupClass.convId);
            if (chatMessage != null) {
                groupClass.lastMessTime = chatMessage.create_time;
                groupClass.lastMess = ChatMessage.getContentText(chatMessage);
            }
            groupClass.setNeedNotifiFromPrefer();
        }

        GroupList.getInstance().setGroupList(groupClassArrayList);

        //start intent
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


}
