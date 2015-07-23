package com.tizi.quanzi.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by qixingchen on 15/7/13.
 */
public class GetPassword {
    public static String fullHash(String pwd) {
        pwd = preHASH(pwd);
        pwd = LaterHASH(pwd);
        return pwd;
    }

    private static String GetSalt() {
        return Hash("quanzi");
    }

    public static String preHASH(String pwd) {
        String salt = GetSalt();
        pwd = Hash(pwd);
        for (int i = 0; i < 2; i++) {
            pwd += salt;
            pwd = Hash(pwd);
        }
        return pwd;
    }

    public static String LaterHASH(String pwd) {
        return preHASH(pwd);
    }

    private static String Hash(String need) {
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
}
