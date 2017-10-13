package com.tuniu.skin.loader;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tuniu.skin.R;
import com.tuniu.skin.SkinManager;
import com.tuniu.skin.base.AttrObserver;
import com.tuniu.skin.base.SkinConfig;
import com.tuniu.skin.base.SkinItem;
import com.tuniu.skin.base.SkinStyle;
import com.tuniu.skin.observer.BackgroundObserver;
import com.tuniu.skin.observer.TextColorObserver;
import com.tuniu.skin.observer.TextSizeObserver;
import com.tuniu.skin.utils.SkinL;
import com.tuniu.skin.utils.SkinResourcesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SkinInflaterFactoryProxy implements LayoutInflaterFactory {
    private AppCompatActivity mAppCompatActivity;
    /**
     * 存储那些有皮肤更改需求的View及其对应的属性的集合
     */
    private List<SkinItem> mSkinItemList = new ArrayList<>();
    private int textSizeId = -1;
    private int textColorId = -1;
    private int backgroundId = -1;

    public SkinInflaterFactoryProxy(AppCompatActivity appCompatActivity) {
        mAppCompatActivity = appCompatActivity;
    }

    public SkinInflaterFactoryProxy() {

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = null;
        if (mAppCompatActivity != null) {
            //表示是compatactivity进入的
            view = compatCreateView(parent, name, context, attrs);
        } else {
            view = normalCreateView(name, context, attrs, view);
        }
        if (view == null) {
            SkinL.e("error to inflate view!");
            return null;
        }
        if (view instanceof TextView && SkinConfig.isCanChangeFont()) {
            TextViewRepository.add((TextView) view);
        }
        if (view.getTag() != null) {
            String tag = view.getTag().toString();
            if (tag.startsWith("style:")) {
                parseTag(view, tag.substring(tag.indexOf(":") + 1));
            } else {
                parseStyle(context, attrs, view);
            }
        } else {
            parseStyle(context, attrs, view);
        }
        return view;
    }

    @Nullable
    private View normalCreateView(String name, Context context, AttributeSet attrs, View view) {
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            if (-1 == name.indexOf('.')) {
                if ("View".equals(name)) {
                    view = inflater.createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = inflater.createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = inflater.createView(name, "android.webkit.", attrs);
                }
            } else {
                view = LayoutInflater.from(context).createView(name, null, attrs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            view = null;
        }
        return view;
    }

    @Nullable
    private View compatCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view;
        AppCompatDelegate delegate = mAppCompatActivity.getDelegate();
        view = delegate.createView(parent, name, context, attrs);
        if (view == null) {
            return null;
        }
        return view;
    }

    private void parseTag(View view, String styleName) {
        SkinStyle style = SkinManager.getInstance().getStyle(styleName);
        if (style == null) {
            //还没有加载皮肤的时候，也要把view加进刷新列表里
            if (!SkinManager.getInstance().isExternalSkin()) {
                SkinItem skinItem = new SkinItem(view);
                skinItem.styleName = styleName;
                skinItem.needLoadStyle = true;
                mSkinItemList.add(skinItem);
            } else {
                SkinL.e("没有找到tag对应的style -》" + styleName);
            }
        } else {
            SkinItem skinItem = new SkinItem(view);
            List<AttrObserver> viewAttrs = skinItem.getAttrs(styleName, view);
            if (!viewAttrs.isEmpty()) {
                skinItem.attrs = viewAttrs;
                mSkinItemList.add(skinItem);
                if (SkinManager.getInstance().isExternalSkin()) {//如果当前皮肤来自于外部
                    skinItem.update();
                }
            }
        }
    }

    /**
     * collect skin view
     */
    private void parseStyle(Context context, AttributeSet attrs, View view) {
        List<AttrObserver> viewAttrs = new ArrayList<>();//存储View可更换皮肤属性的集合
        for (int i = 0; i < attrs.getAttributeCount(); i++) {//遍历当前View的属性
            String attrName = attrs.getAttributeName(i);//属性名
            String attrValue = attrs.getAttributeValue(i);//属性值
            //region  style
            if ("style".equals(attrName) && attrValue.contains("skin_")) {//style theme
                String styleName = attrValue.substring(attrValue.indexOf("/") + 1);
                int styleID = context.getResources().getIdentifier(styleName, "style", context.getPackageName());
                int textSizeId = getTextSizeId(context);
                int textColorId = getTextColorId(context);
                int backgroundId = getBackgroundId(context);
                TypedArray ta = context.obtainStyledAttributes(styleID, R.styleable.skinSupport);
                final int N = ta.getIndexCount();
                SkinL.i(String.format("style[%s] has [%s] attrs ", styleName, N));
                for (int index = 0; index < N; index++) {
                    int attr = ta.getIndex(index);
                    if (attr == textColorId) {
                        createTextColor(view, viewAttrs, styleName);
                        SkinL.i(String.format("style[%s] has [%s] attrs ", styleName, "TextView_textColor"));
                    } else if (attr == backgroundId) {
                        createBackground(view, viewAttrs, styleName);
                        SkinL.i(String.format("style[%s] has [%s] attr ", styleName, "View_background"));
                    } else if (attr == textSizeId) {
                        createTextSize(view, viewAttrs, styleName);
                        SkinL.i(String.format("style[%s] has [%s] attrs ", styleName, "TextView_textSize"));
                    }
                }
                ta.recycle();
                break;
            }
        }
        if (!viewAttrs.isEmpty()) {
            SkinItem skinItem = new SkinItem(view);
            skinItem.attrs = viewAttrs;
            mSkinItemList.add(skinItem);
            if (SkinManager.getInstance().isExternalSkin()) {//如果当前皮肤来自于外部
                skinItem.update();
            }
        }
    }

    private void createTextSize(View view, List<AttrObserver> viewAttrs, String styleName) {
        AttrObserver changeableAttr = new TextSizeObserver();
        changeableAttr.styleName = styleName;
        changeableAttr.origin(view);
        viewAttrs.add(changeableAttr);
    }

    private void createBackground(View view, List<AttrObserver> viewAttrs, String styleName) {
        AttrObserver changeableAttr = new BackgroundObserver();
        changeableAttr.styleName = styleName;
        changeableAttr.origin(view);
        viewAttrs.add(changeableAttr);
    }

    private void createTextColor(View view, List<AttrObserver> viewAttrs, String styleName) {
        AttrObserver changeableAttr = new TextColorObserver();
        changeableAttr.styleName = styleName;
        changeableAttr.origin(view);
        viewAttrs.add(changeableAttr);
    }

    public void applySkin() {
        if (mSkinItemList.isEmpty()) {
            return;
        }
        for (SkinItem skinItem : mSkinItemList) {
            if (skinItem.getView() == null) {
                continue;
            }
            skinItem.update();
        }
    }

    /**
     * 清除有皮肤更改需求的View及其对应的属性的集合
     */
    public void clean() {
        if (mSkinItemList.isEmpty()) {
            return;
        }
        for (SkinItem skinItem : mSkinItemList) {
            skinItem.clean();
        }
    }

    private void addSkinView(SkinItem item) {
        int index = mSkinItemList.indexOf(item);
        if (index != -1) {
            mSkinItemList.get(index).attrs.addAll(item.attrs);
        } else {
            mSkinItemList.add(item);
        }
    }

    public void dynamicAddSkinEnableView(View view, String styleName) {
        SkinStyle style = SkinManager.getInstance().getStyle(styleName);
        List<AttrObserver> viewAttrs = createViewOberver(styleName, view, style);
        SkinItem skinItem = new SkinItem(view);
        skinItem.attrs = viewAttrs;
        addSkinView(skinItem);
        if (SkinManager.getInstance().isExternalSkin()) {
            skinItem.update();
        }
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

    private int getTextSizeId(Context ctx) {
        if (textSizeId == -1) {
            textSizeId = SkinResourcesUtils.getStyleableId(ctx, "skinSupport_android_textSize");
        }
        return textSizeId;
    }

    private int getTextColorId(Context ctx) {
        if (textColorId == -1) {
            textColorId = SkinResourcesUtils.getStyleableId(ctx, "skinSupport_android_textColor");
        }
        return textColorId;
    }

    private int getBackgroundId(Context ctx) {
        if (backgroundId == -1) {
            backgroundId = SkinResourcesUtils.getStyleableId(ctx, "skinSupport_android_background");
        }
        return backgroundId;
    }
}
