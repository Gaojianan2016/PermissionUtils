# PermissionUtils
权限管理工具

```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}


dependencies {
    implementation 'com.github.Gaojianan2016:PermissionUtils:1.0.1'
}
```

# 基本使用
Activity

```
package com.gjn.permissionutils;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gjn.permissionlibrary.PermissionCallBack;
import com.gjn.permissionlibrary.PermissionUtils;

public class MainActivity extends AppCompatActivity {

    private boolean isShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isShow) {
            if (PermissionUtils.requestPermissions(this, PermissionUtils.CODE_CAMERA,
                    PermissionUtils.CODE_PHONE, PermissionUtils.CODE_STORAGE)) {
                Log.e("-s-", "权限全通过");
            }else {
                Log.e("-s-", "权限部分未通过");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        isShow = true;

        for (String permission : permissions) {
            Log.d("-s-", "===>" + permission);
        }

        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults,
                new PermissionCallBack() {
                    @Override
                    public void onSuccess(int code) {
                        Log.e("-s-", "用户同意全部权限");
                    }

                    @Override
                    public void onFail(int code) {
                        Log.e("-s-", "用户拒绝部分权限");
                    }

                    @Override
                    public void onRetry(int code) {
                        Log.e("-s-", "重试");
                    }

                    @Override
                    public void onSetting(int code) {
                        Log.e("-s-", "进入设置");
                        isShow = false;
                    }
                });

    }
}
```

AndroidManifest

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.gjn.permissionutils">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />


    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```
