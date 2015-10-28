package com.tizi.quanzi.dataStatic;


import java.io.Serializable;

/**
 * Created by qixingchen on 15/9/8.
 * 通讯组抽象类
 */
public abstract class ConvGroupAbs implements Serializable {

    public String Name;
    public String Face;
    public String ID;
    public int Type;
    public String convId;
    //不要重置的项目
    public int UnreadCount;
    public String lastMess;
    public long lastMessTime;


}
