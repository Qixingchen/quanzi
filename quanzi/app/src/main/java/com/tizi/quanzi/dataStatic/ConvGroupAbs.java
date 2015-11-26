package com.tizi.quanzi.dataStatic;


import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

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
    public String lastMess;
    public long lastMessTime;
    private HashSet<String> unreadMessageIDSet = new HashSet<>();

    public boolean addUnreadMessageID(String messID) {
        return unreadMessageIDSet.add(messID);
    }

    public boolean addUnreadMessageID(List<String> messID) {
        return unreadMessageIDSet.addAll(messID);
    }

    public boolean removeUnreadMessad(String messID) {
        return unreadMessageIDSet.remove(messID);
    }

    public void removeAllUnread() {
        unreadMessageIDSet.clear();
    }

    public int getUnreadCount() {
        return unreadMessageIDSet.size();
    }


}
