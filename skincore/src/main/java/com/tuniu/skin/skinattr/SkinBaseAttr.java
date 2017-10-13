package com.tuniu.skin.skinattr;

import com.tuniu.skin.base.AttrObserver;
import com.tuniu.skin.plist.NSObject;

/**
 * Created by chenchen19 on 2017/6/28.
 */

public interface SkinBaseAttr {

    void deSerialize(NSObject content);

    AttrObserver getObserver();
}

