package com.tizi.quanzi.otto;

/**
 * Created by qixingchen on 15/10/13.
 * LC网络状态更改
 */
public class AVIMNetworkEvents {
    public boolean isNetWorkAvailable;

    public static AVIMNetworkEvents networkEvents(boolean isNetWorkAvailable) {
        AVIMNetworkEvents avimNetworkEvents = new AVIMNetworkEvents();
        avimNetworkEvents.isNetWorkAvailable = isNetWorkAvailable;
        return avimNetworkEvents;
    }
}
