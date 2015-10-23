package com.tizi.quanzi.otto;

/**
 * Created by qixingchen on 15/10/15.
 * 权限回调
 */
public class PermissionAnser {
    public int requestCode;
    public String[] permissions;
    public int[] grantResults;
    public boolean allGreen;

    public static PermissionAnser getAns(int requestCode, String[] permissions, int[] grantResults, boolean allGreen) {
        PermissionAnser permissionAnser = new PermissionAnser();
        permissionAnser.requestCode = requestCode;
        permissionAnser.permissions = permissions;
        permissionAnser.grantResults = grantResults;
        permissionAnser.allGreen = allGreen;
        return permissionAnser;
    }
}
