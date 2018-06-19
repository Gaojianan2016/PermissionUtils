package com.gjn.permissionlibrary;

/**
 * Created by gjn on 2018/6/19.
 */

public interface PermissionCallBack {
    void onSuccess(int code);

    void onFail(int code);

    void onRetry(int code);

    void onSetting(int code);
}
