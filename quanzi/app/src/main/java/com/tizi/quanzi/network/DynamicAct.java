package com.tizi.quanzi.network;

/**
 * Created by qixingchen on 15/8/18.
 * 动态查询
 * 分流 {@link GroupDynamicAct} {@link UserDynamicAct}
 *
 * @see RetrofitAPI.Dyns
 */
public class DynamicAct {

    private boolean isUser;
    private RetrofitNetworkAbs.NetworkListener listener;

    public DynamicAct(boolean isUser) {
        this.isUser = isUser;
    }

    public static DynamicAct getNewInstance(boolean isUser) {
        return new DynamicAct(isUser);
    }

    /**
     * 查询圈子内对于某事件的动态
     */
    public void getDynamic(String themeID, String groupID, int start) {
        if (isUser) {
            UserDynamicAct.getNewInstance().setNetworkListener(listener).getDynamic(start);
        } else {
            GroupDynamicAct.getNewInstance().setNetworkListener(listener).getGroupDynamic(themeID, groupID, start);
        }
    }

    /**
     * 获取动态
     *
     * @param isGroup 查群还是查主题
     * @param id      群或主题或个人的ID
     * @param start   开始的序号
     */
    public void getDynamic(boolean isGroup, String id, int start) {
        if (isUser) {
            if (id == null) {
                UserDynamicAct.getNewInstance().setNetworkListener(listener).getDynamic(start);
            } else {
                UserDynamicAct.getNewInstance().setNetworkListener(listener).getDynamic(id, start);
            }
        } else {
            GroupDynamicAct.getNewInstance().setNetworkListener(listener).getGroupDynamic(isGroup, id, start);
        }

    }

    /**
     * 获取动态
     *
     * @param dynID 动态ID
     */
    public void getDynamicByID(String dynID) {
        if (isUser) {
            UserDynamicAct.getNewInstance().setNetworkListener(listener).getDynamicByID(dynID);
        } else {
            GroupDynamicAct.getNewInstance().setNetworkListener(listener).getDynamicByID(dynID);
        }
    }

    /*点赞*/
    public void addZan(String dynID, boolean isZan) {
        if (isUser) {
            UserDynamicAct.getNewInstance().setNetworkListener(listener).addZan(dynID, isZan);
        } else {
            GroupDynamicAct.getNewInstance().setNetworkListener(listener).addZan(dynID, isZan);
        }
    }

    public void isZan(String dynID) {
        if (isUser) {
            UserDynamicAct.getNewInstance().setNetworkListener(listener).isZan(dynID);
        } else {
            GroupDynamicAct.getNewInstance().setNetworkListener(listener).isZan(dynID);
        }
    }


    /*获取评论*/
    public void getComment(String dynID, int start, int limit) {
        if (isUser) {
            UserDynamicAct.getNewInstance().setNetworkListener(listener).getComment(dynID, start, limit);
        } else {
            GroupDynamicAct.getNewInstance().setNetworkListener(listener).getComment(dynID, start, limit);
        }
    }

    /*添加评论*/
    public void addComment(String dynID, String comment) {
        if (isUser) {
            UserDynamicAct.getNewInstance().setNetworkListener(listener).addComment(dynID, comment);
        } else {
            GroupDynamicAct.getNewInstance().setNetworkListener(listener).addComment(dynID, comment);
        }
    }


    /*添加评论*/
    public void addComment(String dynID, String comment, String replyID, String atUserID) {
        if (isUser) {
            UserDynamicAct.getNewInstance().setNetworkListener(listener).addComment(dynID, comment, replyID, atUserID);
        } else {
            GroupDynamicAct.getNewInstance().setNetworkListener(listener).addComment(dynID, comment, replyID, atUserID);
        }
    }

    /*发表动态*/
    public void addDYn(String ThemeID, String GroupID, String comment, String pic) {

        if (isUser) {
            UserDynamicAct.getNewInstance().setNetworkListener(listener).addDYn(comment, pic);
        } else {
            GroupDynamicAct.getNewInstance().setNetworkListener(listener).addDYn(ThemeID, GroupID, comment, pic);
        }
    }

    public void addDYn(String ThemeID, String GroupID, String comment) {
        addDYn(ThemeID, GroupID, comment, null);
    }

    @SuppressWarnings("unchecked")
    public DynamicAct setNetworkListener(RetrofitNetworkAbs.NetworkListener networkListener) {
        this.listener = networkListener;
        return this;
    }

}
