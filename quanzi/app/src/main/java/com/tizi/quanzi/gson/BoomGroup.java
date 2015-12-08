package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/10/9.
 */
public class BoomGroup extends OnlySuccess {

    @SerializedName("groupmatch")
    public List<GroupmatchEntity> groupmatch;

    public static class GroupmatchEntity {
        /**
         * id : HTDM00000753d64cab1e4afd91c1094e5a7d9a01
         * convId : 561757de60b28e98eff351de
         * groupId1 : HTDM0048ff1ccb60fb654af3ae07f972fac1d7ba
         * groupId2 : HTDM00480d08f2a9c55c4dea96f804fffe40c717
         * groupName1 : ä¸­æ
         * groupName2 : 中文
         * icon1 : http://ac-hy5srahi.clouddn.com/canfgHeHSM0Vxz7j9jLZhn7kQSUtyBrGoJgtNfb5.jpg?imageView/1/w/200/h/200/q/100/format/png
         * icon2 : http://ac-hy5srahi.clouddn.com/XlSPdlcllz97KAlhBBsuavPWLV3pbFV6KrTS3Mai.jpg?imageView/1/w/200/h/200/q/100/format/png
         * grpmem1 : [{"id":"HTDM004825b32141fe9c41f09846e85f0902f0bd","birthday":"2005-09-24","icon":"http://ac-hy5srahi.clouddn.com/caLxbdJcpOapVD1UR2yoJvD.jpeg","sex":"1","name":"账号1"}]
         * grpmem2 : [{"id":"HTDM004825b32141fe9c41f09846e85f0902f0bd","birthday":"2005-09-24","icon":"http://ac-hy5srahi.clouddn.com/caLxbdJcpOapVD1UR2yoJvD.jpeg","sex":"1","name":"账号1"}]
         */

        @SerializedName("id")
        public String id;
        @SerializedName("convId")
        public String convId;
        @SerializedName("groupId1")
        public String groupId1;
        @SerializedName("groupId2")
        public String groupId2;
        @SerializedName("groupName1")
        public String groupName1;
        @SerializedName("groupName2")
        public String groupName2;
        @SerializedName("icon1")
        public String icon1;
        @SerializedName("icon2")
        public String icon2;
        @SerializedName("grpmem1")
        public List<GroupAllInfo.MemberEntity> groupMenber1;
        @SerializedName("grpmem2")
        public List<GroupAllInfo.MemberEntity> groupMenber2;

    }
}
