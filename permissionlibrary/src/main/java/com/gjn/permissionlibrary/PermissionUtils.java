package com.gjn.permissionlibrary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gjn on 2018/6/19.
 */

public class PermissionUtils {
    private static final String TAG = "PermissionUtils";

    public static final int CODE_CALENDAR = 0;      //日历权限
    public static final int CODE_CAMERA = 1;        //相机权限
    public static final int CODE_CONTACTS = 2;      //联系人权限
    public static final int CODE_LOCATION = 3;      //定位权限
    public static final int CODE_MICROPHONE = 4;    //麦克相关权限
    public static final int CODE_PHONE = 5;         //手机状态权限
    public static final int CODE_SENSORS = 6;       //传感器权限
    public static final int CODE_SMS = 7;           //短信权限
    public static final int CODE_STORAGE = 8;       //SD卡权限
    public static final int CODE_MULTI = 111;       //多个权限

    //日历权限 0
    public static final String PERMISSION_READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    //相机权限 1
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    //联系人权限 2
    public static final String PERMISSION_WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
    //定位权限 3
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    //麦克相关权限 4
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    //手机状态权限 5
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    //传感器权限 6
    public static final String PERMISSION_BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    //短信权限 7
    public static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;
    //SD卡权限 8
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    /**
     * 判断是否请求权限
     * @param activity
     * @param code
     * @param permissions
     * @return true 全部权限通过 | false 有权限未通过
     */
    public static boolean requestPermissions(Activity activity, int code, String... permissions){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            Log.i(TAG, "api < 23!");
            return true;
        }
        //未允许权限集合
        List<String> notGranted = new ArrayList<>();
        for (String permission : permissions) {
            //添加未允许的权限
            if(ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                notGranted.add(permission);
            }
        }
        if (notGranted.size() > 0) {
            //向系统请求权限
            ActivityCompat.requestPermissions(activity, notGranted.toArray(new String[notGranted.size()]), code);
            return false;
        }
        return true;
    }

    /**
     * 判断是否请求权限
     * @param activity
     * @param code
     * @return true 全部权限通过 | false 有权限未通过
     */
    public static boolean requestPermissions(Activity activity, Integer... code){
        List<String> permissions = new ArrayList<>();
        List<Integer> codes = Arrays.asList(code);
        //日历权限
        if (codes.contains(CODE_CALENDAR)) {
            permissions.add(PERMISSION_READ_CALENDAR);
        }
        //相机权限
        if (codes.contains(CODE_CAMERA)) {
            permissions.add(PERMISSION_CAMERA);
        }
        //联系人权限
        if (codes.contains(CODE_CONTACTS)) {
            permissions.add(PERMISSION_WRITE_CONTACTS);
        }
        //定位权限
        if (codes.contains(CODE_LOCATION)) {
            permissions.add(PERMISSION_ACCESS_FINE_LOCATION);
        }
        //麦克相关权限
        if (codes.contains(CODE_MICROPHONE)) {
            permissions.add(PERMISSION_RECORD_AUDIO);
        }
        //手机状态权限
        if (codes.contains(CODE_PHONE)) {
            permissions.add(PERMISSION_READ_PHONE_STATE);
        }
        //传感器权限
        if (codes.contains(CODE_SENSORS)) {
            permissions.add(PERMISSION_BODY_SENSORS);
        }
        //短信权限
        if (codes.contains(CODE_SMS)) {
            permissions.add(PERMISSION_READ_SMS);
        }
        //SD卡权限
        if (codes.contains(CODE_STORAGE)) {
            permissions.add(PERMISSION_READ_EXTERNAL_STORAGE);
        }

        if (permissions.size() == 0){
            return true;
        }
        return requestPermissions(activity, CODE_MULTI, permissions.toArray(new String[permissions.size()]));
    }

    /**
     * 权限获取之后的结果显示
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param permissionCallBack    权限获取结果监听
     */
    public static void requestPermissionsResult(final Activity activity, final int requestCode,
                                                String[] permissions, int[] grantResults,
                                                final PermissionCallBack permissionCallBack){
        requestPermissionsResult(activity, requestCode, permissions, grantResults, true, permissionCallBack);
    }

    /**
     * 权限获取之后的结果显示
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param showdefaultWindow     是否显示默认权限结果窗口
     * @param permissionCallBack    权限获取结果监听
     */
    public static void requestPermissionsResult(final Activity activity, final int requestCode,
                                                String[] permissions, int[] grantResults,
                                                boolean showdefaultWindow,
                                                final PermissionCallBack permissionCallBack){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            Log.i(TAG, "api < 23!");
            return;
        }
        //未允许权限集合
        List<String> notGranted = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            //添加未允许的权限
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                notGranted.add(permissions[i]);
            }
        }
        //需重新获取权限集合
        final String[] requestStrings = notGranted.toArray(new String[notGranted.size()]);
        if (notGranted.size() > 0) {
            //有权限被拒绝
            if (showdefaultWindow) {
                creatWindow(activity, notGranted,
                        //重新获取
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(activity, requestCode, requestStrings);
                                permissionCallBack.onRetry(requestCode);
                            }
                        },
                        //进入设置
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startAppSetting(activity);
                                permissionCallBack.onSetting(requestCode);
                            }
                        },
                        //退出
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                permissionCallBack.onFail(requestCode);
                            }
                        });
            }else {
                permissionCallBack.onFail(requestCode);
            }
        }else {
            //全部权限通过
            permissionCallBack.onSuccess(requestCode);
        }
    }

    private static void startAppSetting(Activity activity) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(intent);
    }

    private static void creatWindow(Activity activity, List<String> notGranted,
                                    DialogInterface.OnClickListener retrylistener,
                                    DialogInterface.OnClickListener settinglistener,
                                    DialogInterface.OnClickListener canclelistener) {
        StringBuilder message = new StringBuilder();
        message.append(notGranted.size()).append("个权限被拒绝\n\n")
                .append("尝试重新获取\n\n如果失败请进入设置中修改\n\n")
                .append("点击进入设置跳转 软件设置 -> 权限管理");

        new AlertDialog.Builder(activity)
                .setTitle("帮助")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("重新获取", retrylistener)
                .setNeutralButton("进入设置", settinglistener)
                .setNegativeButton("退出", canclelistener)
                .create()
                .show();
    }
}
