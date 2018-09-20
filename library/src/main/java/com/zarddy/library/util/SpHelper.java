package com.zarddy.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Map;

public class SpHelper {

    //默认配置文件
    public static String DEFAULT_SP_NAME = "default_sp";

    /**
     * 将键值保存到指定的配置文件中
     * @param context
     * @param spName
     * @param key
     * @param defaultValue
     * @return
     */
    public static final String getSpValue(Context context,String spName,String key,String defaultValue){
        return getSP(context, spName).getString(key, defaultValue);
    }

    public static final boolean getSpValue(Context context,String spName,String key,boolean defaultValue){
        return getSP(context, spName).getBoolean(key, defaultValue);
    }

    public static final int getSpValue(Context context,String spName,String key,int defaultValue){
        return getSP(context, spName).getInt(key, defaultValue);
    }

    public static final long getSpValue(Context context,String spName,String key,long defaultValue){
        return getSP(context, spName).getLong(key, defaultValue);
    }

    public static final float getSpValue(Context context,String spName,String key,float defaultValue){
        return getSP(context, spName).getFloat(key, defaultValue);
    }

    public static final String getSpValue(Context context, String key,String defaultValue) {
        return getSpValue(context, DEFAULT_SP_NAME, key, defaultValue);
    }

    public static final int getSpValue(Context context, String key,int defaultValue){
        return getSpValue(context, DEFAULT_SP_NAME, key, defaultValue);
    }

    public static final boolean getSpValue(Context context,String key,boolean defaultValue){
        return getSpValue(context, DEFAULT_SP_NAME, key, defaultValue);
    }

    public static final float getSpValue(Context context,String key,float defaultValue){
        return getSpValue(context, DEFAULT_SP_NAME, key, defaultValue);
    }
    //======================================================
    public static final void putSpValue(Context context,String spName,String key,String value){
        value = TextUtils.isEmpty(value) ? "" : value;
        getSP(context, spName).edit().putString(key, value).commit();
    }

    public static final void putSpValue(Context context,String spName,String key,boolean value){
        getSP(context, spName).edit().putBoolean(key, value).commit();
    }

    public static final void putSpValue(Context context,String spName,String key,int value){
        getSP(context, spName).edit().putInt(key, value).commit();
    }

    public static final void putSpValue(Context context,String spName,String key,long value){
        getSP(context, spName).edit().putLong(key, value).commit();
    }

    public static final void putSpValue(Context context,String spName,String key,float value){
        getSP(context, spName).edit().putFloat(key, value).commit();
    }

    public static final void putSpValue(Context context,String key,String value){
        putSpValue(context, DEFAULT_SP_NAME, key, value);
    }

    public static final void putSpValue(Context context,String key,long value){
        putSpValue(context, DEFAULT_SP_NAME, key, value);
    }

    public static final void putSpValue(Context context,String key,int value){
        putSpValue(context, DEFAULT_SP_NAME, key, value);
    }

    public static final void putSpValue(Context context,String key,boolean value){
        putSpValue(context, DEFAULT_SP_NAME, key, value);
    }

    public static final void putSpValue(Context context,String key,float value){
        putSpValue(context, DEFAULT_SP_NAME, key, value);
    }


    private static final SharedPreferences getSP(Context context, String sp_name) {
        return context.getApplicationContext().getSharedPreferences(sp_name, Context.MODE_PRIVATE);
    }

    /**
     * 获取该SP文件所有的键值
     * @param context
     * @param spName
     * @return
     */
    public static final Map<String,Object> getAllSpValue(Context context,String spName){
        return (Map<String, Object>) getSP(context, spName).getAll();
    }

    /**
     * 清空SP数据
     * @param context
     * @param spName
     */
    public static final void clearSP(Context context, String spName){
        if(spName == null){
            getSP(context, DEFAULT_SP_NAME).edit().clear().commit();
        } else {
            getSP(context, spName).edit().clear().commit();
        }
    }
}
