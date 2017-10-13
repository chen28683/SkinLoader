package com.tuniu.skin.skinattr;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import com.tuniu.skin.base.AttrObserver;
import com.tuniu.skin.observer.BackgroundObserver;
import com.tuniu.skin.plist.NSDictionary;
import com.tuniu.skin.plist.NSObject;
import com.tuniu.skin.plist.NSString;
import com.tuniu.skin.utils.SkinL;
import com.tuniu.skin.utils.SkinResourcesUtils;

/**
 * Created by chenchen19 on 2017/6/28.
 */
public class BackgroundColorAttr implements SkinBaseAttr {
    public Drawable mDrawable;

    @Override
    public void deSerialize(NSObject content) {
        if (content instanceof NSDictionary) {
            NSDictionary colorList = (NSDictionary) content;
            Drawable normal = null;
            Drawable press = null;
            Drawable disable = null;
            if (colorList.get("backgroundColorNormalStart") != null && colorList.get("backgroundColorNormalEnd") != null) {
                Integer start = SkinResourcesUtils.parseColor(((NSString) colorList.get("backgroundColorNormalStart")).getContent());
                Integer end = SkinResourcesUtils.parseColor(((NSString) colorList.get("backgroundColorNormalEnd")).getContent());
                normal = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{start, end,});
            }
            if (colorList.get("backgroundColorPressStart") != null && colorList.get("backgroundColorPressEnd") != null) {
                Integer start = SkinResourcesUtils.parseColor(((NSString) colorList.get("backgroundColorPressStart")).getContent());
                Integer end = SkinResourcesUtils.parseColor(((NSString) colorList.get("backgroundColorPressEnd")).getContent());
                press = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{start, end,});
            }
            if (colorList.get("backgroundColorNormal") != null) {
                String normalColor = ((NSString) colorList.get("backgroundColorNormal")).getContent();
                normal = new ColorDrawable(Color.parseColor("#FF" + normalColor));
            }
            if (colorList.get("backgroundColorPress") != null) {
                String pressColor = ((NSString) colorList.get("backgroundColorPress")).getContent();
                press = new ColorDrawable(Color.parseColor("#FF" + pressColor));
            }
            if (colorList.get("backgroundColorDisable") != null) {
                String disableColor = ((NSString) colorList.get("backgroundColorDisable")).getContent();
                disable = new ColorDrawable(Color.parseColor("#FF" + disableColor));
            }
            if (normal == null) {
                SkinL.e("error to parse BackgroundColorAttr!");
                return;
            } else {
                mDrawable = normal;
            }
            if (press != null && disable == null) {
                mDrawable = SkinResourcesUtils.newPressSelector(normal, press);
            }
            if (press != null && disable != null) {
                mDrawable = SkinResourcesUtils.newPressDisableSelector(normal, press, disable);
            }
        }
    }

    @Override
    public AttrObserver getObserver() {
        return new BackgroundObserver();
    }

}
