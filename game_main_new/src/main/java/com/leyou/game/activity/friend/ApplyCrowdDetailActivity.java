package com.leyou.game.activity.friend;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.Result;
import com.leyou.game.dao.Crowd;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.newapi.CrowdApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import rx.Observer;

/**
 * Description :查找群-详情页面-申请加入群
 *
 * @author : rocky
 * @Create Time : 2017/12/7 上午10:10
 * @Modified By: rocky
 * @Modified Time : 2017/12/7 上午10:10
 */
public class ApplyCrowdDetailActivity extends BaseActivity {

    @BindView(R.id.iv_order_pay_back)
    ImageView ivOrderPayBack;
    @BindView(R.id.tv_crowd_name)
    TextView tvCrowdName;
    @BindView(R.id.iv_crowd_header)
    SimpleDraweeView ivCrowdHeader;
    @BindView(R.id.tv_crowd_nick_name)
    TextView tvCrowdNickName;
    @BindView(R.id.tv_crowd_introduce)
    TextView tvCrowdIntroduce;
    @BindView(R.id.btn_join_crowd)
    Button btnJoinCrowd;
    private Crowd crowdInfo;
    private int type;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_apply_crowd_detail;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        crowdInfo = getIntent().getParcelableExtra("crowdInfo");

        ivCrowdHeader.setImageURI(crowdInfo.getHeadImgUrl());
        tvCrowdNickName.setText("群昵称:" + crowdInfo.getName() + "");
        tvCrowdIntroduce.setText("群简介:" + crowdInfo.getIntroduction() + "");
        if (crowdInfo.isJoin == 1) {
            type = 1;
            btnJoinCrowd.setText("进入群聊");
        } else {
            type = 0;
        }
    }

    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.iv_order_pay_back, R.id.btn_join_crowd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_order_pay_back:
                finishCurrentActivity();
                break;
            case R.id.btn_join_crowd:
                if ("进入群聊".equalsIgnoreCase(btnJoinCrowd.getText().toString())) {
                    RongIM.getInstance().startConversation(this,
                            Conversation.ConversationType.GROUP,
                            crowdInfo.getGroupId(),
                            crowdInfo.getName());
                    finishCurrentActivity();
                } else {
                    applyJoinCrowd(crowdInfo.getGroupId());
                }
                break;
        }
    }

    private void applyJoinCrowd(String groupId) {
        HttpUtil.subscribe(HttpUtil.createApi(CrowdApi.class, Constants.URL).applyJoin(groupId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToastShort("申请失败，再试一次");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    ToastUtils.showToastShort("申请成功，等待群主同意");
                    finishCurrentActivity();
                } else {
                    ToastUtils.showToastShort("申请失败，再试一次");
                }
            }
        });
    }
}
