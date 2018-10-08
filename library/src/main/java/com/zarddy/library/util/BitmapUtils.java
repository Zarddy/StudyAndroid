package com.zarddy.library.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapUtils {

    private static final int DEFAULT_OTHER_DRAWABLE_WIDTH = 50;
    private static final int DEFAULT_OTHER_DRAWABLE_HEIGHT = 50;

    /**
     * 从Drawable中获取Bitmap对象
     * @param drawable 需要转换成bitmap的drawable对象
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        try {
            if (drawable == null) {
                return null;
            }

            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            Bitmap bitmap = Bitmap.createBitmap(
                    intrinsicWidth <= 0 ? DEFAULT_OTHER_DRAWABLE_WIDTH
                            : intrinsicWidth,
                    intrinsicHeight <= 0 ? DEFAULT_OTHER_DRAWABLE_HEIGHT
                            : intrinsicHeight, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;

        } catch (OutOfMemoryError e) {
            return null;
        }
    }
}
