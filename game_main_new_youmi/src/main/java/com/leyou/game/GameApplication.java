package com.leyou.game;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.view.WindowManager;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.igexin.sdk.PushManager;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.UserData;
import com.leyou.game.dao.Friend;
import com.leyou.game.rong.RongCloudEvent;
import com.leyou.game.service.GeTuiIntentService;
import com.leyou.game.service.GeTuiPushService;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.UnCrashThread;
import com.leyou.game.util.newapi.FriendApi;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.utils.SystemUtils;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import rx.Observer;

/**
 * Description : 应用程序层初始化应用操作
 *
 * @author : rocky
 * @Create Time : 2017/3/29 上午10:54
 * @Modified By: rocky
 * @Modified Time : 2017/3/29 上午10:54
 */
public class GameApplication extends MultiDexApplication {
    private static final String TAG = "GameApplication";
    private static GameApplication instance;
    private static Context context;
    private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();

    public static GameApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    public WindowManager.LayoutParams getWindowParams() {
        return windowParams;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        initFresco();
        if (getPackageName().equals(SystemUtils.getCurProcessName(getApplicationContext()))) {
            instance = this;
            Constants.init(this);
            initRestart();
            initPush();
            initUM();
            UserData.getInstance().init(this, true);
            initIM();
            initX5();
        } else {
            UserData.getInstance().init(this, false);
        }
//        String deviceInfo = getDeviceInfo(this);
//        LogUtil.d(TAG, "deviceInfo:" + deviceInfo);
    }

    private void initPush() {
        PushManager.getInstance().initialize(this.getApplicationContext(), GeTuiPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);
    }

    private void initUM() {
        MobclickAgent.enableEncrypt(true);//日志加密
        MobclickAgent.setDebugMode(false);
        MobclickAgent.openActivityDurationTrack(false);
        PlatformConfig.setWeixin(Constants.WX_APP_ID, Constants.WX_APP_SECRET);
        PlatformConfig.setQQZone(Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
    }

    private void initIM() {
        RongIM.init(this);
        RongCloudEvent.init(this);
    }

    private void initX5() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtil.e(TAG, " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                LogUtil.e(TAG, " onCoreInitFinished  加载系统内核");
            }
        };
        QbSdk.initX5Environment(this, cb);
        QbSdk.setDownloadWithoutWifi(true);
    }

    private void initFresco() {
        int MAX_MEM = 30 * ByteConstants.MB;
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEM,// 内存缓存中总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,// 内存缓存中图片的最大数量。
                MAX_MEM,// 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,// 内存缓存中准备清除的总图片的最大数量。
                Integer.MAX_VALUE);// 内存缓存中单个图片的最大大小。
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };
        //小图片的磁盘配置
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryPath(this.getApplicationContext().getCacheDir())//缓存图片基路径
                .setBaseDirectoryName(Constants.IMAGE_PIPELINE_SMALL_CACHE_DIR)//文件夹名
                .setMaxCacheSize(Constants.MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(Constants.MAX_SMALL_DISK_LOW_CACHE_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(Constants.MAX_SMALL_DISK_VERYLOW_CACHE_SIZE)//缓存的最大大小,当设备极低磁盘空间
                .build();
        //默认图片的磁盘配置
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryPath(new File(Constants.IMAGE_CACHE_DIR))
                .setBaseDirectoryName(Constants.IMAGE_PIPELINE_CACHE_DIR)
                .setMaxCacheSize(Constants.MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(Constants.MAX_DISK_CACHE_LOW_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(Constants.MAX_DISK_CACHE_VERYLOW_SIZE)//缓存的最大大小,当设备极低磁盘空间
                .build();
        //缓存图片配置
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
//                .setCacheKeyFactory(cacheKeyFactory)
                .setDownsampleEnabled(true)
//                .setWebpSupportEnabled(true)
//                .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)
//                .setExecutorSupplier(executorSupplier)
//                .setImageCacheStatsTracker(imageCacheStatsTracker)
//                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
//                .setNetworkFetchProducer(networkFetchProducer)
//                .setPoolFactory(poolFactory)
//                .setProgressiveJpegConfig(progressiveJpegConfig)
//                .setRequestListeners(requestListeners)
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig)
                .setMainDiskCacheConfig(diskCacheConfig)
                .build();
        Fresco.initialize(context, config);
    }

    /**
     * 异常崩溃，重新启动应用
     */
    private void initRestart() {
        UnCrashThread unCrashThread = new UnCrashThread(this);
        Thread.setDefaultUncaughtExceptionHandler(unCrashThread);
    }

    public static void getMyFriends() {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getMyFriendList(), new Observer<ResultArray<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                e.printStackTrace();
            }

            @Override
            public void onNext(ResultArray<Friend> contactBeanResultArray) {
                if (contactBeanResultArray.result == 1) {
                    List<Friend> list = contactBeanResultArray.data;
                    if (null != list && list.size() > 0) {
                        if (null != list && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                DBUtil.getInstance(GameApplication.context).updateMyFriend(list.get(i));
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 个推 绑定别名
     *
     * @param userId
     */
    public static void bindAlias(String userId) {
        LogUtil.e(TAG, userId);
        boolean bindAlias = PushManager.getInstance().bindAlias(context, userId);
        LogUtil.e(TAG, "----bind-->" + bindAlias);
    }

    /**
     * 个推 解绑别名
     *
     * @param userId
     */
    public static void unBindAlias(String userId) {
        LogUtil.e(TAG, userId);
        boolean unBindAlias = PushManager.getInstance().unBindAlias(context, userId, true);
        LogUtil.e(TAG, "----unBind-->" + unBindAlias);
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在  之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
    public static void connect(String token) {
        LogUtil.d(TAG, "------tokenId:" + token);
        if (GameApplication.getInstance().getApplicationInfo().packageName.equals(SystemUtils.getCurProcessName(getContext()))) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查
                 * 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    LogUtil.d(TAG, "----tokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userId 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userId) {
                    LogUtil.d(TAG, "----userId" + userId);
                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(userId, UserData.getInstance().getNickname(), Uri.parse(UserData.getInstance().getPictureUrl())));
                    RongIM.getInstance().setMessageAttachedUserInfo(true);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtil.d(TAG, "----errorCode:" + errorCode);
                }
            });
        }
    }

    /**
     * <p>断开与融云服务器的连接。当调用此接口断开连接后，仍然可以接收 Push 消息。
     * <p>若想断开连接后不接受 Push 消息
     */
    public static void disconnect() {
        RongIM.getInstance().disconnect();
    }

    /**
     * <p>断开与融云服务器的连接。当调用此接口断开连接后，仍然可以接收 Push 消息。
     * <p>若想断开连接后不接受 Push 消息
     */
    public static void logout() {
        RongIM.getInstance().logout();
    }

    public static void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            LogUtil.d(TAG, "deviceInfo:--id--" + device_id);
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            LogUtil.d(TAG, "deviceInfo:--mac--" + mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            LogUtil.d(TAG, "deviceInfo:--id--" + device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.fillInStackTrace());
        }
        return null;
    }

}
