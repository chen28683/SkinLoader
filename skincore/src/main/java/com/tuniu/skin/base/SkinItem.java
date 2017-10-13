package com.tuniu.skin.base;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.tuniu.skin.SkinManager;
import com.tuniu.skin.utils.SkinL;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:9:21
 * Desc:store view and attribute collection
 */
public class SkinItem {

    public List<AttrObserver> attrs;

    public String styleName;

    public boolean needLoadStyle;
    /**
     * 设置为弱引用，防止内存泄露
     */
    private WeakReference<View> view;


    public SkinItem(View view) {
        attrs = new ArrayList<>();
        this.view = new WeakReference<>(view);
    }

    public View getView() {
        return view == null ? null : view.get();
    }

    public void update() {
        if (needLoadStyle && !TextUtils.isEmpty(styleName)) {
            getAttrs(styleName, view.get());
            if (attrs == null || attrs.isEmpty()) {
                SkinL.e("can not load style -->" + styleName);
                return;
            } else {
                needLoadStyle = false;
            }
        }
        if (attrs == null || attrs.isEmpty()) {
            return;
        }
        for (AttrObserver at : attrs) {
            View v = view.get();
            if (v != null) {
                at.apply(v);
            }
        }
    }

    public void clean() {
        if (attrs != null) {
            attrs.clear();
        }
    }

    @Override
    public String toString() {
        return "SkinItem [view=" + view.get().getClass().getSimpleName() + ", attrs=" + attrs + "]";
    }

    public List<AttrObserver> getAttrs(String styleName, View v) {
        SkinStyle style = SkinManager.getInstance().getStyle(styleName);
        if (style == null) {
            return null;
        }
        List<AttrObserver> viewAttrs = createViewOberver(styleName, v, style);
        if (!viewAttrs.isEmpty()) {
            attrs = viewAttrs;
        }
        return viewAttrs;
    }

    @NonNull
    private List<AttrObserver> createViewOberver(String styleName, View v, SkinStyle style) {
        List<AttrObserver> viewAttrs = new ArrayList<>();
        if (style.bgImageAttr != null) {
            AttrObserver changeSupportAttr = style.bgImageAttr.getObserver();
            changeSupportAttr.styleName = styleName;
            changeSupportAttr.origin(v);
            viewAttrs.add(changeSupportAttr);
        }
        if (style.textSizeAttr != null) {
            AttrObserver changeSupportAttr = style.textSizeAttr.getObserver();
            changeSupportAttr.styleName = styleName;
            changeSupportAttr.origin(v);
            viewAttrs.add(changeSupportAttr);
        }
        if (style.textColorAttr != null) {
            AttrObserver changeSupportAttr = style.textColorAttr.getObserver();
            changeSupportAttr.styleName = styleName;
            changeSupportAttr.origin(v);
            viewAttrs.add(changeSupportAttr);
        }
        if (style.bgColorAttr != null) {
            AttrObserver changeSupportAttr = style.bgColorAttr.getObserver();
            changeSupportAttr.styleName = styleName;
            changeSupportAttr.origin(v);
            viewAttrs.add(changeSupportAttr);
        }
        return viewAttrs;
    }

}
