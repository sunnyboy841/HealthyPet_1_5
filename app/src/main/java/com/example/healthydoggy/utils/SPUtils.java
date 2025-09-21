package com.example.healthydoggy.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
    private static final String SP_NAME = "login_status";
    public static final String KEY_IS_LOGIN = "is_login";
    public static final String KEY_USERNAME = "username";

    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    // 保存登录状态
    public static void saveLoginStatus(Context context, String username) {
        SharedPreferences.Editor editor = getSP(context).edit();
        editor.putBoolean(KEY_IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    // 清除登录状态
    public static void clearLoginStatus(Context context) {
        SharedPreferences.Editor editor = getSP(context).edit();
        editor.clear();
        editor.apply();
    }

    // 获取当前登录用户名
    public static String getUsername(Context context) {
        return getSP(context).getString(KEY_USERNAME, "匿名用户");
    }

    // 判断是否已登录
    public static boolean isLogin(Context context) {
        return getSP(context).getBoolean(KEY_IS_LOGIN, false);
    }
}