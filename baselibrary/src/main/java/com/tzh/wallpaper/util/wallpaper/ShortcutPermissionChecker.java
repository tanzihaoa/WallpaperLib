package com.tzh.wallpaper.util.wallpaper;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ShortcutPermissionChecker {
    private static final String TAG = "ShortcutPermissionCheck";

    @ShortcutPermission.PermissionResult
    public static int checkOnEMUI(Context context) {
        Log.d(TAG, "checkOnEMUI");
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        try {
            Class<?> PermissionManager = Class.forName("com.huawei.hsm.permission.PermissionManager");
            Method canSendBroadcast = PermissionManager.getDeclaredMethod("canSendBroadcast", Context.class, Intent.class);
            boolean invokeResult = (boolean) canSendBroadcast.invoke(PermissionManager, context, intent);
            //hsm.permission.PermissionManager.canSendBroadcast
            Log.d(TAG, "EMUI check permission canSendBroadcast invoke result = " + invokeResult);
            if (invokeResult) {
                return ShortcutPermission.PERMISSION_GRANTED;
            } else {
                return ShortcutPermission.PERMISSION_DENIED;
            }
        } catch (ClassNotFoundException e) {//Mutil-catch require API level 19
            return ShortcutPermission.PERMISSION_UNKNOWN;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return ShortcutPermission.PERMISSION_UNKNOWN;
        }
    }

    @ShortcutPermission.PermissionResult
    public static int checkOnVIVO(Context context) {
        Log.d(TAG, "checkOnVIVO");
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            Log.d(TAG, "contentResolver is null");
            return ShortcutPermission.PERMISSION_UNKNOWN;
        }

        Cursor query = null;
        try {
            ///这里在android vivo（V1838A、V1928A、V1901A (vivo Y3)上会出现异常，只有系统应用才能访问nnd）
            Uri parse = Uri.parse("content://com.bbk.launcher2.settings/favorites");
            query = contentResolver.query(parse, null, null, null, null);
            if (query == null) {
                Log.d(TAG, "cursor is null (Uri : content://com.bbk.launcher2.settings/favorites)");
                return ShortcutPermission.PERMISSION_UNKNOWN;
            }

            while (query.moveToNext()) {
                String titleByQueryLauncher = query.getString(query.getColumnIndexOrThrow("title"));
                Log.d(TAG, "title by query is " + titleByQueryLauncher);
                if (!TextUtils.isEmpty(titleByQueryLauncher) && titleByQueryLauncher.equals(getAppName(context))) {
                    int value = query.getInt(query.getColumnIndexOrThrow("shortcutPermission"));
                    Log.d(TAG, "permission value is " + value);
                    if (value == 1 || value == 17) {
                        return ShortcutPermission.PERMISSION_DENIED;
                    } else if (value == 16) {
                        return ShortcutPermission.PERMISSION_GRANTED;
                    } else if (value == 18) {
                        return ShortcutPermission.PERMISSION_ASK;
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        } finally {
            if (query != null) {
                query.close();
            }
        }
        return ShortcutPermission.PERMISSION_UNKNOWN;
    }

    @ShortcutPermission.PermissionResult
    public static int checkOnMIUI(Context context) {
        Log.d(TAG, "checkOnMIUI");
        try {
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            String pkg = context.getApplicationContext().getPackageName();
            int uid = context.getApplicationInfo().uid;
            Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getDeclaredMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
            Object invoke = checkOpNoThrowMethod.invoke(mAppOps, 10017, uid, pkg);//the ops of INSTALL_SHORTCUT is 10017
            if (invoke == null) {
                Log.d(TAG, "MIUI check permission checkOpNoThrowMethod(AppOpsManager) invoke result is null");
                return ShortcutPermission.PERMISSION_UNKNOWN;
            }
            String result = invoke.toString();
            Log.d(TAG, "MIUI check permission checkOpNoThrowMethod(AppOpsManager) invoke result = " + result);
            switch (result) {
                case "0":
                    return ShortcutPermission.PERMISSION_GRANTED;
                case "1":
                    return ShortcutPermission.PERMISSION_DENIED;
                case "5":
                    return ShortcutPermission.PERMISSION_ASK;
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
            return ShortcutPermission.PERMISSION_UNKNOWN;
        }
        return ShortcutPermission.PERMISSION_UNKNOWN;
    }

    @ShortcutPermission.PermissionResult
    public static int checkOnOPPO(Context context) {
        Log.d(TAG, "checkOnOPPO");
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            Log.d(TAG, "contentResolver is null");
            return ShortcutPermission.PERMISSION_UNKNOWN;
        }
        Uri parse = Uri.parse("content://settings/secure/launcher_shortcut_permission_settings");
        Cursor query = contentResolver.query(parse, null, null, null, null);
        if (query == null) {
            Log.d(TAG, "cursor is null (Uri : content://settings/secure/launcher_shortcut_permission_settings)");
            return ShortcutPermission.PERMISSION_UNKNOWN;
        }
        try {
            String pkg = context.getApplicationContext().getPackageName();
            while (query.moveToNext()) {
                @SuppressLint("Range") String value = query.getString(query.getColumnIndex("value"));
                Log.d(TAG, "permission value is " + value);
                if (!TextUtils.isEmpty(value)) {
                    if (value.contains(pkg + ", 1")) {
                        return ShortcutPermission.PERMISSION_GRANTED;
                    }
                    if (value.contains(pkg + ", 0")) {
                        return ShortcutPermission.PERMISSION_DENIED;
                    }
                }
            }
            return ShortcutPermission.PERMISSION_UNKNOWN;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
            return ShortcutPermission.PERMISSION_UNKNOWN;
        } finally {
            query.close();
        }
    }

    private static String getAppName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 快捷方式权限的获取
     * --start
     */
    /**
     * 是否创建了快捷方式
     *
     * @return
     */
//    public static boolean hasShortcut(Context context) {
//        if (Build.VERSION.SDK_INT >= 25) {
//            ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
//            List<ShortcutInfo> infos = shortcutManager.getPinnedShortcuts();
//            for (int i = 0; i < infos.size(); i++) {
//                ShortcutInfo info = infos.get(i);
//                if (info.getId().equals(mPinShortcutId)) {
//                    return true;
//                }
//            }
//        } else {
//            return isShortCutExist(context, context.getString(R.string.openDoor_oneKey));
//
//        }
//        return false;
//    }

    public static boolean isShortCutExist(Context context, String appName) {
        boolean isInstallShortcut = false;
        if (null == context || TextUtils.isEmpty(appName)) {
            return isInstallShortcut;
        }
        String AUTHORITY = getAuthority(context);
        Log.i("ShortCutUtil", "AUTHORITY = " + AUTHORITY);
        final ContentResolver cr = context.getContentResolver();
        if (!TextUtils.isEmpty(AUTHORITY)) {
            try {
                final Uri CONTENT_URI = Uri.parse(AUTHORITY);
                Cursor c = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?", new String[]{appName}, null);
                if (c != null && c.getCount() > 0) {
                    isInstallShortcut = true;
                }
                if (null != c && !c.isClosed())
                    c.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return isInstallShortcut;
    }

    public static String getAuthority(Context context) {
        // 获取默认
        String authority = getAuthorityFromPermissionDefault(context);
        Log.i("ShortCutUtil", "获取默认 AUTHORITY = " + authority);
        // 获取特殊第三方
        if (authority == null || authority.trim().equals("")) {
            String packageName = getCurrentLauncherPackageName(context);
            packageName += ".permission.INSTALL_SHORTCUT";//packageName += ".permission.READ_SETTINGS";
            authority = getThirdAuthorityFromPermission(context, packageName);
        }
        Log.i("ShortCutUtil", "获取特殊第三方 AUTHORITY = " + authority);
        // 还是获取不到，直接写死
        if (TextUtils.isEmpty(authority)) {
            int sdkInt = android.os.Build.VERSION.SDK_INT;
            if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                authority = "com.android.launcher.settings";
            } else if (sdkInt < 19) {// Android 4.4以下
                authority = "com.android.launcher2.settings";
            } else {// 4.4以及以上
                authority = "com.android.launcher3.settings";
            }
        }
        Log.i("ShortCutUtil", "写死 AUTHORITY = " + authority);
        authority = "content://" + authority + "/favorites?notify=true";
        return authority;
    }

    public static String getAuthorityFromPermissionDefault(Context context) {
        return getThirdAuthorityFromPermission(context, "com.android.launcher.permission.READ_SETTINGS");
    }

    public static String getThirdAuthorityFromPermission(Context context, String permission) {
        if (TextUtils.isEmpty(permission)) {
            return "";
        }
        try {
            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
            if (packs == null) {
                return "";
            }
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission) || permission.equals(provider.writePermission)) {
                            // 精准匹配launcher.settings，再一次验证
                            if (!TextUtils.isEmpty(provider.authority) && (provider.authority).contains(".launcher.settings"))
                                return provider.authority;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurrentLauncherPackageName(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res == null || res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return "";
        }
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }
    }

    /**
     * 快捷方式权限的获取
     * --end
     */
}
