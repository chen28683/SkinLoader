package com.tuniu.skin.skinattr;

import android.graphics.drawable.Drawable;

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

public class BackgroundImageAttr implements SkinBaseAttr {

    public Drawable mDrawable;

    @Override
    public void deSerialize(NSObject content) {
        if (content instanceof NSDictionary) {
            NSDictionary drawableList = (NSDictionary) content;
            Drawable normal = null;
            Drawable press = null;
            Drawable disable = null;
            if (drawableList.get("backgroundImageNormal") != null) {
                String normalImg = ((NSString) drawableList.get("backgroundImageNormal")).getContent();
                normal = SkinResourcesUtils.getDrawableFormFile(normalImg);
            }
            if (drawableList.get("backgroundImagePress") != null) {
                String pressImg = ((NSString) drawableList.get("backgroundImagePress")).getContent();
                press = SkinResourcesUtils.getDrawableFormFile(pressImg);
            }
            if (drawableList.get("backgroundImageDisable") != null) {
                String disableImg = ((NSString) drawableList.get("backgroundImageDisable")).getContent();
                disable = SkinResourcesUtils.getDrawableFormFile(disableImg);
            }
            if (normal == null) {
                SkinL.e("error to parse BackgroundColorAttr!");
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
