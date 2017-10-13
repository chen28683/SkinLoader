目前只有android的代码，ios的代码待开源中
## 使用说明

### 继承
  view的解析我们采用了hook系统的layoutinflate方式，为了提高效率和更好的兼容性，我们提供了两种Acitivity用来继承
  ，如果是FragmentAcitivity的话，请继承SkinBaseActivity，如果需要继承AppCompatActivity的话，请继承SkinBaseCompatActivity。


### 调用代码
``` java
  //初始化，最好在application里进行
  SkinManager.getInstance().init(this);
  //加载皮肤控件
  SkinManager.getInstance().loadSkin(String skinPath, final SkinLoaderListener callback, final Context context);
  //还原默认
  SkinManager.getInstance().restoreDefaultTheme();
```

### 皮肤包结构
皮肤包采用和ios通用的结构。具体目录如下

```
xx.skin
    \--- Default
         \---Resource
                \---xx.png
         \---style.plist
```


其中Default用来做扩展，是必须的。Resource目录下存放图片文件，请尽量防止XXdpi的图片，以免失真
         style.plist是配置文件，大致内容如下

``` xml
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
    <plist version="1.0">
    <dict>
    	<key>skin_home_bg</key>
    	<dict>
    		<key>backgroundImage</key>
    		<string>my_head_bg.png</string>
    	</dict>
    	<key>skin_blackfish_text</key>
    	<dict>
    		<key>textColor</key>
    		<string>333333</string>
    		<key>textFontSize</key>
    		<string>24</string>
    	</dict>
    </dict>
    </plist>

```

skin_home_bg 就是应用中style的名字，目前我们支持的属性(key)有
* backgroundImage
* textColor
* textFontSize
* backgroundColor
* textColorPress
* textColorDisable
* backgroundImagePress

**后续会继续扩展**

### app定义属性
我定义了三种方式，style方式，tag方式，和代码加载方式，都可以实现换肤

#### style 方式
需要换肤的控件，可以修改xml文件，把一些通用的属性放在style文件中(支持style的继承),
例如上面结构的<key>skin_home_bg</key>，代码中可以设置为

``` xml
    <style name="skin_blackfish_text">
        <item name="android:textColor">@color/white</item>
        <item name="android:textSize">@dimen/ts_header_title</item>
    </style>
```

这样在加载皮肤包的时候，就会去查找这个style对应的属性，替换掉。

#### tag方式
因为有些view可能已经使用了style属性，因此我们还提供了tag属性。使用方式很简单，使用"skin:"前缀，后面加上皮肤包里style的名字
``` xml
 <TextView
        android:tag="style:skin_tab_button"/>
````
但是该方式，无法指定哪些属性被替换，只要是皮肤包里对应的style里配置的属性，框架都会尝试去替换

#### 代码添加方式
因为我们是hook layoutInflate的方式，所以只要是layoutinflate创建出来的view，都不需要动态替换。如果是new出来的话，
则需要调用BaseActivity里的方法。

``` java
  //传入需要适配的style的名字

  public void dynamicAddSkinEnableView(View view, String styleName);
```

