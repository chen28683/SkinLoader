package com.tuniu.skinloader;

import android.os.Bundle;
import android.view.View;

import com.tuniu.skin.SkinManager;
import com.tuniu.skin.base.SkinBaseCompatActivity;
import com.tuniu.skin.base.SkinLoaderListener;
import com.tuniu.skin.base.SkinLoaderResult;
import com.tuniu.skin.utils.SkinL;

public class MainActivity extends SkinBaseCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SkinManager.getInstance().setDebug(true);
        SkinManager.getInstance().init(this);
        findViewById(R.id.switchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManager.getInstance().loadSkin("Default.skin", new SkinLoaderListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(SkinLoaderResult result) {
                        SkinManager.getInstance().notifySkinUpdate();
                        SkinL.i("load skin succeed!");
                    }

                    @Override
                    public void onFailed(String errMsg) {

                    }

                }, MainActivity.this);
            }
        });
        findViewById(R.id.switchDefault).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManager.getInstance().restoreDefaultTheme(MainActivity.this);
            }
        });
    }
}
