package com.tizi.quanzi.dataStatic;

import android.support.annotation.Nullable;

import com.tizi.quanzi.model.PrivateMessPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by qixingchen on 15/9/3.
 * 私信对列表
 */
public class PrivateMessPairList {

    private final static ArrayList<PrivateMessPair> PrivateMessPairList = new ArrayList<>();
    private static PrivateMessPairList mInstance;
    private ArrayList<OnChangeCallBack> onChangeCallBacks = new ArrayList<>();

    public PrivateMessPairList addOnChangeCallBack(OnChangeCallBack onChangeCallBack) {
        onChangeCallBacks.add(onChangeCallBack);
        return mInstance;
    }

    public void deleteOnChangeCallBack(OnChangeCallBack onChangeCallBack) {
        onChangeCallBacks.remove(onChangeCallBack);
    }

    public static PrivateMessPairList getInstance() {
        if (mInstance == null) {
            synchronized (PrivateMessPairList.class) {
                if (mInstance == null) {
                    mInstance = new PrivateMessPairList();
                }
            }
        }
        return mInstance;
    }

    private PrivateMessPairList() {
    }

    public ArrayList<PrivateMessPair> getPrivateMessPairList() {
        return PrivateMessPairList;
    }

    public int getUnreadNum() {
        int ans = 0;
        for (PrivateMessPair privateMessPair : PrivateMessPairList) {
            if (privateMessPair.UnreadCount != 0) {
                ans++;
            }
        }
        return ans;
    }

    public void callUpdate() {
        noticeAllCallBack();
    }

    @Nullable
    public PrivateMessPair getPrivateMessPair(String id) {
        synchronized (PrivateMessPairList) {
            for (PrivateMessPair privateMessPair : PrivateMessPairList) {
                if (privateMessPair.ID.compareTo(id) == 0) {
                    return privateMessPair;
                }
            }
        }
        return null;
    }

    public String getIDByConvID(String convID) {
        synchronized (PrivateMessPairList) {
            for (PrivateMessPair privateMessPair : PrivateMessPairList) {
                if (privateMessPair.convId.compareTo(convID) == 0) {
                    return privateMessPair.ID;
                }
            }
        }
        return "";
    }

    public void setPrivateMessPairList(List<PrivateMessPair> newPrivateMessPairList) {
        synchronized (PrivateMessPairList) {
            PrivateMessPairList.clear();
            PrivateMessPairList.addAll(newPrivateMessPairList);
        }
        sort();
        noticeAllCallBack();
    }

    public void addPair(PrivateMessPair Pair) {
        synchronized (PrivateMessPairList) {
            PrivateMessPairList.add(Pair);
        }
        sort();
        noticeAllCallBack();
    }

    public boolean deletePair(String ID) {
        synchronized (PrivateMessPairList) {
            for (PrivateMessPair PrivateMessPair : PrivateMessPairList) {
                if (PrivateMessPair.ID.compareTo(ID) == 0) {
                    PrivateMessPairList.remove(PrivateMessPair);
                    noticeAllCallBack();
                    return true;
                }
            }
            return false;
        }
    }

    public void updatePair(PrivateMessPair pair) {
        synchronized (PrivateMessPairList) {
            for (PrivateMessPair PrivateMessPair : PrivateMessPairList) {
                if (PrivateMessPair.ID.compareTo(pair.ID) == 0) {
                    PrivateMessPairList.remove(PrivateMessPair);
                    PrivateMessPairList.add(PrivateMessPair);
                }
            }
        }
        sort();
        noticeAllCallBack();
    }

    public void sort() {
        synchronized (PrivateMessPairList) {
            Collections.sort(PrivateMessPairList, new Comparator<PrivateMessPair>() {
                @Override
                public int compare(PrivateMessPair lhs, PrivateMessPair rhs) {
                    if (lhs.lastMessTime == 0 && rhs.lastMessTime == 0) {
                        return 0;
                    }
                    if (lhs.lastMessTime == 0) {
                        return Integer.MAX_VALUE;
                    }
                    if (rhs.lastMessTime == 0) {
                        return Integer.MIN_VALUE;
                    }
                    return (int) -(lhs.lastMessTime - rhs.lastMessTime);
                }
            });
        }
    }

    public boolean updatePairLastMess(String convID, String lastMess, long lastTime) {
        boolean isUpdated = false;
        synchronized (PrivateMessPairList) {
            for (PrivateMessPair group : PrivateMessPairList) {
                if (group.convId.compareTo(convID) == 0) {
                    group.lastMess = lastMess;
                    group.lastMessTime = lastTime;
                    isUpdated = true;
                    break;
                }
            }
        }
        if (isUpdated) {
            sort();
            noticeAllCallBack();
        }
        return isUpdated;
    }

    private void noticeAllCallBack() {
        for (OnChangeCallBack cb : onChangeCallBacks) {
            cb.changed();
        }
    }

    public interface OnChangeCallBack {
        void changed();
    }

}
