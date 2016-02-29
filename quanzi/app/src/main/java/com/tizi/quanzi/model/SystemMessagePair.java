//package com.tizi.quanzi.model;
//
//import com.tizi.quanzi.dataStatic.ConvGroupAbs;
//import com.tizi.quanzi.tool.StaticField;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by qixingchen on 15/10/22.
// * 系统消息组
// */
//public class SystemMessagePair extends ConvGroupAbs implements Serializable {
//    public SystemMessage systemMessage;
//
//    /**
//     * 从List<SystemMessage> 转换 List<PrivateMessPair>
//     */
//    public static List<SystemMessagePair> SysMessPairsFromSystemMesses(List<SystemMessage> systemMessages) {
//        List<SystemMessagePair> systemMessagePairs = new ArrayList<>();
//        for (SystemMessage systemMessage : systemMessages) {
//            systemMessagePairs.add(SysMessPairFromSystemMess(systemMessage));
//        }
//        return systemMessagePairs;
//    }
//
//    /**
//     * 从SystemMessage 转换 SysMessPair
//     */
//    public static SystemMessagePair SysMessPairFromSystemMess(SystemMessage systemMessage) {
//        SystemMessagePair systemMessagePair = new SystemMessagePair();
//        systemMessagePair.setType(StaticField.PrivateMessOrSysMess.SysMess);
//        systemMessagePair.setName(systemMessage.user_name);
//        systemMessagePair.setFace(systemMessage.user_icon);
//        systemMessagePair.setID(systemMessage.getId());
//        systemMessagePair.setConvId(systemMessage.getConvid());
//        systemMessagePair.systemMessage = systemMessage;
//        systemMessagePair.setLastMess(systemMessage.getContent());
//        systemMessagePair.setLastMessTime(systemMessage.create_time);
//        if (systemMessage.getStatus() == StaticField.SystemMessAttrName.statueCode.complete) {
//            systemMessage.setIsread(true);
//        }
//        return systemMessagePair;
//    }
//
//}
