package com.tizi.quanzi.otto;

/**
 * Created by qixingchen on 15/10/15.
 * 权限回调
 */
public class PermissionAnser {
    public int requestCode;
    public String[] permissions;
    public int[] grantResults;

    public static PermissionAnser getAns(int requestCode, String[] permissions, int[] grantResults) {
        PermissionAnser permissionAnser = new PermissionAnser();
        permissionAnser.requestCode = requestCode;
        permissionAnser.permissions = permissions;
        permissionAnser.grantResults = grantResults;
        return permissionAnser;
    }
}
