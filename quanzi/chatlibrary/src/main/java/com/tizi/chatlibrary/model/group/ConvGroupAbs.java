package com.tizi.chatlibrary.model.group;


import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.tizi.chatlibrary.BR;
import com.tizi.chatlibrary.model.message.ChatMessage;
import com.tizi.chatlibrary.staticData.GroupList;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

/**
 * Created by qixingchen on 15/9/8.
 * 通讯组抽象类
 */
public class ConvGroupAbs implements Serializable, Observable {

    private String Name;
    private String Face;
    private String ID;
    private int Type;
    private String convId;
    //不要重置的项目
    private String lastMess;
    private long lastMessTime;
    private HashSet<String> unreadMessageIDSet = new HashSet<>();
    private transient PropertyChangeRegistry pcr = new PropertyChangeRegistry();

    @Bindable
    public String getName() {
        if (Type != ChatMessage.CONVERSATION_TYPE_TEMP_GROUP) {
            return Name;
        }
        return String.format("%s 与 %s",
                ((TempGroupClass) this).groupName1, ((TempGroupClass) this).groupName2);
    }

    public void setName(String name) {
        Name = name;
        notifyChange(BR.name);
    }

    @Bindable
    public String getFace() {
        if (Type != ChatMessage.CONVERSATION_TYPE_TEMP_GROUP) {
            return Face;
        }
        if (isMyGroup(((TempGroupClass) this).groupId1)) {
            return ((TempGroupClass) this).icon2;
        } else {
            return ((TempGroupClass) this).icon1;
        }
    }

    public void setFace(String face) {
        Face = face;
        notifyChange(BR.face);
    }

    @Bindable
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
        notifyChange(BR.iD);
    }

    @Bindable
    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
        notifyChange(BR.type);
    }

    @Bindable
    public String getConvId() {
        return convId;
    }

    public void setConvId(String convId) {
        this.convId = convId;
        notifyChange(BR.convId);
    }

    @Bindable
    public String getLastMess() {
        return lastMess;
    }

    public void setLastMess(String lastMess) {
        this.lastMess = lastMess;
        notifyChange(BR.lastMess);
    }

    @Bindable
    public long getLastMessTime() {
        return lastMessTime;
    }

    public void setLastMessTime(long lastMessTime) {
        this.lastMessTime = lastMessTime;
        notifyChange(BR.lastMessTime);
    }

    public boolean addUnreadMessageID(String messID) {
        boolean stat = unreadMessageIDSet.add(messID);
        notifyChange(BR.unreadCount);
        return stat;
    }

    public boolean addUnreadMessageID(List<String> messID) {
        boolean stat = unreadMessageIDSet.addAll(messID);
        notifyChange(BR.unreadCount);
        return stat;
    }

    public boolean removeUnreadMessad(String messID) {
        boolean stat = unreadMessageIDSet.remove(messID);
        notifyChange(BR.unreadCount);
        return stat;
    }

    public void removeAllUnread() {
        unreadMessageIDSet.clear();
        notifyChange(BR.unreadCount);
    }

    @Bindable
    public int getUnreadCount() {
        return unreadMessageIDSet.size();
    }

    /**
     * Adds a callback to listen for changes to the Observable.
     *
     * @param callback The callback to start listening.
     */
    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (pcr == null) {
            pcr = new PropertyChangeRegistry();
        }
        pcr.add(callback);
    }

    /**
     * Removes a callback from those listening for changes.
     *
     * @param callback The callback that should stop listening.
     */
    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (pcr == null) {
            pcr = new PropertyChangeRegistry();
        }
        pcr.remove(callback);
    }

    private void notifyChange(int propertyId) {
        if (pcr == null) {
            pcr = new PropertyChangeRegistry();
        }
        pcr.notifyChange(this, propertyId);
    }


    /**
     * 判断是不是自己的群
     *
     * @param GroupID 群号
     *
     * @return true：是自己的圈子 false:不是自己的圈子
     */
    private boolean isMyGroup(String GroupID) {
        return GroupList.getInstance().getGroup(GroupID) != null;
    }
}
