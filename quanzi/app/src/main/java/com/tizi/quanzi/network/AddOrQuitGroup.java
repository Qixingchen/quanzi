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
import com.tizi.quanzi.gson.GroupInfo;
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
public class AddOrQuitGroup {
    private static AddOrQuitGroup mInstance;
    private static final String TAG = AddOrQuitGroup.class.getSimpleName();
    private Gson gson;
    private Context mContext;

    private String groupname, icon, notice, userid, grouptags;


    private NewGroupListener newGroupListener;
    private QuitGroupListener quitGroupListener;
    private QueryGroupListener queryGroupListener;

    private static Response.Listener<String> mOKListener;
    private static Response.ErrorListener mErrorListener;

    public AddOrQuitGroup setNewGroupListener(NewGroupListener newGroupListener) {
        this.newGroupListener = newGroupListener;
        return mInstance;
    }

    public static AddOrQuitGroup getInstance() {
        if (mInstance == null) {
            synchronized (AddOrQuitGroup.class) {
                if (mInstance == null) {
                    mInstance = new AddOrQuitGroup();
                }
            }
        }
        return mInstance;
    }

    private AddOrQuitGroup() {
        this.mContext = App.getApplication();
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
    public void NewAGroup(String GroupName, String icon, String notice, String userID, String tag, String convid) {
        makeAddListener();

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
        newGroupPara.put("convid", convid);

        GetVolley.getmInstance(mContext).setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/group/createF", newGroupPara);
    }

    public AddOrQuitGroup setQuitGroupListener(QuitGroupListener quitGroupListener) {
        this.quitGroupListener = quitGroupListener;
        return mInstance;
    }

    public void exitGroup(String GroupID) {
        makeQuitListener();
        Map<String, String> quitGroupPara = new TreeMap<>();
        quitGroupPara.put("groupid", GroupID);
        quitGroupPara.put("userid", App.getUserID());
        GetVolley.getmInstance(mContext).setOKListener(mOKListener).setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/group/exitGroupF"
                        , quitGroupPara);
    }

    public AddOrQuitGroup setQueryListener(QueryGroupListener queryListener) {
        this.queryGroupListener = queryListener;
        return mInstance;
    }

    public void queryGroup(String GroupID) {
        makeQueryListener();
        Map<String, String> queryParas = new TreeMap<>();
        queryParas.put("groupid", GroupID);
        GetVolley.getmInstance(mContext).setOKListener(mOKListener).setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/group/findF"
                        , queryParas);
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

    public interface QuitGroupListener {
        /**
         * 成功回调
         */
        void onOK();

        void onError();
    }

    public interface QueryGroupListener {
        void OK(GroupInfo groupInfo);

        void Error(String Mess);
    }

    private void makeQuitListener() {
        mOKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Group group = gson.fromJson(response, Group.class);

                if (group.isSuccess()) {
                    if (quitGroupListener != null) {
                        // TODO: 15/8/31 判断是否成功
                        quitGroupListener.onOK();
                    }
                } else {
                    Toast.makeText(mContext, group.getMsg(), Toast.LENGTH_LONG).show();
                    if (quitGroupListener != null) {
                        quitGroupListener.onError();
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

    private void makeAddListener() {
        gson = new Gson();
        mOKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Group group = gson.fromJson(response, Group.class);

                if (group.isSuccess()) {
                    if (newGroupListener != null) {
                        // TODO: 15/8/25 add Group
                        GroupClass groupClass = new GroupClass();
                        groupClass.groupID = group.getGroupId();
                        newGroupListener.onOK(groupClass);

                    } else {
                        Toast.makeText(mContext, group.getMsg(), Toast.LENGTH_LONG).show();
                        if (newGroupListener != null) {
                            newGroupListener.onError();
                        }

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

    private void makeQueryListener() {
        gson = new Gson();
        mOKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GroupInfo group = gson.fromJson(response, GroupInfo.class);

                if (group.success) {
                    if (queryGroupListener != null) {
                        queryGroupListener.OK(group);
                    } else {
                        Toast.makeText(mContext, "网络错误", Toast.LENGTH_LONG).show();
                        if (queryGroupListener != null) {
                            queryGroupListener.Error("");
                        }

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
}
