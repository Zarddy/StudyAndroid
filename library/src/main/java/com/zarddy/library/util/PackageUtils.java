package com.zarddy.library.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public class PackageUtils {

    /**
     * @return 获取包信息
     */
    public static PackageInfo getPackageInfo(Context context) {

        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = null;
        if (pm != null) {
            try {
                packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            } catch (PackageManager.NameNotFoundException e) {
                packageInfo = null;
            }
        }
        return packageInfo;
    }

    /**
     * @return 获取包名
     */
    public static String getPackageName(Context context) {

        PackageInfo packageInfo = getPackageInfo(context);
        String pkgName = "";

        if (packageInfo != null) {
            pkgName = packageInfo.packageName;
        }
        return pkgName;
    }

    /**
     * @return 获取版本名称
     */
    public static String getVersionName(Context context) {

        PackageInfo packageInfo = getPackageInfo(context);
        String verName = "";

        if (packageInfo != null) {
            verName = packageInfo.versionName; // 版本名
        }
        return verName;
    }

    /**
     * @return 获取版本号
     */
    public static int getVersionCode(Context context) {

        PackageInfo packageInfo = getPackageInfo(context);
        int verCode = 0;

        if (packageInfo != null) {
            verCode = packageInfo.versionCode; // 版本号
        }
        return verCode;
    }

    /**
     * 跳转到应用在应用商店的详情页
     * @param packageName 应用的包名
     */
    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
