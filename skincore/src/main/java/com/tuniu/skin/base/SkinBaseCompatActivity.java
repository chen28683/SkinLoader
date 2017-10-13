package com.tuniu.skin.base;

import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tuniu.skin.SkinManager;
import com.tuniu.skin.loader.SkinInflaterFactoryProxy;


/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:10:24
 * Your activity need extend this
 */
public class SkinBaseCompatActivity extends AppCompatActivity implements ISkinUpdate {

    private final static String TAG = "SkinBaseActivity";
    private SkinInflaterFactoryProxy mSkinInflaterFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSkinInflaterFactory = new SkinInflaterFactoryProxy(this);
        LayoutInflaterCompat.setFactory(getLayoutInflater(), mSkinInflaterFactory);
        super.onCreate(savedInstanceState);
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


    public SkinInflaterFactoryProxy getInflaterFactory() {
        return mSkinInflaterFactory;
    }


    public final void removeSkinView(View v) {
//        mSkinInflaterFactory.removeSkinView(v);
    }

    @Override
    public void onSkinUpdate() {
        mSkinInflaterFactory.applySkin();
    }
}
