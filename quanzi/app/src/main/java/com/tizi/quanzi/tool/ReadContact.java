package com.tizi.quanzi.tool;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.tizi.quanzi.log.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/9/18.
 * 读取通讯录
 */
public class ReadContact {

    /**
     * 读取通讯录
     *
     * @param context context
     *
     * @return 通讯录列表 {@link com.tizi.quanzi.tool.ReadContact.Mobiles}
     */
    public static List<Mobiles> readContact(Context context) {
        Cursor phones = context.getContentResolver().query
                (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        List<Mobiles> mobilesList = new ArrayList<>();
        if (phones != null) {
            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                phoneNumber = Tool.getPhoneNum(phoneNumber);
                if (phoneNumber != null) {
                    Mobiles mobile = new Mobiles();
                    mobile.mobile = phoneNumber;
                    mobilesList.add(mobile);
                }
            }
            phones.close();
        }
        Log.i("通讯录", String.valueOf(mobilesList.size()));
        return mobilesList;
    }

    public static class Mobiles {
        public String mobile;
    }
}
