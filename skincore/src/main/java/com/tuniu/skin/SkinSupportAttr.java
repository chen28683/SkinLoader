package com.tuniu.skin;

import android.support.annotation.RestrictTo;

import com.tuniu.skin.skinattr.BackgroundColorAttr;
import com.tuniu.skin.skinattr.BackgroundImageAttr;
import com.tuniu.skin.skinattr.SkinBaseAttr;
import com.tuniu.skin.skinattr.TextColorAttr;
import com.tuniu.skin.skinattr.TextSizeAttr;
import com.tuniu.skin.base.SkinStyle;

/**
 * Created by chenchen19 on 2017/6/20.
 * 常量定义
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class SkinSupportAttr {
    public static final String STYLE_TEXT_COLOR = ("textColor");
    public static final String STYLE_FONT_SIZE = ("textFontSize");
    public static final String STYLE_BG_IMAGE = ("backgroundImage");
    public static final String STYLE_BG_COLOR = ("backgroundColor");

    public static SkinBaseAttr getSkinAttr(SkinStyle skinStyle, String name) throws RuntimeException {
        switch (name) {
            case STYLE_TEXT_COLOR:
                skinStyle.textColorAttr = new TextColorAttr();
                return skinStyle.textColorAttr;
            case STYLE_BG_COLOR:
                skinStyle.bgColorAttr = new BackgroundColorAttr();
                return skinStyle.bgColorAttr;
            case STYLE_BG_IMAGE:
                skinStyle.bgImageAttr = new BackgroundImageAttr();
                return skinStyle.bgImageAttr;
            case STYLE_FONT_SIZE:
                skinStyle.textSizeAttr = new TextSizeAttr();
                return skinStyle.textSizeAttr;
            default:
                throw new RuntimeException("no support attr " + name);
        }
    }
}
