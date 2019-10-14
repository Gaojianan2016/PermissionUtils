package com.gjn.permissionlibrary;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gjn on 2018/6/19.
 */

public class PermissionUtils {
    private static final String TAG = "PermissionUtils";

    public static boolean isDebug = false;

    public static final int CODE_CALENDAR =     0x780;      //日历权限
    public static final int CODE_CAMERA =       0x781;      //相机权限
    public static final int CODE_CONTACTS =     0x782;      //联系人权限
    public static final int CODE_LOCATION =     0x783;      //定位权限
    public static final int CODE_MICROPHONE =   0x784;      //麦克相关权限
    public static final int CODE_PHONE =        0x785;      //手机状态权限
    public static final int CODE_SENSORS =      0x786;      //传感器权限
    public static final int CODE_SMS =          0x787;      //短信权限
    public static final int CODE_STORAGE =      0x788;      //SD卡权限
    public static final int CODE_MULTI =        0x7834;     //多个权限

    //日历权限 0
    public static final String PERMISSION_READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    public static final String PERMISSION_WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    //相机权限 1
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    //联系人权限 2
    public static final String PERMISSION_WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    //定位权限 3
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    //麦克相关权限 4
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    //手机状态权限 5
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_USE_SIP = Manifest.permission.USE_SIP;
    public static final String PERMISSION_PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
    public static final String PERMISSION_ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL;
    public static final String PERMISSION_READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
    public static final String PERMISSION_WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;
    //传感器权限 6
    public static final String PERMISSION_BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    //短信权限 7
    public static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;
    public static final String PERMISSION_RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
    public static final String PERMISSION_RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
    public static final String PERMISSION_RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    public static final String PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS;
    public static final String PERMISSION_READ_CELL_BROADCASTS = "android.permission.READ_CELL_BROADCASTS";
    //SD卡权限 8
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    /**
     * 判断是否请求权限
     *
     * @param activity
     * @param code
     * @param permissions
     * @return true 全部权限通过 | false 有权限未通过
     */
    public static boolean requestPermissions(Activity activity, int code, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.i(TAG, "api < 23! request success");
            return true;
        }
        //未允许权限集合
        List<String> notGranted = new ArrayList<>();
        for (String permission : permissions) {
            //添加未允许的权限
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permission);
            }
        }
        if (notGranted.size() > 0) {
            //向系统请求权限
            print("请求权限: code = " + code);
            for (String str : notGranted) {
                print(str);
            }
            ActivityCompat.requestPermissions(activity, notGranted.toArray(new String[notGranted.size()]), code);
            return false;
        }
        return true;
    }

    /**
     * 判断是否请求权限
     *
     * @param activity
     * @param code
     * @return true 全部权限通过 | false 有权限未通过
     */
    public static boolean requestPermissions(Activity activity, int code) {
        return requestPermissions(activity, new Integer[]{code});
    }

    /**
     * 判断是否请求权限
     *
     * @param activity
     * @param code
     * @return true 全部权限通过 | false 有权限未通过
     */
    public static boolean requestPermissions(Activity activity, Integer... code) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Log.w(TAG, "安卓7.0后，需要传入对应的权限，不能单权限获取整个权限组，建议使用" +
                    "requestPermissions(Activity activity, int[] codes, String... strings) 或者 " +
                    "requestPermissions(Activity activity, int code, String... permissions)");
        }
        String[] permissions = code2String(code);
        return permissions.length == 0 || requestPermissions(activity, CODE_MULTI, permissions);
    }

    public static boolean requestPermissions(Activity activity, int[] codes, String... strings) {
        String[] permissions = concat(strings, code2String(int2Integer(codes)));
        return permissions.length == 0 || requestPermissions(activity, CODE_MULTI, permissions);
    }

    /**
     * 权限获取之后的结果显示
     *
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param permissionCallBack 权限获取结果监听
     */
    public static void requestPermissionsResult(final Activity activity, final int requestCode,
                                                String[] permissions, int[] grantResults,
                                                final PermissionCallBack permissionCallBack) {
        requestPermissionsResult(activity, requestCode, permissions, grantResults, true, permissionCallBack);
    }

    /**
     * 权限获取之后的结果显示
     *
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param showdefaultWindow  是否显示默认权限结果窗口
     * @param permissionCallBack 权限获取结果监听
     */
    public static void requestPermissionsResult(final Activity activity, final int requestCode,
                                                String[] permissions, int[] grantResults,
                                                boolean showdefaultWindow,
                                                final PermissionCallBack permissionCallBack) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.i(TAG, "api < 23! result success");
            return;
        }
        print("requestCode = " + requestCode);
        //未允许权限集合
        List<String> notGranted = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            print("权限 = " + permissions[i] + ", 当前结果 = " + grantResults[i]);
            //添加未允许的权限
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permissions[i]);
            }
        }
        //需重新获取权限集合
        final String[] requestStrings = notGranted.toArray(new String[notGranted.size()]);
        if (notGranted.size() > 0) {
            //有权限被拒绝
            if (showdefaultWindow) {
                createWindow(activity, notGranted,
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
            } else {
                permissionCallBack.onFail(requestCode);
            }
        } else {
            //全部权限通过
            permissionCallBack.onSuccess(requestCode);
        }
    }

    private static void print(String str) {
        if (isDebug) {
            Log.d(TAG, str);
        }
    }

    private static void startAppSetting(Activity activity) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(intent);
    }

    private static void createWindow(Activity activity, List<String> notGranted,
                                    DialogInterface.OnClickListener retrylistener,
                                    DialogInterface.OnClickListener settinglistener,
                                    DialogInterface.OnClickListener canclelistener) {
        StringBuilder message = new StringBuilder();
        message.append(notGranted.size()).append("个权限被拒绝\n");
        for (String str : notGranted) {
            message.append(getLast(str)).append("\n");
        }
        message.append("\n尝试重新获取\n\n如果失败请进入设置中修改\n\n")
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

    private static String getLast(String str){
        String[] strings = str.split("[.]");
        if (strings.length > 1) {
            return strings[strings.length - 1];
        }
        return str;
    }

    private static Integer[] int2Integer(int[] ints){
        Integer[] result = new Integer[ints.length];
        for (int i = 0; i < ints.length; i++) {
            result[i] = ints[i];
        }
        return result;
    }

    private static <T> T[] concat(T[] t1, T[] t2){
        if (t1 == null || t1.length == 0) {
            return t2;
        }
        if (t2 == null || t2.length == 0) {
            return t1;
        }
        T[] result = Arrays.copyOf(t1, t1.length+t2.length);
        System.arraycopy(t2, 0, result, result.length - t2.length, t2.length);
        return result;
    }

    @SafeVarargs
    private static <T> T[] concat(T[] t1, T[]... ts){
        T[] result = Arrays.copyOf(t1, t1.length);
        for (int i = 0; i < ts.length; i++) {
            if (i < ts.length) {
                result = concat(result, ts[i]);
            }
        }
        return result;
    }

    private static String[] code2String(Integer... code){
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
        return permissions.toArray(new String[permissions.size()]);
    }
}
