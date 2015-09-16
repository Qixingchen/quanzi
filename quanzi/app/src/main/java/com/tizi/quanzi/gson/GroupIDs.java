package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/9/16.
 */
public class GroupIDs extends OnlySuccess {

    /**
     * grpids : ["HTDM0048f024e27ca44341e6b7799954cb59eeb3","HTDM00481e17a23f1e84476382e608afd74389f6"]
     */

    @SerializedName("grpids")
    public List<String> grpids;
}
