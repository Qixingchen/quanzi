package com.tizi.chatlibrary.action;

/**
 * Created by qixingchen on 16/2/23.
 * 初始化library
 */
public class Init {
    private static Init mInstance;

    public static Init getInstance() {
        if (mInstance == null) {
            synchronized (Init.class) {
                if (mInstance == null) {
                    mInstance = new Init();
                }
            }
        }
        return mInstance;
    }
}
