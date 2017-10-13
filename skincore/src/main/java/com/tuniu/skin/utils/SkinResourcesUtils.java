package com.tuniu.skin.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.LruCache;

import com.tuniu.skin.SkinManager;
import com.tuniu.skin.base.SkinConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;

/**
 * Created by _SOLID
 * Date:2016/7/9
 * Time:13:56
 */
public class SkinResourcesUtils {
    private static final int DEFAULT_SIZE = 10;
    private static LruCache<String, Drawable> mCache = new LruCache<>(DEFAULT_SIZE);

    /**
     * 设置Selector。
     */
    public static StateListDrawable newPressSelector(Drawable normal, Drawable pressed) {
        StateListDrawable bg = new StateListDrawable();
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 设置Selector。
     */
    public static StateListDrawable newPressDisableSelector(Drawable normal, Drawable pressed, Drawable disable) {
        StateListDrawable bg = new StateListDrawable();
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{-android.R.attr.state_enabled}, disable);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 对TextView设置不同状态时其文字颜色。
     */
    public static ColorStateList createColorStateList(int normal, int pressed, int disable) {
        int[] colors = new int[]{disable, pressed, normal};
        int[][] states = new int[3][];
        states[0] = new int[]{-android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{};
        return new ColorStateList(states, colors);
    }

    public static Drawable getDrawableFormFile(String skinPath, String fileName) {
        String path = skinPath + SkinConfig.RESOURCE_PATH + fileName;
        Drawable drawable = mCache.get(path);
        if (drawable != null) {
            return drawable;
        } else {
            try {
                drawable = Drawable.createFromStream(new FileInputStream(new File(path)), "");
                if (drawable != null) {
                    mCache.put(path, drawable);
                    SkinL.i("succeed to put cache  " + path);
                }
                return drawable;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 从皮肤包里读取图片
     *
     * @param fileName 文件名
     * @return
     */
    public static Drawable getDrawableFormFile(String fileName) {
        return getDrawableFormFile(SkinManager.getInstance().getCurSkinPath(), fileName);
    }

    public static int getResourceId(Context context, String name, String type) {
        String className = context.getPackageName() + ".R";
        try {
            Class<?> cls = Class.forName(className);
            for (Class<?> childClass : cls.getClasses()) {
                String simple = childClass.getSimpleName();
                if (simple.equals(type)) {
                    for (Field field : childClass.getFields()) {
                        String fieldName = field.getName();
                        if (fieldName.equals(name)) {
                            System.out.println(fieldName);
                            return (int) field.get(null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getStyleableId(Context context, String name) {
        return SkinResourcesUtils.getResourceId(context, name, "styleable");
    }

    public static Integer parseColor(String color) {
        if (TextUtils.isEmpty(color)) {
            return null;
        }
        if (color.length() == 8) {
            return Color.parseColor("#" + color);
        }
        if (color.length() == 6) {
            return Color.parseColor("#FF" + color);
        }
        return null;
    }
}
