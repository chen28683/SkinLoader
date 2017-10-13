package com.tuniu.skin.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.tuniu.skin.SkinManager;
import com.tuniu.skin.loader.SkinInflaterFactoryProxy;


/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:10:24
 * Your activity need extend this
 */
public class SkinBaseActivity extends FragmentActivity implements ISkinUpdate {

    private SkinInflaterFactoryProxy mSkinInflaterFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSkinInflaterFactory = new SkinInflaterFactoryProxy();
        LayoutInflaterCompat.setFactory(getLayoutInflater(), mSkinInflaterFactory);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SkinManager.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
        mSkinInflaterFactory.clean();
    }



    public final void removeSkinView(View v) {
//        mSkinInflaterFactory.removeSkinView(v);
    }

    @Override
    public void onSkinUpdate() {
        mSkinInflaterFactory.applySkin();
    }

    public void dynamicAddSkinEnableView(View view, String styleName) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(view, styleName);
    }
}
