package com.tizi.quanzi.chat;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;
import com.tizi.quanzi.log.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qixingchen on 15/8/12.、
 * 网络状态回调
 */
public class MyAVIMClientEventHandler extends AVIMClientEventHandler {
    private static final String TAG = "网络状态回调";
    private static MyAVIMClientEventHandler mInstance;

    private static Map<String, OnConnectionChange> callBacks = new HashMap<>();

    public static MyAVIMClientEventHandler getInstance() {
        if (mInstance == null) {
            synchronized (MyAVIMClientEventHandler.class) {
                if (mInstance == null) {
                    mInstance = new MyAVIMClientEventHandler();
                }
            }
        }
        return mInstance;
    }

    /**
     * 添加网络监听
     *
     * @param name   监听器名，用于移除
     * @param change 监听器
     */
    public void addChange(String name, OnConnectionChange change) {
        callBacks.put(name, change);
    }


    /**
     * 移除网络监听
     *
     * @param name 需要移除的监听器名
     */
    public void removeChange(String name) {
        callBacks.remove(name);
    }

    /**
     * 链接丢失回调
     */
    @Override
    public void onConnectionPaused(AVIMClient avimClient) {
        Log.w(TAG, "链接丢失");
        for (OnConnectionChange change : callBacks.values()) {
            change.onPaused();
        }
    }

    /**
     * 链接恢复回调
     */
    @Override
    public void onConnectionResume(AVIMClient avimClient) {
        Log.w(TAG, "链接恢复");
        for (OnConnectionChange change : callBacks.values()) {
            change.onResume();
        }
    }

    /**
     * 网络变更回调
     */
    public interface OnConnectionChange {
        void onPaused();

        void onResume();
    }
}
