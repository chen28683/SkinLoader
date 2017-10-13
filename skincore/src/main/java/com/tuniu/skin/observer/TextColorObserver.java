package com.tuniu.skin.observer;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import com.tuniu.skin.SkinManager;
import com.tuniu.skin.base.AttrObserver;
import com.tuniu.skin.base.SkinStyle;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:22:53
 */
public class TextColorObserver extends AttrObserver {
    private ColorStateList colorList;

    @Override
    public void apply(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if (SkinManager.getInstance().isExternalSkin()) {
                SkinStyle style = SkinManager.getInstance().getStyle(styleName);
                if (style != null && style.needTextColor()) {
                    if (style.textColorAttr.hasColorList()) {
                        tv.setTextColor(style.textColorAttr.colorStateList);
                    } else {
                        Integer color = style.textColorAttr.normalColor;
                        if (color != null) {
                            tv.setTextColor(color);
                        }
                    }
                }
            } else {
                tv.setTextColor(colorList);
            }
        }
    }

    @Override
    protected void getOriginValue(View view) {
        if (view instanceof TextView) {
            colorList = ((TextView) view).getTextColors();
        }
    }
}
