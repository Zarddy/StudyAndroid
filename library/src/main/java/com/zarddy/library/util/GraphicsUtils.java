package com.zarddy.library.util;

public class GraphicsUtils {

    /**
     * 弧度换算成角度
     *
     * @return
     */
    public static double radianToDegree(double radian) {
        return radian * 180 / Math.PI;
    }

    /**
     * 角度换算成弧度
     * @param degree
     * @return
     */
    public static double degreeToRadian(double degree) {
        return degree * Math.PI / 180;
    }
}
