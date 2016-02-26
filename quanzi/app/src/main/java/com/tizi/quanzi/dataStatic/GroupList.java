package com.tizi.quanzi.dataStatic;

/**
 * Created by qixingchen on 15/9/1.
 * 全局变量组，用于储存群组消息
 */
//public class GroupList extends ConvGroupAbsList<GroupClass> {
//
//    private static GroupList mInstance;
//
//    public static ConvGroupAbsList getInstance() {
//        if (mInstance == null) {
//            synchronized (GroupList.class) {
//                if (mInstance == null) {
//                    mInstance = new GroupList();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    /**
//     * 设置未读消息
//     *
//     * @param convID  需查询的组的ID
//     * @param GroupID 需要查询的groupID
//     */
//    @Override
//    public void setUnreadMessage(String convID, String GroupID) {
//        GroupClass group = getGroup(GroupID);
//        if (group != null) {
//            group.addUnreadMessageID(DBAct.getInstance().quaryUnreadList(convID));
//        }
//    }
//
//    /**
//     * 通知所有回调
//     */
//    @Override
//    protected void noticeAllCallBack() {
//        BusProvider.getInstance().post(this);
//    }
//
//    /**
//     * 使用 groupEntityList 设置 groupList
//     *
//     * @param groupEntityList GroupEntity {@link com.tizi.quanzi.gson.Login.GroupEntity}  的List
//     */
//    public void setGroupListByLoginGroup(List<Login.GroupEntity> groupEntityList) {
//
//        //set group
//        ArrayList<GroupClass> groupClassArrayList = GroupClass.
//                getGroupArrayListByEntityList(groupEntityList);
//
//        for (GroupClass groupClass : groupClassArrayList) {
//            ChatMessage chatMessage = DBAct.getInstance().queryNewestMessage(groupClass.getConvId());
//            if (chatMessage != null) {
//                groupClass.setLastMessTime(chatMessage.create_time);
//                groupClass.setLastMess(ChatMessage.getContentText(chatMessage));
//            }
//        }
//
//        GroupList.getInstance().setGroupList(groupClassArrayList);
//    }
//}
