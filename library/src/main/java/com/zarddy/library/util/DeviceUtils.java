package com.zarddy.library.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.view.inputmethod.InputMethodManager;

import java.io.File;

/**
 * 拨号方法
 */

public class DeviceUtils {

    /**
     * 调用安装程序
     * @param filepath 安装包目标路径
     */
    public static void installApp(final Context context, final String filepath) {

        try {
            File appFile = new File(filepath);
            if (!appFile.exists()) {
                return;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data;
            // 判断版本大于等于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 在清单文件中配置的authorities
                data = FileProvider.getUriForFile(context, PackageUtils.getPackageName(context) + ".fileprovider", appFile);
                // 给目标应用一个临时授权
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                data = Uri.fromFile(appFile);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            intent.setDataAndType(data, "application/vnd.android.package-archive");
            context.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 收起软键盘
     */
    public static void hideSoftKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_HIDDEN , InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
