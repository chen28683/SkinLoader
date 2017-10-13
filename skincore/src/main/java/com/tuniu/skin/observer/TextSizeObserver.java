package com.tuniu.skin.observer;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.tuniu.skin.SkinManager;
import com.tuniu.skin.base.AttrObserver;
import com.tuniu.skin.base.SkinStyle;

/**
 * Created by chenchen19 on 2017/6/22.
 * 修改textview文字大小的示例
 */
public class TextSizeObserver extends AttrObserver {
    public float textSize;

    @Override
    public void apply(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if (SkinManager.getInstance().isExternalSkin()) {
                SkinStyle style = SkinManager.getInstance().getStyle(styleName);
                if (style != null && style.needTextSize()) {
//                    float fontScale = view.getResources().getDisplayMetrics().scaledDensity;
//                    float defaultTextSize = (textSize / fontScale + 0.5f);
                    if (style.textSizeAttr.size > 0) {
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textSizeAttr.size);
                    }
                }
            } else {
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
        }
    }

    @Override
    protected void getOriginValue(View view) {
        if (view instanceof TextView) {
            textSize = ((TextView) view).getTextSize();
        }
    }
}
