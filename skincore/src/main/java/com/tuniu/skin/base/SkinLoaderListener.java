package com.tuniu.skin.base;

/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:21:07
 * Desc:
 */
public interface SkinLoaderListener {
    void onStart();

    void onSuccess(SkinLoaderResult result);

    void onFailed(String errMsg);

}
