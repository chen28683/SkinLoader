package com.tuniu.skin.utils;

import android.util.Log;

/**
 * Created by _SOLID
 * Date:2016/12/14
 * Time:10:24
 */
public class SkinL {

    private static final String TAG = "SkinL";
    private static boolean DEBUG;

    public static void setDebug(boolean flag) {
        DEBUG = flag;
    }

    public static void i(String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void trace() {
        new Exception(TAG).printStackTrace();
    }
}
