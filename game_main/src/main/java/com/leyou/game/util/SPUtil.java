package com.leyou.game.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Description : com.leyou.game.util
 *
 * @author : rocky
 * @Create Time : 2017/4/5 下午2:34
 * @Modified Time : 2017/4/5 下午2:34
 */
public class SPUtil {
    public static final String SETTING = "setting";
    public static final String TREASURE = "treasure";
    public static final String UPDATE_CONTACTS = "contacts";
    public static final String SPLASH = "splash";
    public static final String THREE_ID = "id";
    public static final String INDUCE = "induce_new_person";

    /**
     * 获取保存指定sp文件字段
     *
     * @param context 上下文
     * @param name    sp名称
     * @param keyName 字段
     * @return
     */
    public static String getString(Context context, String name, String keyName) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getString(keyName, "");
    }

    /**
     * 保存指定sp文件字段
     *
     * @param context 上下文
     * @param name    sp名称
     * @param keyName 字段
     * @return
     */
    public static void setString(Context context, String name, String keyName, String value) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(keyName, value);
        edit.apply();
    }

    /**
     * 获取保存指定sp文件字段
     *
     * @param context 上下文
     * @param name    sp名称
     * @param keyName 字段
     * @return
     */
    public static long getLong(Context context, String name, String keyName) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getLong(keyName, 0l);
    }

    /**
     * 保存指定sp文件字段
     *
     * @param context 上下文
     * @param name    sp名称
     * @param keyName 字段
     * @return
     */
    public static void setLong(Context context, String name, String keyName, long value) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putLong(keyName, value);
        edit.apply();
    }

    /**
     * 获取保存指定sp文件字段
     *
     * @param context 上下文
     * @param name    sp名称
     * @param keyName 字段
     * @return
     */
    public static int getInt(Context context, String name, String keyName) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getInt(keyName, 0);
    }

    /**
     * 保存指定sp文件字段
     *
     * @param context 上下文
     * @param name    sp名称
     * @param keyName 字段
     * @return
     */
    public static void setInt(Context context, String name, String keyName, int value) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(keyName, value);
        edit.apply();
    }

    /**
     * 获取保存指定sp文件字段
     *
     * @param context 上下文
     * @param name    sp名称
     * @param keyName 字段
     * @return
     */
    public static boolean getBoolean(Context context, String name, String keyName) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getBoolean(keyName, false);
    }

    /**
     * 保存指定sp文件字段
     *
     * @param context 上下文
     * @param name    sp名称
     * @param keyName 字段
     * @return
     */
    public static void setBoolean(Context context, String name, String keyName, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(keyName, value);
        edit.apply();
    }

    /**
     * 清除所有sp存储
     *
     * @param context
     */
    public static void clearAllSP(Context context) {
        SharedPreferences preferences1 = context.getSharedPreferences(SPLASH, Context.MODE_PRIVATE);
        preferences1.edit().clear().commit();
        SharedPreferences preferences2 = context.getSharedPreferences(UPDATE_CONTACTS, Context.MODE_PRIVATE);
        preferences2.edit().clear().commit();
        SharedPreferences preferences3 = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        preferences3.edit().clear().commit();
        SharedPreferences preferences4 = context.getSharedPreferences(TREASURE, Context.MODE_PRIVATE);
        preferences4.edit().clear().commit();
        SharedPreferences preferences5 = context.getSharedPreferences(THREE_ID, Context.MODE_PRIVATE);
        preferences5.edit().clear().commit();
        SharedPreferences preferences6 = context.getSharedPreferences(INDUCE, Context.MODE_PRIVATE);
        preferences6.edit().clear().commit();
    }

    /**
     * 清除指定sp文件
     *
     * @param context
     * @param name
     */
    public static void clearSP(Context context, String name) {
        SharedPreferences preferences1 = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        preferences1.edit().clear().commit();
    }
}
