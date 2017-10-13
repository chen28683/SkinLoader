package com.tuniu.skin.observer;

import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.tuniu.skin.SkinManager;
import com.tuniu.skin.base.AttrObserver;
import com.tuniu.skin.base.SkinStyle;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:21:46
 */
public class BackgroundObserver extends AttrObserver {
    public Drawable drawable;

    @Override
    public void apply(View view) {
        if (SkinManager.getInstance().isExternalSkin()) {
            SkinStyle style = SkinManager.getInstance().getStyle(styleName);
            if (style != null && (style.needBgColor() || style.needBgImage())) {
                Drawable drawable = null;
                if (style.bgImageAttr != null) {
                    drawable = style.bgImageAttr.mDrawable;
                } else if (style.bgColorAttr != null) {
                    drawable = style.bgColorAttr.mDrawable;
                }
                if (drawable != null) {
                    ViewCompat.setBackground(view, drawable);
                }
            }

        } else {
            ViewCompat.setBackground(view, drawable);
        }
    }

    @Override
    protected void getOriginValue(View view) {
        drawable = view.getBackground();
    }
}
