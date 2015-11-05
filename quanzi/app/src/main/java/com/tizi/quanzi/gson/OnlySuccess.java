package com.tizi.quanzi.gson;

import java.io.Serializable;

/**
 * Created by Yulan on 2015/8/02.
 * 只有成功信息的调用
 */
public class OnlySuccess implements Serializable {

    /**
     * success : true
     */
    public boolean success;
    public String msg;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
