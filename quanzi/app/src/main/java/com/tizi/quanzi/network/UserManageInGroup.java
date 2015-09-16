package com.tizi.quanzi.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.log.Log;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by qixingchen on 15/9/2.
 * 成员管理后台调用
 */
public class UserManageInGroup {

    private static final String TAG = UserManageInGroup.class.getSimpleName();
    private Gson gson;
    private Context mContext;

    private ManageGroupListener ManageGroupListener;

    private static Response.Listener<String> mOKListener;
    private static Response.ErrorListener mErrorListener;

    public UserManageInGroup setManageGroupListener(ManageGroupListener ManageGroupListener) {
        this.ManageGroupListener = ManageGroupListener;
        return this;
    }

    public static UserManageInGroup getInstance() {
        return new UserManageInGroup();
    }

    private UserManageInGroup() {
        this.mContext = App.getApplication();
        gson = new Gson();
        mOKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GroupAllInfo groupAllInfo = gson.fromJson(response, GroupAllInfo.class);
                if (groupAllInfo.success) {
                    if (ManageGroupListener != null) {
                        ManageGroupListener.onOK(groupAllInfo);
                    }
                } else {
                    Log.e(TAG, "Error");
                    Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
                    ManageGroupListener.onError("");
                }
            }
        };
        mErrorListener = new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w(TAG, error.getMessage());
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
                if (ManageGroupListener != null) {
                    ManageGroupListener.onError(error.getMessage());
                }
            }
        };
    }

    /**
     * 将用户从圈子中删除
     */
    public void deleteUser(String GroupID, String UserID) {

        Map<String, String> deleteUserPara = new TreeMap<>();
        deleteUserPara.put("userid", UserID);
        deleteUserPara.put("groupid", GroupID);


        GetVolley.getmInstance(mContext).setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/group/exitGroupF", deleteUserPara);
    }

    /**
     * 同意加入圈子
     */
    public void acceptJoinGroup(String GroupID, String UserID) {
        Map<String, String> acceptJoinGroupPara = new TreeMap<>();
        acceptJoinGroupPara.put("userid", UserID);
        acceptJoinGroupPara.put("groupid", GroupID);


        GetVolley.getmInstance(mContext).setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/group/acceptGroupInvite", acceptJoinGroupPara);
    }

    /**
     * 删除圈子
     */
    public void deleteGroup(String GroupID) {
        Map<String, String> deleteGroupPara = new TreeMap<>();
        deleteGroupPara.put("userid", AppStaticValue.getUserID());
        deleteGroupPara.put("groupid", GroupID);


        GetVolley.getmInstance(mContext).setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/group/dropGroupF", deleteGroupPara);
    }

    public interface ManageGroupListener {
        /**
         * 成功回调
         */
        void onOK(GroupAllInfo groupAllInfo);

        void onError(String errorMessage);
    }


}
