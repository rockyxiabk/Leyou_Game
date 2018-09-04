package com.leyou.game.activity;

import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.dao.Friend;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 头像大图
 *
 * @author : rocky
 * @Create Time : 2017/9/4 下午5:49
 * @Modified By: rocky
 * @Modified Time : 2017/9/4 下午5:49
 */
public class PictureDetailActivity extends BaseActivity {

    @BindView(R.id.iv_image)
    SimpleDraweeView ivImage;
    private Friend friend;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_picture_detial;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        friend = getIntent().getParcelableExtra("friend");
        ivImage.setImageURI(friend.getPictureUrl());
    }

    @Override
    public void initPresenter() {

    }

    @OnClick(R.id.iv_image)
    public void onViewClicked() {
        finishCurrentActivity();
    }
}
