package com.zarddy.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.math.BigDecimal;

public class ScreenUtils {

    /**
     * 获取状态栏高度（像素）
     * @return 状态栏高度
     */
    public static int getStatueBarHeight(Context context) {//拿取状态栏的高度
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return (int) context.getResources().getDimension(identifier);
        }
        return 0;
    }

    public static DisplayMetrics getDefaultMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (null == wm)
            return null;

        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取屏幕ppi
     */
    public static int getScreenDpi(Context context) {
        DisplayMetrics metrics = getDefaultMetrics(context);
        if (null == metrics)
            return 0;

        return metrics.densityDpi;
    }

    /**
     * 获取屏幕宽度（像素）
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = getDefaultMetrics(context);
        if (null == metrics)
            return 0;

        return metrics.widthPixels;
    }

    /**
     * 获取屏幕高度（像素）
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = getDefaultMetrics(context);
        if (null == metrics)
            return 0;

        return metrics.heightPixels;
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) (dpValue * getScreenDensity(context) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / getScreenDensity(context) + 0.5f);
    }

    /**
     * 屏幕缩放率
     * @return 屏幕缩放率
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 设置屏幕是否保持常亮
     * @param state 是否常亮
     */
    public static void keepScreenOn(Activity activity, boolean state) {
        if (state) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    /**
     * 判断设备是否大屏幕或者横屏
     * @return true：平板（横屏），false：手机（竖屏）
     */
    public static boolean isPadOrLandscape(Context context) {
        return ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
    }

    // 屏幕尺寸（英寸）
    private static double SCREEN_SIZE_INCH = 0;

    /**
     * 获取屏幕尺寸（英寸）
     */
    public static double getScreenSizeInch(Context context) {
        if (SCREEN_SIZE_INCH > 0) {
            return SCREEN_SIZE_INCH;
        }

        try {
            int realWidth = 0, realHeight = 0;
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager == null) {
                return SCREEN_SIZE_INCH;
            }

            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Point size = new Point();
                display.getRealSize(size);
                realWidth = size.x;
                realHeight = size.y;

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);

            } else {
                realWidth = metrics.widthPixels;
                realHeight = metrics.heightPixels;
            }

            double screenSize = Math.sqrt( Math.pow((realWidth/metrics.xdpi), 2) + Math.pow((realHeight/metrics.ydpi), 2));
            BigDecimal bd = new BigDecimal(screenSize);
            SCREEN_SIZE_INCH = bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return SCREEN_SIZE_INCH;
    }

    /**
     * 获取设备屏幕类型，默认平板，当屏幕大于等于10寸，则作为一体机
     */
    public static ScreenType getDeviceScreenType(Context context) {
        if (isPadOrLandscape(context)) { // 如果是横屏设备或平板
            if (getScreenSizeInch(context) >= 10D) { // 大于等于10寸的设备，默认当作一体机
                return ScreenType.ALL_IN_ONE;
            } else {
                return ScreenType.PAD;
            }
        } else {
            return ScreenType.PHONE;
        }
    }
}
