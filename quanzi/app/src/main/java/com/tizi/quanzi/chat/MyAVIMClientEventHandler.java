package com.tizi.quanzi.chat;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;
import com.tizi.quanzi.log.Log;

/**
 * Created by qixingchen on 15/8/12.、
 * 网络状态回调
 */
public class MyAVIMClientEventHandler extends AVIMClientEventHandler {
    private static final String TAG = "网络状态回调";
    @Override
    public void onConnectionPaused(AVIMClient avimClient) {
        //todo
        Log.w(TAG,"链接丢失");
    }

    @Override
    public void onConnectionResume(AVIMClient avimClient) {
// TODO: 15/8/12
        Log.w(TAG,"链接恢复");
    }
}
