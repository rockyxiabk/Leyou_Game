package com.leyou.game.util;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.leyou.game.GameApplication;

/**
 * Description : app跳转
 *
 * @author : rocky
 * @Create Time : 2017/11/23 上午12:33
 * @Modified Time : 2017/11/23 上午12:33
 */
public class AppUtil {
    /**
     * 跳转到app详情界面
     *
     * @param appPkg    App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void startMarketAppDetail(String appPkg) {
        try {
            if (TextUtils.isEmpty(appPkg))
                return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            if (!TextUtils.isEmpty(marketPkg)) {
//                intent.setPackage(marketPkg);
//            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            GameApplication.getInstance().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToastShort("未发现相关应用商店");
        }
    }
}
