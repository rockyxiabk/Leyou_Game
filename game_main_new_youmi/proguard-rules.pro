# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/rocky/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-dontoptimize         #优化  不优化输入的类文件
-verbose           #混淆时是否记录日志
-ignorewarning    #忽略警告
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/* # 混淆时所采用的算法
# support-v7-appcompat
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}
# support-design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# Gson 混淆
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.leyou.game.bean.** { *; }
-keep class com.leyou.game.dao.** { *; }

# greenDao混淆
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static Java.lang.String TABLENAME;
}
-keep class **$Properties

# retrofit2 混淆
-keep public class retrofit2.** { *; }
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# rx.android 混淆
-keep public class rx.android.** { *; }
-dontwarn rx.android.**
-keep class rx.android.** { *; }

# rxjava 混淆
-keep public class rx.** { *; }
-dontwarn rx.android.**
-keep class rx.** { *; }

#okhttp 混淆
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}
-dontwarn okio.**
-keep class okio.** { *;}

# Butterknife 混淆
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#Fresco
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
-keepclassmembers class * {
    native <methods>;
}
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**

#EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

# nineoldandroids
-keep interface com.nineoldandroids.view.** { *; }
-dontwarn com.nineoldandroids.**
-keep class com.nineoldandroids.** { *; }

#getui
-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }

#youmi
-dontwarn ddd.eee.**
-keep class ddd.eee.** { *; }
-keep class ddd.eee.** { *; }

#umeng
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.leyou.game.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#alipay
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

#h5页面混淆
-keepattributes *JavascriptInterface*
-keepclassmembers class com.leyou.game.activity.PlayGameActivity$JsInterfaceFun{
     public <fields>;
     public <methods>;
     public *;
 }
 -keepclassmembers class com.leyou.game.activity.WebViewGuideActivity$JsInterfaceFun{
     public <fields>;
     public <methods>;
     public *;
 }

#UM share
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}

-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep class com.kakao.** {*;}
-dontwarn com.kakao.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.umeng.socialize.impl.ImageImpl {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#agora
-keep class io.agora.**{*;}

#android-gif-drawable
-keep class pl.droidsonroids.gif.**{*;}

# RongCloud SDK
-keep class io.rong.** {*;}
-keep class * implements io.rong.imlib.model.MessageContent {*;}
-dontwarn io.rong.push.**
-dontnote com.xiaomi.**
-dontnote com.google.android.gms.gcm.**
-dontnote io.rong.**
-keep class com.leyou.game.receiver.RongNotificationReceiver {*;}

#AndPermission
 -keepclassmembers class ** {
     @com.yanzhenjie.permission.PermissionYes <methods>;
 }
 -keepclassmembers class ** {
     @com.yanzhenjie.permission.PermissionNo <methods>;
 }

 #tencent X5
 #-optimizationpasses 7
 #-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
 -dontoptimize
 -dontusemixedcaseclassnames
 -verbose
 -dontskipnonpubliclibraryclasses
 -dontskipnonpubliclibraryclassmembers
 -dontwarn dalvik.**
 -dontwarn com.tencent.smtt.**
 #-overloadaggressively

# #@proguard_debug_start
# # ------------------ Keep LineNumbers and properties ---------------- #
# -keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
# -renamesourcefileattribute TbsSdkJava
# -keepattributes SourceFile,LineNumberTable
# #@proguard_debug_end

 # --------------------------------------------------------------------------
 # Addidional for x5.sdk classes for apps

 -keep class com.tencent.smtt.export.external.**{
     *;
 }

 -keep class com.tencent.tbs.video.interfaces.IUserStateChangedListener {
 	*;
 }

 -keep class com.tencent.smtt.sdk.CacheManager {
 	public *;
 }

 -keep class com.tencent.smtt.sdk.CookieManager {
 	public *;
 }

 -keep class com.tencent.smtt.sdk.WebHistoryItem {
 	public *;
 }

 -keep class com.tencent.smtt.sdk.WebViewDatabase {
 	public *;
 }

 -keep class com.tencent.smtt.sdk.WebBackForwardList {
 	public *;
 }

 -keep public class com.tencent.smtt.sdk.WebView {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebView$HitTestResult {
 	public static final <fields>;
 	public java.lang.String getExtra();
 	public int getType();
 }

 -keep public class com.tencent.smtt.sdk.WebView$WebViewTransport {
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebView$PictureListener {
 	public <fields>;
 	public <methods>;
 }


 -keepattributes InnerClasses

 -keep public enum com.tencent.smtt.sdk.WebSettings$** {
     *;
 }

 -keep public enum com.tencent.smtt.sdk.QbSdk$** {
     *;
 }

 -keep public class com.tencent.smtt.sdk.WebSettings {
     public *;
 }


 -keepattributes Signature
 -keep public class com.tencent.smtt.sdk.ValueCallback {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebViewClient {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.DownloadListener {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebChromeClient {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebChromeClient$FileChooserParams {
 	public <fields>;
 	public <methods>;
 }

 -keep class com.tencent.smtt.sdk.SystemWebChromeClient{
 	public *;
 }
 # 1. extension interfaces should be apparent
 -keep public class com.tencent.smtt.export.external.extension.interfaces.* {
 	public protected *;
 }

 # 2. interfaces should be apparent
 -keep public class com.tencent.smtt.export.external.interfaces.* {
 	public protected *;
 }

 -keep public class com.tencent.smtt.sdk.WebViewCallbackClient {
 	public protected *;
 }

 -keep public class com.tencent.smtt.sdk.WebStorage$QuotaUpdater {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebIconDatabase {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebStorage {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.DownloadListener {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.QbSdk {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.QbSdk$PreInitCallback {
 	public <fields>;
 	public <methods>;
 }
 -keep public class com.tencent.smtt.sdk.CookieSyncManager {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.Tbs* {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.utils.LogFileUtils {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.utils.TbsLog {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.utils.TbsLogClient {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.CookieSyncManager {
 	public <fields>;
 	public <methods>;
 }

 # Added for game demos
 -keep public class com.tencent.smtt.sdk.TBSGamePlayer {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.TBSGamePlayerClient* {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.TBSGamePlayerClientExtension {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.TBSGamePlayerService* {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.utils.Apn {
 	public <fields>;
 	public <methods>;
 }
 -keep class com.tencent.smtt.** {
 	*;
 }
 # end


 -keep public class com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension {
 	public <fields>;
 	public <methods>;
 }

 -keep class MTT.ThirdAppInfoNew {
 	*;
 }

 -keep class com.tencent.mtt.MttTraceEvent {
 	*;
 }

 # Game related
 -keep public class com.tencent.smtt.gamesdk.* {
 	public protected *;
 }

 -keep public class com.tencent.smtt.sdk.TBSGameBooter {
         public <fields>;
         public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.TBSGameBaseActivity {
 	public protected *;
 }

 -keep public class com.tencent.smtt.sdk.TBSGameBaseActivityProxy {
 	public protected *;
 }

 -keep public class com.tencent.smtt.gamesdk.internal.TBSGameServiceClient {
 	public *;
 }
 #---------------------------------------------------------------------------


 #------------------  下方是android平台自带的排除项，这里不要动         ----------------

 -keep public class * extends android.app.Activity{
 	public <fields>;
 	public <methods>;
 }
 -keep public class * extends android.app.Application{
 	public <fields>;
 	public <methods>;
 }
 -keep public class * extends android.app.Service
 -keep public class * extends android.content.BroadcastReceiver
 -keep public class * extends android.content.ContentProvider
 -keep public class * extends android.app.backup.BackupAgentHelper
 -keep public class * extends android.preference.Preference

 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 -keepclasseswithmembers class * {
 	public <init>(android.content.Context, android.util.AttributeSet);
 }

 -keepclasseswithmembers class * {
 	public <init>(android.content.Context, android.util.AttributeSet, int);
 }

 -keepattributes *Annotation*

 -keepclasseswithmembernames class *{
 	native <methods>;
 }

 -keep class * implements android.os.Parcelable {
   public static final android.os.Parcelable$Creator *;
 }

 #------------------  下方是共性的排除项目         ----------------
 # 方法名中含有“JNI”字符的，认定是Java Native Interface方法，自动排除
 # 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除

 -keepclasseswithmembers class * {
     ... *JNI*(...);
 }

 -keepclasseswithmembernames class * {
 	... *JRI*(...);
 }

 -keep class **JNI* {*;}

