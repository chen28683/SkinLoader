package com.tuniu.skin.skinattr;

import com.tuniu.skin.base.AttrObserver;
import com.tuniu.skin.observer.TextSizeObserver;
import com.tuniu.skin.plist.NSObject;
import com.tuniu.skin.plist.NSString;

/**
 * Created by chenchen19 on 2017/6/28.
 */
public class TextSizeAttr implements SkinBaseAttr {
    public float size = 0;

    @Override
    public void deSerialize(NSObject content) {
        if (content instanceof NSString) {
            size = ((NSString) content).floatValue();
        }
    }

    @Override
    public AttrObserver getObserver() {
        return new TextSizeObserver();
    }
}
