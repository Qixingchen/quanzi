package com.tizi.quanzi.gson;

import java.util.List;

/**
 * Created by qixingchen on 15/8/17.
 */
public class Group {

    /**
     * groupId : HTDM0048c5d946c3ccae4985981f757d55f743d1
     * groupTags : [{"groupTagId":"HTDM0048e7196ba849424a549140ef18b978777c","tagName":"1name"},{"groupTagId":"HTDM0048715985aaba1c402a99bbfc1b0bdbc310","tagName":"null"}]
     * success : true
     */
    private String groupId;
    private List<GroupTagsEntity> groupTags;
    private boolean success;

    public String getMsg() {
        return msg;
    }

    private String msg;

    public String getGroupId() {
        return groupId;
    }

    public List<GroupTagsEntity> getGroupTags() {
        return groupTags;
    }

    public boolean isSuccess() {
        return success;
    }

    public static class GroupTagsEntity {
        /**
         * groupTagId : HTDM0048e7196ba849424a549140ef18b978777c
         * tagName : 1name
         */
        private String groupTagId;
        private String tagName;

        public String getGroupTagId() {
            return groupTagId;
        }

        public String getTagName() {
            return tagName;
        }
    }
}
