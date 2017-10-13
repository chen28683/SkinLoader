package com.tuniu.skin.skinattr;

import android.content.res.ColorStateList;
import android.graphics.Color;

import com.tuniu.skin.base.AttrObserver;
import com.tuniu.skin.observer.TextColorObserver;
import com.tuniu.skin.plist.NSDictionary;
import com.tuniu.skin.plist.NSObject;
import com.tuniu.skin.plist.NSString;
import com.tuniu.skin.utils.SkinResourcesUtils;

/**
 * Created by chenchen19 on 2017/6/28.
 */
public class TextColorAttr implements SkinBaseAttr {
    public ColorStateList colorStateList;
    public Integer normalColor = null;

    public boolean hasColorList() {
        return colorStateList != null;
    }

    @Override
    public void deSerialize(NSObject content) {
        if (content instanceof NSDictionary) {
            NSDictionary colorList = (NSDictionary) content;
            Integer normal = null;
            Integer press = null;
            Integer disable = null;
            if (colorList.get("textColorNormal") != null) {
                String temp = ((NSString) colorList.get("textColorNormal")).getContent();
                normal = Color.parseColor("#FF" + temp);
                normalColor = normal;
            }
            if (colorList.get("textColorPress") != null) {
                String pressColor = ((NSString) colorList.get("textColorPress")).getContent();
                press = Color.parseColor("#FF" + pressColor);
            }
            if (colorList.get("textColorDisable") != null) {
                String disableColor = ((NSString) colorList.get("textColorDisable")).getContent();
                disable = Color.parseColor("#FF" + disableColor);
            }
            if (normal == null) {
                throw new IllegalArgumentException("textColorNormal can not be null");
            }
            if (press != null && disable != null) {
                colorStateList = SkinResourcesUtils.createColorStateList(normal, press, disable);
            }
        }
    }

    @Override
    public AttrObserver getObserver() {
        return new TextColorObserver();
    }

}
