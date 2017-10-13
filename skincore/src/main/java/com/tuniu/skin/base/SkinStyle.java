package com.tuniu.skin.base;

import com.tuniu.skin.skinattr.BackgroundColorAttr;
import com.tuniu.skin.skinattr.BackgroundImageAttr;
import com.tuniu.skin.skinattr.TextColorAttr;
import com.tuniu.skin.skinattr.TextSizeAttr;

/**
 * Created by chenchen19 on 2017/6/20.
 */
public class SkinStyle {
    public String styleName;
    public TextColorAttr textColorAttr;
    public TextSizeAttr textSizeAttr;
    public BackgroundColorAttr bgColorAttr;
    public BackgroundImageAttr bgImageAttr;

    public boolean needTextColor() {
        return textColorAttr != null;
    }

    public boolean needTextSize() {
        return textSizeAttr != null;
    }

    public boolean needBgColor() {
        return bgColorAttr != null;
    }

    public boolean needBgImage() {
        return bgImageAttr != null;
    }


}
