package com.tizi.quanzi.dataStatic;


import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.support.annotation.DrawableRes;

import com.tizi.quanzi.BR;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.model.BoomGroupClass;
import com.tizi.quanzi.tool.StaticField;

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
        if (Type != StaticField.ConvType.BOOM_GROUP) {
            return Name;
        }
        return String.format("%s 与 %s",
                ((BoomGroupClass) this).groupName1, ((BoomGroupClass) this).groupName2);
    }

    public void setName(String name) {
        Name = name;
        pcr.notifyChange(this, BR.name);
    }

    @Bindable
    public String getFace() {
        if (Type != StaticField.ConvType.BOOM_GROUP) {
            return Face;
        }
        if (isMyGroup(((BoomGroupClass) this).groupId1)) {
            return ((BoomGroupClass) this).icon2;
        } else {
            return ((BoomGroupClass) this).icon1;
        }
    }

    public void setFace(String face) {
        Face = face;
        pcr.notifyChange(this, BR.face);
    }

    @Bindable
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
        pcr.notifyChange(this, BR.iD);
    }

    @Bindable
    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
        pcr.notifyChange(this, BR.type);
    }

    @Bindable
    public String getConvId() {
        return convId;
    }

    public void setConvId(String convId) {
        this.convId = convId;
        pcr.notifyChange(this, BR.convId);
    }

    @Bindable
    public String getLastMess() {
        return lastMess;
    }

    public void setLastMess(String lastMess) {
        this.lastMess = lastMess;
        pcr.notifyChange(this, BR.lastMess);
    }

    @Bindable
    public long getLastMessTime() {
        return lastMessTime;
    }

    public void setLastMessTime(long lastMessTime) {
        this.lastMessTime = lastMessTime;
        pcr.notifyChange(this, BR.lastMessTime);
    }

    @Bindable
    @DrawableRes
    public int getChatTypeIcon() {
        switch (Type) {
            case StaticField.ConvType.TWO_PERSON:
                return R.drawable.ic_person_black_24dp;
            case StaticField.ConvType.GROUP:
                return R.drawable.ic_group_black_24dp;
            case StaticField.ConvType.BOOM_GROUP:
                return R.drawable.ic_whatshot_black_24dp;
            default:
                return R.drawable.ic_person_black_24dp;
        }
    }

    @Bindable
    public boolean getNeedNotify() {
        return AppStaticValue.getNeedNotifi(convId);
    }

    public void setNeedNotify(boolean needNotify) {
        AppStaticValue.setNeedNotifi(convId, needNotify);
        pcr.notifyChange(this, BR.needNotify);
    }

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


    /**
     * 判断是不是自己的群
     *
     * @param GroupID 群号
     *
     * @return true：是自己的圈子
     */
    private boolean isMyGroup(String GroupID) {
        if (GroupList.getInstance().getGroup(GroupID) != null) {
            return true;
        } else {
            return false;
        }
    }
}
