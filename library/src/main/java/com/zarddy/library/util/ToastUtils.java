package com.zarddy.library.util;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zarddy.library.R;

public class ToastUtils {

    public static void showText(Context context, CharSequence text) {

        showText(context, text, Toast.LENGTH_SHORT);
    }

    public static void showText(Context context, CharSequence text, int duration) {

        Toast toast = Toast.makeText(context, text, duration);
        show(toast);
    }

    public static void showText(Context context, @StringRes int resId) {

        showText(context, resId, Toast.LENGTH_SHORT);
    }

    public static void showText(Context context, @StringRes int resId, int duration) {

        Toast toast = Toast.makeText(context, resId, duration);
        show(toast);
    }

    public static void showToast(Context context, @DrawableRes int iconResId, @StringRes int textResId) {
        showToast(context, iconResId, textResId, R.color.white);
    }

    public static void showToast(Context context, @DrawableRes int iconResId,
                                 @StringRes int textResId, @ColorRes int colorResId) {
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        ImageView icon = (ImageView) toastView.findViewById(R.id.icon);
        icon.setImageResource(iconResId);
        TextView text = (TextView) toastView.findViewById(R.id.text);
        text.setText(textResId);
        text.setTextColor(context.getResources().getColor(colorResId));

        Toast toast = createIconToast(context);
        toast.setView(toastView);
        show(toast);
    }

    public static void showToast(Context context, @DrawableRes int iconResId, String content) {
        showToast(context, iconResId, content, R.color.white);
    }

    public static void showToast(Context context, @DrawableRes int iconResId,
                                 String content, @ColorRes int colorResId) {
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        ImageView icon = (ImageView) toastView.findViewById(R.id.icon);
        icon.setImageResource(iconResId);
        TextView text = (TextView) toastView.findViewById(R.id.text);
        text.setText(content);
        text.setTextColor(context.getResources().getColor(colorResId));

        Toast toast = createIconToast(context);
        toast.setView(toastView);
        show(toast);
    }

    private static Toast createIconToast(Context context) {
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    private static void show(Toast toast) {
        toast.show();
    }
}
