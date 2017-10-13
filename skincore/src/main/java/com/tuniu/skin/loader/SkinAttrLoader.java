package com.tuniu.skin.loader;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 遍历activity去获取需要换肤的view，暂未使用
 */
public class SkinAttrLoader
{
//    private static SkinAttrType getSupportAttrType(String attrName)
//    {
//        for (SkinAttrType attrType : SkinAttrType.values())
//        {
//            if (attrType.getAttrType().equals(attrName))
//                return attrType;
//        }
//        return null;
//    }

    /**
     * 传入activity，找到content元素，递归遍历所有的子View，根据tag命名，记录需要换肤的View
     *
     * @param activity
     */
    public static List<View> getSkinViews(Activity activity)
    {
        List<View> skinViews = new ArrayList<>(10);
        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        addSkinViews(content, skinViews);
        return skinViews;
    }

    public static void addSkinViews(View view, List<View> skinViews)
    {
        View skinView = getSkinView(view);
        if (skinView != null) skinViews.add(skinView);

        if (view instanceof ViewGroup)
        {
            ViewGroup container = (ViewGroup) view;

            for (int i = 0, n = container.getChildCount(); i < n; i++)
            {
                View child = container.getChildAt(i);
                addSkinViews(child, skinViews);
            }
        }
    }

    public static View getSkinView(View view)
    {
//        Object tag = view.getTag(R.id.skin_tag_id);
//        if (tag == null) {
//            tag = view.getTag();
//        }
//        if (tag == null) return null;
//        if (!(tag instanceof String)) return null;
//        String tagStr = (String) tag;
//
////        List<AttrObserver> skinAttrs = parseTag(tagStr);
        return null;
    }

}
