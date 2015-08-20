package com.tizi.quanzi.network;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.gson.Group;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.GroupClass;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by qixingchen on 15/8/17.
 * 请求一个新群
 *
 * @see GroupClass
 * @see Group
 */
public class NewGroup {
    private static NewGroup mInstance;
    private static final String TAG = NewGroup.class.getSimpleName();
    private Gson gson;
    private Context mContext;

    private String groupname, icon, notice, userid, grouptags;


    private NewGroupListener newGroupListener;

    private static Response.Listener<String> mOKListener;
    private static Response.ErrorListener mErrorListener;

    public NewGroup setNewGroupListener(NewGroupListener newGroupListener) {
        this.newGroupListener = newGroupListener;
        return mInstance;
    }

    public static NewGroup getInstance() {
        if (mInstance == null) {
            synchronized (NewGroup.class) {
                if (mInstance == null) {
                    mInstance = new NewGroup();
                }
            }
        }
        return mInstance;
    }

    private NewGroup() {
        this.mContext = App.getApplication();
        gson = new Gson();
        mOKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Group group = gson.fromJson(response, Group.class);

                if (group.isSuccess()) {
                    if (newGroupListener != null) {
                        GroupClass groupClass = new GroupClass(groupname, Uri.parse(icon),
                                group.getGroupId(), "");
                        newGroupListener.onOK(groupClass);
                    }
                } else {
                    Toast.makeText(mContext, group.getMsg(), Toast.LENGTH_LONG).show();
                    if (newGroupListener != null) {
                        newGroupListener.onError();
                    }
                }
            }
        };
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w(TAG, error.getMessage());
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
                if (newGroupListener != null) {
                    newGroupListener.onError();
                }
            }
        };
    }

    /**
     * 创建一个群
     *
     * @param GroupName 群名
     * @param icon      群头像
     * @param notice    群公告
     * @param userID    创建者 todo 去掉
     * @param tag       群标签
     */
    public void NewGroup(String GroupName, String icon, String notice, String userID, String tag) {

        groupname = GroupName;
        this.icon = icon;
        this.notice = notice;
        this.grouptags = tag;

        Map<String, String> newGroupPara = new TreeMap<>();
        newGroupPara.put("account", App.getUserPhone());
        newGroupPara.put("groupname", GroupName);
        newGroupPara.put("icon", icon);
        newGroupPara.put("notice", notice);
        newGroupPara.put("userid", userID);
        newGroupPara.put("grouptags", tag);

        GetVolley.getmInstance(mContext).setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/group/createF", newGroupPara);
    }

    public interface NewGroupListener {
        /**
         * 成功回调
         *
         * @param groupClass 刚刚创建的群信息
         *
         * @see GroupClass
         * @see Group
         */
        void onOK(GroupClass groupClass);

        void onError();
    }

}
