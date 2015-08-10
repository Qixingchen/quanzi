package com.tizi.quanzi.gson;

import java.util.List;

/**
 * Created by Yulan on 2015/8/02.
 * http://120.25.232.240:8080/group/findTagF?uid=HTDM0048b1d497171902414aa0fc6bfa982a7853&ts=1&sign=0b05797c826acd80e2a1c95ac8e18a01
 */
public class FindTAG {


    /**
     * msg : null
     * success : true
     * tags : [{"id":"HTDM0048773c059f042340eeafe4261bf92b2a51","tagName":"政治","parentTagName":"哪个是你相对擅长的领域?"},{"id":"HTDM0048773c059f042356eeafe426bbf92b2a52","tagName":"科技","parentTagName":"哪个是你相对擅长的领域?"},{"id":"HTDM0048773c059f042340eeafe426bbf92b2a53","tagName":"美食","parentTagName":"哪个是你相对擅长的领域?"},{"id":"HTDM0048773c059f042340eeafe426bbf92b2a54","tagName":"篮球","parentTagName":"哪个是你相对擅长的领域?"},{"id":"HTDM0048773c059f042340eeafe426bbf92b2a55","tagName":"投资","parentTagName":"哪个是你相对擅长的领域?"},{"id":"HTDM0048773c059f042340eeafe426bbf92b2a57","tagName":"汽车","parentTagName":"哪个是你相对擅长的领域?"},{"id":"HTDM0048773c059f042341eeafe4261bf92b2a51","tagName":"自恋","parentTagName":"哪个标签更符合你的个性?"},{"id":"HTDM0048773c059f042342eeafe426bbf92b2a52","tagName":"安静","parentTagName":"哪个标签更符合你的个性?"},{"id":"HTDM0048773c059f042343eeafe426bbf92b2a53","tagName":"善良","parentTagName":"哪个标签更符合你的个性?"},{"id":"HTDM0048773c059f042345eeafe426bbf92b2a54","tagName":"敏感","parentTagName":"哪个标签更符合你的个性?"},{"id":"HTDM0048773c059f042e46eeafe426bbf92b2a55","tagName":"工作狂","parentTagName":"哪个标签更符合你的个性?"},{"id":"HTDM0048773c059f0423yueeafe426bbf92b2a57","tagName":"乐观","parentTagName":"哪个标签更符合你的个性?"}]
     */
    private String msg;
    private boolean success;
    private List<TagsEntity> tags;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setTags(List<TagsEntity> tags) {
        this.tags = tags;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<TagsEntity> getTags() {
        return tags;
    }

    public static class TagsEntity {
        /**
         * id : HTDM0048773c059f042340eeafe4261bf92b2a51
         * tagName : 政治
         * parentTagName : 哪个是你相对擅长的领域?
         */
        private String id;
        private String tagName;
        private String parentTagName;

        public void setId(String id) {
            this.id = id;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public void setParentTagName(String parentTagName) {
            this.parentTagName = parentTagName;
        }

        public String getId() {
            return id;
        }

        public String getTagName() {
            return tagName;
        }

        public String getParentTagName() {
            return parentTagName;
        }
    }
}
