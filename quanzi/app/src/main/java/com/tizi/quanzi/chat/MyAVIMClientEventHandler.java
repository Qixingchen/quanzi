package com.tizi.quanzi.chat;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.otto.AVIMNetworkEvents;
import com.tizi.quanzi.otto.BusProvider;

/**
 * Created by qixingchen on 15/8/12.、
 * 网络状态回调
 */
public class MyAVIMClientEventHandler extends AVIMClientEventHandler {
    private static final String TAG = "网络状态回调";
    private static MyAVIMClientEventHandler mInstance;
    public boolean isNetworkAvailable;

    private MyAVIMClientEventHandler() {
        isNetworkAvailable = true;
    }

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
     * 链接丢失回调
     */
    @Override
    public void onConnectionPaused(AVIMClient avimClient) {
        Log.w(TAG, "链接丢失");
        BusProvider.getInstance().post(AVIMNetworkEvents.networkEvents(false));
        isNetworkAvailable = false;
    }

    /**
     * 链接恢复回调
     */
    @Override
    public void onConnectionResume(AVIMClient avimClient) {
        Log.w(TAG, "链接恢复");
        BusProvider.getInstance().post(AVIMNetworkEvents.networkEvents(true));
        isNetworkAvailable = true;
    }

    @Override
    public void onClientOffline(AVIMClient avimClient, int i) {
        // TODO: 15/11/5 no document
    }

}
