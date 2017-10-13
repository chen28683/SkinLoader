package com.tuniu.skin.loader;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.tuniu.skin.SkinSupportAttr;
import com.tuniu.skin.base.SkinStyle;
import com.tuniu.skin.plist.NSDictionary;
import com.tuniu.skin.plist.PropertyListParser;
import com.tuniu.skin.skinattr.SkinBaseAttr;
import com.tuniu.skin.utils.SkinL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by chenchen19 on 2017/6/20.
 * 解析资源zip包
 */
public class SkinZipLoader {

    /**
     * 该方法涉及文件操作，建议不要放到UI线程中
     */
    @WorkerThread
    public static HashMap<String, SkinStyle> parsePListFile(String skinPath, String filePath) {
        HashMap<String, SkinStyle> styleList = new HashMap<>();
        try {
            InputStream is = new FileInputStream(skinPath + filePath);
            NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(is);
            if (rootDict == null) {
                throw new IllegalArgumentException("没有找到皮肤包");
            }
            for (String key : rootDict.allKeys()) {
                SkinStyle style = new SkinStyle();
                style.styleName = key;
                NSDictionary attrs = (NSDictionary) rootDict.get(key);
                for (String aKey : attrs.allKeys()) {
                    SkinBaseAttr attr = null;
                    try {
                        attr = SkinSupportAttr.getSkinAttr(style, aKey);
                    } catch (RuntimeException e) {
                        continue;
                    }
                    if (attr != null) {
                        try {
                            attr.deSerialize(attrs.get(aKey));
                        } catch (Exception e) {
                            SkinL.e("deSerialize attr fail! !  ->" + aKey);
                        }
                    } else {
                        SkinL.e("don't support attr !  ->" + aKey);
                    }
                }
                styleList.put(style.styleName, style);
            }
            //Continue parsing...
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return styleList;
    }


    public static void unZipSkinPackage(String zipFileString, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        unzipSkinPackage(outPathString, inZip);
    }

    public static void unZipSkinPackage(InputStream in, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(in);
        unzipSkinPackage(outPathString, inZip);
    }

    private static void unzipSkinPackage(String outPathString, ZipInputStream inZip) throws IOException {
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                String path = outPathString + File.separator + szName;
                File file = saveFile(path);
                // get the output stream of the file
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    @NonNull
    private static File saveFile(String path) throws IOException {
        File folder = new File(path.substring(0, path.lastIndexOf(File.separatorChar)));
        if (!folder.exists()) {
            boolean succeed = folder.mkdirs();
            System.out.println(succeed);
        }
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        SkinL.i("save file[" + path + "] result ->" + file.createNewFile());
        return file;
    }

}
