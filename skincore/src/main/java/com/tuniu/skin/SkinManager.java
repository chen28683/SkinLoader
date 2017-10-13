package com.tuniu.skin;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;
import com.tuniu.skin.base.ISkinUpdate;
import com.tuniu.skin.base.SkinConfig;
import com.tuniu.skin.base.SkinLoaderListener;
import com.tuniu.skin.base.SkinLoaderResult;
import com.tuniu.skin.base.SkinStyle;
import com.tuniu.skin.loader.SkinZipLoader;
import com.tuniu.skin.loader.TextViewRepository;
import com.tuniu.skin.utils.SkinFileUtils;
import com.tuniu.skin.utils.SkinL;
import com.tuniu.skin.utils.TypefaceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 管理皮肤
 *
 * @author chenchen19
 */
public class SkinManager {
    private static volatile SkinManager mInstance;
    private List<ISkinUpdate> mSkinObservers = new ArrayList<>();
    private boolean isDefaultSkin = true;
    private HashMap<String, SkinStyle> mStyles = new HashMap<>();
    /**
     * skin path
     */
    private String savedSkinPath;

    private SkinManager() {
    }

    public static SkinManager getInstance() {
        if (mInstance == null) {
            synchronized (SkinManager.class) {
                if (mInstance == null) {
                    mInstance = new SkinManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取当前是否使用皮肤
     */
    public void init(Context ctx) {
        isDefaultSkin = SkinConfig.isDefaultSkin(ctx);
        TypefaceUtils.CURRENT_TYPEFACE = TypefaceUtils.getTypeface(ctx);
        if (!isDefaultSkin) {
            loadSkin(SkinConfig.getCustomSkinPath(ctx), new SkinLoaderListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(SkinLoaderResult result) {
                    notifySkinUpdate();
                }

                @Override
                public void onFailed(String errMsg) {

                }
            }, ctx);
        }
    }

    public void setDebug(boolean flag) {
        SkinL.setDebug(flag);
    }

    public boolean isExternalSkin() {
        return !isDefaultSkin;
    }

    public String getCurSkinPath() {
        return savedSkinPath;
    }

    /**
     * 恢复到默认主题
     */
    public void restoreDefaultTheme(Context context) {
        SkinConfig.saveSkinPath(context, SkinConfig.DEFAULT_SKIN);
        isDefaultSkin = true;
        notifySkinUpdate();
    }

    public void notifySkinUpdate() {
        for (ISkinUpdate mSkinObserver : mSkinObservers) {
            mSkinObserver.onSkinUpdate();
        }
    }

    /**
     * 异步加载本地皮肤包
     * <p>
     * eg:theme.skin
     * </p>
     *
     * @param skinPath the name of skin(in assets/skin)
     * @param callback load Callback
     */
    public void loadSkin(String skinPath, final SkinLoaderListener callback, final Context context) {
        new AsyncTask<String, String, SkinLoaderResult>() {

            protected void onPreExecute() {
                if (callback != null) {
                    callback.onStart();
                }
            }

            @Override
            protected SkinLoaderResult doInBackground(String... params) {
                SkinLoaderResult result = new SkinLoaderResult();
                try {
                    if (params.length == 1) {
                        String path = params[0];
                        String skinName = "";
                        if (path.contains("/")) {
                            skinName = path.substring(path.lastIndexOf("/") + 1).replace(".skin", "");
                        } else {
                            skinName = path.replace(".skin", "");
                        }
                        String unzipPath = SkinFileUtils.getSkinDir(context) + File.separator + skinName;
                        //已经解压过
                        if (!unzipPath.equals(SkinConfig.getCustomSkinPath(context))) {
                            if (!path.contains("/")) {
                                SkinZipLoader.unZipSkinPackage(context.getAssets().open(path), SkinFileUtils.getSkinDir(context));
                            } else {
                                SkinZipLoader.unZipSkinPackage(path, SkinFileUtils.getSkinDir(context));
                            }
                        }
                        savedSkinPath = unzipPath;
                        result.styleMap = SkinZipLoader.parsePListFile(unzipPath, File.separator + SkinConfig.PLIST_NAME);
                        result.savePath = unzipPath;
                        return result;
                    }
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(SkinLoaderResult result) {
                if (result != null) {
                    SkinConfig.saveSkinPath(context, result.savePath);
                    mStyles = result.styleMap;
                    isDefaultSkin = false;
                    if (callback != null) {
                        callback.onSuccess(result);
                    }
                } else {
                    if (callback != null) {
                        callback.onFailed("");
                    }
                }
            }
        }.execute(skinPath);
    }

    public void loadFont(String fontName, Context context) {
        Typeface tf = TypefaceUtils.createTypeface(context, fontName);
        TextViewRepository.applyFont(tf);
    }

    public SkinStyle getStyle(String styleName) {
        return mStyles.get(styleName);
    }

    public void register(ISkinUpdate skinBaseActivity) {
        mSkinObservers.add(skinBaseActivity);
    }

    public void unregister(ISkinUpdate skinBaseActivity) {
        mSkinObservers.remove(skinBaseActivity);
    }

    /**
     * load skin form internet
     * <p>
     * eg:https://raw.githubusercontent.com/burgessjp/ThemeSkinning/master/app/src/main/assets/skin/theme.skin
     * </p>
     *
     * @param skinUrl  the url of skin
     * @param callback load Callback
     */
    public void loadSkinFromUrl(String skinUrl, final SkinLoaderListener callback, final Context context) {
        String skinPath = SkinFileUtils.getSkinDir(context);
        final String skinName = skinUrl.substring(skinUrl.lastIndexOf("/") + 1);
        final String skinFullName = skinPath + File.separator + skinName;
        File skinFile = new File(skinFullName);
        if (skinFile.exists()) {
            loadSkin(skinFullName, callback, context);
            return;
        }

        Uri downloadUri = Uri.parse(skinUrl);
        Uri destinationUri = Uri.parse(skinFullName);

        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH);
        callback.onStart();
        downloadRequest.setStatusListener(new DownloadStatusListenerV1() {
            @Override
            public void onDownloadComplete(DownloadRequest downloadRequest) {
                loadSkin(skinFullName, callback, context);
            }

            @Override
            public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                callback.onFailed(errorMessage);
            }

            @Override
            public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
            }
        });
        ThinDownloadManager manager = new ThinDownloadManager();
        manager.add(downloadRequest);


    }
}
