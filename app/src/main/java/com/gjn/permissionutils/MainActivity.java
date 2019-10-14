package com.gjn.permissionutils;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gjn.permissionlibrary.PermissionCallBack;
import com.gjn.permissionlibrary.PermissionUtils;

public class MainActivity extends AppCompatActivity {

    private boolean isShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtils.isDebug = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isShow) {
            if (PermissionUtils.requestPermissions(this, new int[]{PermissionUtils.CODE_CAMERA,
                    PermissionUtils.CODE_PHONE, PermissionUtils.CODE_STORAGE},
                    PermissionUtils.PERMISSION_ACCESS_COARSE_LOCATION)) {
                Log.e("-s-", "权限全通过");
            } else {
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
