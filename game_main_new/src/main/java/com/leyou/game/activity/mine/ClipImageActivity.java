package com.leyou.game.activity.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.util.BitmapUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.widget.clip.ClipImageLayout;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 图片剪切
 *
 * @author : rocky
 * @Create Time : 2017/5/27 下午2:17
 * @Modified By: rocky
 * @Modified Time : 2017/5/27 下午2:17
 */
public class ClipImageActivity extends BaseActivity {

    @BindView(R.id.iv_clip_back)
    ImageView ivClipBack;
    @BindView(R.id.tv_clip_image_confirm)
    TextView tvClipImageConfirm;
    @BindView(R.id.clipImageLayout)
    ClipImageLayout clipImageLayout;
    private String imagePath;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_clip_image;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        imagePath = getIntent().getStringExtra("imagePath");
        clipImageLayout.setHorizontalPadding(0);
        clipImageLayout.setImageSource("file://" + imagePath);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.iv_clip_back, R.id.tv_clip_image_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_clip_back:
                finishCurrentActivity();
                break;
            case R.id.tv_clip_image_confirm:
                Bitmap clip = clipImageLayout.clip();
                String imagePath = BitmapUtil.saveBitmap(clip);
                LogUtil.d(this.getClass().getSimpleName(), imagePath);
                Intent data = new Intent();
                data.putExtra("image", imagePath);
                setResult(-1, data);
                finishCurrentActivity();
                break;
        }
    }
}
