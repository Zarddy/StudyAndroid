package com.zarddy.library.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkState {

    private final static int NET_TYPE_NO = 0;
    private final static int NET_TYPE_UNKOWN = 1;
    private final static int NET_TYPE_2G = 2;
    private final static int NET_TYPE_3G = 3;
    private final static int NET_TYPE_4G = 4;

    private static ConnectivityManager connManager;
    private static NetworkInfo networkInfo;

    /**
     * 判断当前是否已连接网络
     */
    public static boolean isNetworkConnected(Context context) {
        boolean isConnected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
                return false;
            }
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            isConnected = (networkInfo != null && networkInfo.isConnected());
        } catch (Exception e) {
            isConnected = false;
        }
        return isConnected;
    }

    /**
     * 判断连接所用的网络类型
     */
    public static String getNetworkType(Context context) {
        String result = "disconnection";

        if (connManager == null)
            connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (networkInfo == null && connManager != null)
            networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && isNetworkConnected(context)) {

            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI ) {	// 如果是WIFI连接
                result = "wifi";

            } else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {	// 如果是手机网络连接
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                result = getNetGeneration(context, connManager, networkInfo, telephonyManager) + "G";
            }
        }
        return result;
    }

    /**
     * 判断sim卡网络为几代网络
     */
    private static int getNetGeneration(Context context, ConnectivityManager connectivityManager, NetworkInfo networkInfo, TelephonyManager telephonyManager) {

        if (networkInfo != null && networkInfo.isAvailable()) {

            switch (telephonyManager.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:		// Value: 1 | ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:		// Value: 2 | ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:	// Value: 2 | ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_1xRTT:	// Value: 7 | ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_IDEN:		// Value: 11 | ~25 kbps
                    return NET_TYPE_2G;
                case TelephonyManager.NETWORK_TYPE_UMTS:	// Value: 3 | ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:	// Value: 5 | ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:	// Value: 6 | ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:	// Value: 8 | ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:	// Value: 9 | ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:	// Value: 10 | ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B:	// Value: 12 | ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_EHRPD:	// Value: 14 | ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP:	// Value: 15 | ~ 10-20 Mbps
                    return NET_TYPE_3G;
                case TelephonyManager.NETWORK_TYPE_LTE:		// Value: 13 | ~ 10+ Mbps
                    return NET_TYPE_4G;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return NET_TYPE_UNKOWN;
                default:
                    return NET_TYPE_UNKOWN;
            }
        } else {
            return NET_TYPE_NO;
        }
    }
}
