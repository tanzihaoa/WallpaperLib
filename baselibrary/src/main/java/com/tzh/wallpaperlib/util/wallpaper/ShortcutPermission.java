package com.tzh.wallpaperlib.util.wallpaper;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ZP on 2019-12-02.
 */
public class ShortcutPermission {

    private static final String TAG = "ShortcutPermission";
    @Retention(RetentionPolicy.SOURCE)
    public @interface PermissionResult {}

    /**
     * Permission check result: this is returned by {@link #check(Context)}
     * if the permission has been granted to the given package.
     */
    public static final int PERMISSION_GRANTED = 0;

    /**
     * Permission check result: this is returned by {@link #check(Context)}
     * if the permission has not been granted to the given package.
     */
    public static final int PERMISSION_DENIED = -1;

    public static final int PERMISSION_ASK = 1;

    public static final int PERMISSION_UNKNOWN = 2;

    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    @PermissionResult
    public static Boolean check(Context context) {
        Log.d(TAG, "manufacturer = " + MARK + ", api level= " + Build.VERSION.SDK_INT);
        int result = PERMISSION_UNKNOWN;
        if (MARK.contains("huawei")) {
            result = ShortcutPermissionChecker.checkOnEMUI(context);
        } else if (MARK.contains("xiaomi")) {
            result = ShortcutPermissionChecker.checkOnMIUI(context);
        } else if (MARK.contains("oppo")) {
            result = ShortcutPermissionChecker.checkOnOPPO(context);
        } else if (MARK.contains("vivo")) {
            result = ShortcutPermissionChecker.checkOnVIVO(context);
        } else if (MARK.contains("samsung") || MARK.contains("meizu")) {
            result = PERMISSION_GRANTED;
        }
        return result == ShortcutPermission.PERMISSION_GRANTED;
    }

    /**
     * 获取系统版本
     * @return
     */
    private static int getSystemVersion(){
        return Build.VERSION.SDK_INT;
    }
}
