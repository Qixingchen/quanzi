package com.tizi.quanzi.gson;

import android.net.Uri;

/**
 * Created by qixingchen on 15/7/16.
 */
public class Group {
    public String groupName;
    public Uri groupFace;
    public String groupID;

    public static Group getGroups() {
        Group group = new Group();
        group.groupFace = Uri.parse("http://cdn.marketplaceimages.windowsphone.com/v8/images/f5f28d0a-bef3-4bed-99ef-d2f21d624e3b?imageType=ws_icon_large");
        group.groupID = "group id";
        group.groupName = "group name  ";
        return group;
    }
}
