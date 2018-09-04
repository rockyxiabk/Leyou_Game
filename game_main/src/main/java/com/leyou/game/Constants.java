package com.leyou.game;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.facebook.common.util.ByteConstants;
import com.leyou.game.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Description : com.leyou.game
 *
 * @author : rocky
 * @Create Time : 2017/3/29 上午11:06
 * @Modified Time : 2017/3/29 上午11:06
 */
public class Constants {

    // 填写从短信SDK应用后台注册得到的APPSECRET
    public static String APPKEY = "1d251a0438cc6";
    public static String APPSECRET = "5dff9b631f2694f7e60c5ac2adf22949";

    public static String WX_APP_ID = "wxd4075568f673e687";
    public static String WX_APP_SECRET = "5fccf3828ceb5cda851aff7ad29a2352";

    //app文件夹
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/StormGame/";
    public static final String IMAGE_CACHE_DIR = ROOT_DIR + "cache/image/";
    public static final String DOWNLOAD_DIR = "/StormGame/download/";
    public static final String UPDATE_DIR = ROOT_DIR + "download/";
    public static final String WEB_DIR = ROOT_DIR + "cache/web/";
    public static final String WEB_APP_CACHE_DIR = WEB_DIR + "appcache";
    public static final String WEB_DATABASES_CACHE_DIR = WEB_DIR + "databases";
    public static final String WEB_GEOLOCATION_CACHE_DIR = WEB_DIR + "geolocation";

    public static final String PRIZE_ACTION = "com.leyou.game.PRIZE_ACTION";//中奖信息
    public static final String NEW_VERSION_ACTION = "com.leyou.game.NEW_VERSION_ACTION";//新版本信息
    public static final String DIAMOND_CONVERT = "com.leyou.game.DIAMOND_CONVERT_ACTION";//钻石流量兑换
    public static final String OFF_LINE_ACTION = "com.leyou.game.OFF_LINE_ACTION";//下线通知

    //小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 5 * ByteConstants.MB;
    //小图磁盘缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 10 * ByteConstants.MB;
    //默认图极低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_LOW_SIZE = 30 * ByteConstants.MB;
    //默认图磁盘缓存的最大值
    public static final int MAX_DISK_CACHE_VERYLOW_SIZE = 10 * ByteConstants.MB;
    //小图所放路径的文件夹名
    public static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "imagePipeline_small_cache";
    //默认图所放路径的文件夹名
    public static final String IMAGE_PIPELINE_CACHE_DIR = "imagePipeline_cache";
    //最大缓存大小
    public static final long MAX_DISK_CACHE_SIZE = 30 * ByteConstants.MB;


    public static final String ROOT_URL_TEST = "http://192.168.0.13";//游戏平台测试地址
    public static final String ROOT_URL = "http://59.110.52.197";//游戏平台测试地址

    public static final String URL = ROOT_URL + ":18080/leyou/";//游戏平台测试地址
    public static final String WOLF_KILL_URL = ROOT_URL + ":8081/lrs/";//狼人杀游戏口地址
    public static final String TREASURE_URL = ROOT_URL + ":8080/baoku/";//宝库正式接口

    public static final String MAIN_GUIDE_URL = "http://59.110.52.197:28080/readme/intro/guide.html";//诱导链接
    public static final String TREASURE_PLAY_EXPLAIN = "http://59.110.52.197:28080/readme/intro/intro.html";//宝库玩法介绍
    public static final String TREASURE_WORKER_UPGRADE = "http://59.110.52.197:28080/readme/intro/shengji.html";//矿工升级介绍
    public static final String TREASURE_WORKER_COMPOSE = "http://59.110.52.197:28080/readme/intro/hecheng.html";//矿工合成介绍
    public static final String TREASURE_WORKER_DISMISS = "http://59.110.52.197:28080/readme/intro/fire.html";//矿工解雇说明
    public static final String MINE_SIGNED = "http://59.110.52.197:28080/readme/intro/diamond.html";//签到介绍
    public static final String WIN_AWARD_EXPLAIN = "http://59.110.52.197:28080/readme/intro/game_intro.html";//赢大奖玩法介绍
    public static final String MINE_AGREEMENT = "http://59.110.52.197:28080/readme/intro/agreement.html";//用户协议
    public static final String MINE_FUNCTION = "http://59.110.52.197:28080/readme/intro/about.html";//关于介绍
    public static final String MINE_DEVELOPER = "http://59.110.52.197:28080/page/duomi/index.html";//首页 开发者
    public static final String FRIEND_AWARD_EXPLAIN = "http://59.110.52.197:28080/readme/intro/friendRanking.html";//好友排行介绍
    public static final String FIGHT_GAME_WOLF_KILL_RULE = "http://59.110.52.197:28080/readme/intro/index_rule.html";//狼人杀游戏规则
    public static final String DIAMOND_CONVERT_URL = "http://59.110.52.197:28080/readme/intro/exchangeMB.html";//钻石兑换流量

    public static int GAME_FREE_TIMES = 3;
    public static int TEN = 10;
    public static int FIFTEEN = 15;
    public static int TWENTY = 20;
    public static int TREASURE_THREE_480 = 2;
    public static int TREASURE_THREE_720 = 3;
    public static int TREASURE_FIVE_480 = 4;
    public static int TREASURE_FIVE_720 = 5;
    private static String packageName;
    private static String versionName;
    private static int versionCode;
    private static String channelId;
    private static String imei;
    private static String imsi;
    private static String net;
    private static String apn;
    private static String clientId;

    public static void init(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo;
        packageName = context.getPackageName();
        try {
            packInfo = packageManager.getPackageInfo(packageName, 0);
            versionName = packInfo.versionName;
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        ApplicationInfo appInfo;
        try {
            appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            channelId = appInfo.metaData.get("UMENG_CHANNEL").toString();
        } catch (PackageManager.NameNotFoundException e) {
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
        if (null == imei) {
            imei = tm.getSimSerialNumber();
        }
        imsi = tm.getSubscriberId();

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            net = networkInfo.getTypeName();
            apn = networkInfo.getExtraInfo();
        } else {
            net = "";
            apn = "";
        }
        getStartJson();
    }

    /**
     * 获取手机信息和应用版本信息
     *
     * @return
     */
    public static JSONObject getStartJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("androidVersion", Build.VERSION.RELEASE);
            obj.put("packageName", packageName);
            obj.put("appVersion", versionName);
            obj.put("versionCode", versionCode);
            obj.put("imei", imei);
            obj.put("imsi", imsi);
            obj.put("netModel", net);
            obj.put("netApn", apn);
            obj.put("phoneFactory", Build.BRAND);
            obj.put("phoneType", Build.MODEL);
            obj.put("chId", channelId);
        } catch (JSONException e) {
        }
        LogUtil.i("tag", "----phoneinfo:" + obj.toString());
        return obj;
    }

    public static String getIMEI() {
        return imei;
    }

    public static String getIMSI() {
        return imsi;
    }

    public static String getChannelId() {
        return channelId;
    }

    public static String getVerName() {
        return versionName;
    }

    public static int getVerCode() {
        return versionCode;
    }

    public static String getPackageName() {
        return packageName;
    }

    public static String getNet() {
        return net;
    }

    public static String getClientId() {
        return clientId;
    }

    public static void setClientId(String clientId) {
        Constants.clientId = clientId;
    }
}
