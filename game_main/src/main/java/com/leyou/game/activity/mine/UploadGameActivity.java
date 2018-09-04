package com.leyou.game.activity.mine;

import android.os.Bundle;

import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Description : 上传游戏页面
 *
 * @author : rocky
 * @Create Time : 2017/4/18 上午10:53
 * @Modified By: rocky
 * @Modified Time : 2017/4/18 上午10:53
 */
public class UploadGameActivity extends BaseActivity {

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_upload_game;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {

    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }
}
