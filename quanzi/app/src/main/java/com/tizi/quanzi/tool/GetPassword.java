package com.tizi.quanzi.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by qixingchen on 15/7/13.
 * 密码计算
 */
public class GetPassword {

    /**
     * 从明文密码计算最终散列化的密码
     *
     * @param pwd 明文密码
     *
     * @return 最终密码
     */
    public static String fullHash(String pwd) {
        pwd = preHASH(pwd);
        pwd = LaterHASH(pwd);
        return pwd;
    }

    /**
     * 从明文密码得到预处理密码
     *
     * @param pwd 明文密码
     *
     * @return 预处理密码
     */
    public static String preHASH(String pwd) {
        String salt = GetSalt();
        pwd = SHA_256(pwd);
        for (int i = 0; i < 2; i++) {
            pwd += salt;
            pwd = SHA_256(pwd);
        }
        return pwd;
    }

    /**
     * 从预处理密码得到最终密码
     *
     * @param pwd 预处理密码
     *
     * @return 最终密码
     */
    public static String LaterHASH(String pwd) {
        return preHASH(pwd);
    }

    /**
     * 获取 SHA-256 散列值
     *
     * @param need 需要散列化的值
     *
     * @return 散列值
     */
    private static String SHA_256(String need) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(need.getBytes());

        byte byteData[] = md.digest();

        // /convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * @return salt值
     */
    private static String GetSalt() {
        return SHA_256("quanzi");
    }
}
