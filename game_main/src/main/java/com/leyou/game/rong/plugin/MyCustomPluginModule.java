package com.leyou.game.rong.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

/**
 * Description : 自定义扩展输入功能区
 *
 * @author : rocky
 * @Create Time : 2017/8/15 上午9:40
 * @Modified Time : 2017/8/15 上午9:40
 */
public class MyCustomPluginModule implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return null;
    }

    @Override
    public String obtainTitle(Context context) {
        return null;
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {

    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
