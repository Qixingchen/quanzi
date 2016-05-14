package com.tizi.quanzi.network;

import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.AllTags;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.tool.Tool;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by qixingchen on 15/9/10.
 * 用户个人信息变更
 *
 * @see com.tizi.quanzi.network.RetrofitAPI.UserAccount
 */
public class UserInfoSetting extends RetrofitNetworkAbs {

    private RetrofitAPI.UserAccount userAccountSer = RetrofitNetwork.retrofit.create(RetrofitAPI.UserAccount.class);

    public static UserInfoSetting getNewInstance() {
        return new UserInfoSetting();
    }

    /**
     * 更改用户的资料
     *
     * @param field 需更改的字段名称
     * @param value 更改值
     */
    private void changeFiled(String field, String value) {

        value = Tool.getUTF_8String(value);
        userAccountSer.changeUserInfo(field, value, AppStaticValue.getUserID())
                .enqueue(new Callback<OnlySuccess>() {
                    @Override
                    public void onResponse(Call<OnlySuccess> call, Response<OnlySuccess> response) {
                        myOnResponse(response);
                    }

                    @Override
                    public void onFailure(Call<OnlySuccess> call, Throwable t) {
                        myOnFailure(t);
                    }
                });
    }

    /**
     * 更改用户的昵称
     */
    public void changeName(String name) {
        changeFiled("userName", name);
    }

    /**
     * 更改用户性别
     */
    public void changeSex(String sex) {
        changeFiled("sex", sex);
    }

    /**
     * 更改用户头像；
     */
    public void changeFace(String uri) {
        changeFiled("icon", uri);
    }

    /**
     * 更改用户签名
     */
    public void changeSign(String sign) {
        changeFiled("signature", sign);
    }

    /**
     * 更改用户背景
     */
    public void changeBackground(String background) {
        changeFiled("bg", background);
    }

    /**
     * 更改用户地区
     */
    public void changeArea(String area) {
        changeFiled("area", area);
    }

    /**
     * 修改用户标签
     */
    public void changeTag(List<AllTags.TagsEntity> tags) {
        changeFiled("usertags", new AllTags().getTagServerString(tags));
    }

    /**
     * 更改用户生日
     */
    public void chagngeBirthday(int year, int month, int day) {
        changeFiled("birthday", year + "-" + month + "-" + day);
    }

    /**
     * 更改用户 Latitude
     */
    public void changeLatitude(double Latitude) {
        changeFiled("latitude", String.valueOf(Latitude));
    }

    /**
     * 更改用户 Longitude
     */
    public void changeLongitude(double Longitude) {
        changeFiled("longitude", String.valueOf(Longitude));
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserInfoSetting setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }
}
