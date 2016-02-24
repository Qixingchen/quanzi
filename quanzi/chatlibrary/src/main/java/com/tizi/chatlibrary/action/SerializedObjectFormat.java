package com.tizi.chatlibrary.action;

import android.support.annotation.Nullable;

import com.tizi.chatlibrary.log.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by qixingchen on 15/8/25.
 * 序列化对象 转换
 */
public class SerializedObjectFormat {

    private static final String TAG = SerializedObjectFormat.class.getSimpleName();

    /**
     * 将可序列化对象转换为 byte[]
     *
     * @param s 需要序列化的对象
     *
     * @return 序列化后的byte[]
     */
    public static byte[] getSerializedObject(Serializable s) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(s);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException ignored) {
            }
        }
        byte[] result = baos.toByteArray();
        Log.d(TAG, "Object " + s.getClass().getSimpleName() + "  written tobyte[]: " + result.length);
        return result;
    }

    /**
     * 将序列化byte[]转换为Object
     * T 对象类型
     *
     * @param in 序列化后的byte[]
     *
     * @return 转换的Object
     */
    @Nullable
    public static Object readSerializedObject(byte[] in) {
        Object result = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(in);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
            result = ois.readObject();
        } catch (Exception e) {
            result = null;
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (Throwable ignored) {
            }
        }
        return result;
    }
}
