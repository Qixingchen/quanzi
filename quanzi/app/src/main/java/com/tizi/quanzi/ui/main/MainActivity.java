package com.tizi.quanzi.ui.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.integration.android.IntentIntegrator;
import com.squareup.otto.Subscribe;
import com.tizi.quanzi.BuildConfig;
import com.tizi.quanzi.R;
import com.tizi.quanzi.chat.MyAVIMClientEventHandler;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.dataStatic.SystemMessageList;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.otto.AVIMNetworkEvents;
import com.tizi.quanzi.otto.ActivityResultAns;
import com.tizi.quanzi.otto.FragmentResume;
import com.tizi.quanzi.otto.PermissionAnser;
import com.tizi.quanzi.tool.GetAppVersion;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseActivity;
import com.tizi.quanzi.ui.quanzi_zone.QuanziZoneActivity;
import com.tizi.quanzi.ui.theme.ThemeSignUpFragment;


public class MainActivity extends BaseActivity {
    private String toolbarTitle = "主题";
    private MainFragment mainFragment;
    private NotifiMessageFragment notifiMessageFragment;
    private UserInfoSetFragment userInfoSetFragment;


    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: 15/10/13 play政策不允许做这个 do not forget delete
        if (!BuildConfig.DEBUG) {
            GetAppVersion.doit(this);
        }
        Intent joinGroup = getIntent();
        if (Intent.ACTION_VIEW.equals(joinGroup.getAction())) {
            Uri uri = joinGroup.getData();
            toJoinGroup(uri);
        }
    }

    @Override
    protected void findView() {
        mainFragment = new MainFragment();
    }

    @Override
    protected void initView() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, mainFragment).commit();
    }

    @Override
    protected void setViewEvent() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        onChanged();
        return true;
    }

    @Subscribe
    public void onChanged(SystemMessageList ignore) {
        onChanged();
    }

    @Subscribe
    public void onChanged(PrivateMessPairList ignore) {
        onChanged();
    }

    private void onChanged() {
        if (menu == null) {
            return;
        }
        MenuItem item = menu.findItem(R.id.action_notifi_message);
        int num = PrivateMessPairList.getInstance().getAllUnreadCount()
                + SystemMessageList.getInstance().getAllUnreadCount();
        if (num != 0) {

            item.setTitle("消息(" + num + ")条");
        } else {

            item.setTitle("消息");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notifi_message) {
            notifiMessageFragment = new NotifiMessageFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_from_action_button, R.anim.disapear,
                            R.anim.no_change, R.anim.slide_back_to_action_button)
                    .replace(R.id.fragment, notifiMessageFragment)
                    .addToBackStack("notifiMessageFragment").commit();
        }
        if (id == R.id.action_scan_qr_code) {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.setPackage("com.google.zxing.client.android");
            if (Tool.isIntentSafe(mActivity, intent)) {
                mActivity.startActivityForResult(intent, StaticField.PermissionRequestCode.QrCodeScan_QuestCode);
            } else {
                ScanQRCodeBySelf();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /*订阅通知界面的恢复和暂停*/
    @Subscribe
    public void onFragmentResume(FragmentResume fragmentResume) {
        if (fragmentResume.FramgentName.equals(NotifiMessageFragment.class.getSimpleName())) {


            if (menu == null) {
                return;
            }
            if (fragmentResume.resumeOrPause) {
                menu.findItem(R.id.action_notifi_message).setVisible(false);
            } else {
                menu.findItem(R.id.action_notifi_message).setVisible(true);
            }
        }
        if (fragmentResume.FramgentName.equals(ThemeSignUpFragment.class.getSimpleName())) {
            if (fragmentResume.resumeOrPause) {
                toolbar.setTitle("报名");
            } else {
                toolbar.setTitle(toolbarTitle);
            }
        }
    }

    /*LC网络状态更改*/
    @Subscribe
    public void onNetworkChange(AVIMNetworkEvents avimNetworkEvents) {
        if (toolbar == null) {
            return;
        }
        toolbar.setTitle(avimNetworkEvents.isNetWorkAvailable ? toolbarTitle : "等待网络");
    }

    /*更换Toolbar Title*/
    public void onTabChanged(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
        if (!toolbar.getTitle().toString().equals("等待网络")) {
            toolbar.setTitle(toolbarTitle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(MyAVIMClientEventHandler.getInstance().isNetworkAvailable ? toolbarTitle : "等待网络");
        onChanged();
    }

    public void StartUserInfoSet() {
        userInfoSetFragment = new UserInfoSetFragment();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.disapear,
                        R.anim.no_change, R.anim.slide_out_to_bottom)
                .replace(R.id.fragment, userInfoSetFragment).addToBackStack("userInfoSetFragment").commit();
    }

    private void ScanQRCodeBySelf() {
        //        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        //            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, StaticField.PermissionRequestCode.QrCodeScan);
        //            return;
        //        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(String.format("%s需要扫码应用", StaticField.AppName.AppZHName))
                    .setMessage("由于Android Marshmallow新的权限体系，索取相机权限将导致无法使用Intent获取系统相机的拍摄结果。您需要安装扫码应用才能使用此功能。 ")
                    .setPositiveButton("下载条码扫描器", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent installApk = new Intent(Intent.ACTION_VIEW);
                            installApk.setData(Uri.parse("market://details?id=com.google.zxing.client.android"));
                            startActivity(installApk);
                        }
                    })
                    .setNeutralButton("查看权限说明", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent permissionNotice = new Intent(Intent.ACTION_VIEW);
                            permissionNotice.setData(Uri.parse(mActivity.getString(R.string.uses_permission)));
                            if (Tool.isIntentSafe(mActivity, permissionNotice)) {
                                mActivity.startActivity(permissionNotice);
                            }
                        }
                    })
                    .show();
            return;
        }
        new IntentIntegrator(this).initiateScan();
    }

    @Subscribe
    public void onActivityResult(ActivityResultAns ans) {
        if (ans.requestCode == StaticField.PermissionRequestCode.QrCodeScan_QuestCode) {
            if (ans.resultCode != Activity.RESULT_OK) {
                return;
            }
            String contents = ans.data.getStringExtra("SCAN_RESULT");
            toJoinGroup(Uri.parse(contents));
        }
    }

    @Subscribe
    public void onPermission(PermissionAnser anser) {
        if (anser.requestCode != StaticField.PermissionRequestCode.QrCodeScan) {
            return;
        }
        if (anser.allGreen) {
            ScanQRCodeBySelf();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(String.format("%s需要相机权限", StaticField.AppName.AppZHName))
                    .setMessage("扫码功能需要相机权限，否则无法继续，请点击设置授权。您也可以安装ZXing Team的条码扫描器在不授权的情况下使用此功能。")
                    .setPositiveButton("设置权限", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + getApplicationContext().getPackageName())));
                        }
                    }).setNeutralButton("下载条码扫描器", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent installApk = new Intent(Intent.ACTION_VIEW);
                    installApk.setData(Uri.parse("market://details?id=com.google.zxing.client.android"));
                    startActivity(installApk);
                }
            }).show();
        }
    }

    /**
     * 获取uri，tizi-tech.com下，开始加入圈子
     */
    private void toJoinGroup(Uri uri) {
        if (uri.getScheme() != null && (uri.getScheme().equals("http") || uri.getScheme().equals("https"))) {
            if (uri.getHost().contains("tizi-tech.com")) {
                String contents = uri.toString();
                if (contents.contains("joinGroup")) {
                    if (Tool.isGuest()) {
                        Tool.GuestAction(mContext);
                        return;
                    }
                    int last = contents.lastIndexOf("=");
                    String groupID = contents.substring(last + 1);
                    boolean forjoin = true;
                    for (Object group : GroupList.getInstance().getGroupList()) {
                        if (((GroupClass) group).ID.equals(groupID)) {
                            forjoin = false;
                            break;
                        }
                    }
                    Intent groupZone = new Intent(mContext, QuanziZoneActivity.class);
                    groupZone.putExtra("groupID", groupID);
                    groupZone.putExtra("forJoin", forjoin);
                    startActivity(groupZone);
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                if (Tool.isIntentSafe(mActivity, intent)) {
                    startActivity(intent);
                }
            }
        } else {
            Snackbar.make(view, "不支持的值", Snackbar.LENGTH_LONG).show();
        }
    }
}
