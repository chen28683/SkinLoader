package com.tuniu.skin.base;

import android.view.View;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:21:38
 */
public abstract class AttrObserver {

    public String styleName;

    private boolean getOrigin = false;

    /**
     * Use to apply view with new TypedValue
     *
     * @param view
     */
    public abstract void apply(View view);

    protected abstract void getOriginValue(View view);

    public void origin(View view) {
        if (!getOrigin) {
            getOriginValue(view);
            getOrigin = true;
        }
    }

    @Override
    public String toString() {
        return "AttrObserver{" +
                "styleName='" + styleName + '\'' +
                '}';
    }
}
