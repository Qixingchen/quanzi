package com.tizi.quanzi.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.log.Log;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by qixingchen on 15/9/2.
 */
public class UserManageInGroup {

    private static UserManageInGroup mInstance;
    private static final String TAG = UserManageInGroup.class.getSimpleName();
    private Gson gson;
    private Context mContext;

    private ManageGroupListener ManageGroupListener;

    private static Response.Listener<String> mOKListener;
    private static Response.ErrorListener mErrorListener;

    public UserManageInGroup setManageGroupListener(ManageGroupListener ManageGroupListener) {
        this.ManageGroupListener = ManageGroupListener;
        return mInstance;
    }

    public static UserManageInGroup getInstance() {
        if (mInstance == null) {
            synchronized (UserManageInGroup.class) {
                if (mInstance == null) {
                    mInstance = new UserManageInGroup();
                }
            }
        }
        return mInstance;
    }

    private UserManageInGroup() {
        this.mContext = App.getApplication();
        gson = new Gson();
        mOKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                OnlySuccess dyns = gson.fromJson(response, OnlySuccess.class);
                if (dyns.isSuccess()) {
                    if (ManageGroupListener != null) {
                        ManageGroupListener.onOK();
                    }
                } else {
                    Log.e(TAG, "Error");
                    Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
                    ManageGroupListener.onError();
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
                    ManageGroupListener.onError();
                }
            }
        }

        ;
    }

    /**
     * 将用户加入圈子
     */
    public UserManageInGroup deleteUser(String GroupID, String UserID) {

        Map<String, String> deleteUserPara = new TreeMap<>();
        deleteUserPara.put("userid", UserID);
        deleteUserPara.put("groupid", GroupID);


        GetVolley.getmInstance(mContext).setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/group/exitGroupF", deleteUserPara);
        return mInstance;
    }

    public interface ManageGroupListener {
        /**
         * 成功回调
         */
        void onOK();

        void onError();
    }


}
