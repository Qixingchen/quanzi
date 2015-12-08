package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/12/8.
 */
public class UserTags extends OnlySuccess {

    /**
     * tagName : 湖人
     * tagId : HTDM0048773c059f042340eeafe426bbf92aaa50
     * id : HTDM0000a9dd0f033ef04ce9b6b41ef6eb0934af
     */

    @SerializedName("usertags")
    public List<AllTags.TagsEntity> usertags;

}
