package com.leyou.game.activity.friend;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.friend.CrowdApplyAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.dao.Crowd;
import com.leyou.game.ipresenter.friend.ICreateCrowd;
import com.leyou.game.ipresenter.friend.ICrowdApply;
import com.leyou.game.presenter.friend.ApplyCrowdPresenter;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 群聊申请同意页面
 *
 * @author : rocky
 * @Create Time : 2017/12/6 下午7:39
 * @Modified By: rocky
 * @Modified Time : 2017/12/6 下午7:39
 */
public class NewCrowdApplyActivity extends BaseActivity implements ICrowdApply {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.re_toolbar)
    RelativeLayout reToolbar;
    @BindView(R.id.recycler_apply_crowd)
    RecyclerView recyclerApplyCrowd;
    @BindView(R.id.frame_list)
    FrameLayout frameList;
    @BindView(R.id.re_no_apply)
    RelativeLayout reNoApply;
    @BindView(R.id.activity_friend_detail)
    RelativeLayout activityFriendDetail;
    private List<Crowd> crowdList = new ArrayList<>();
    private CrowdApplyAdapter applyAdapter;
    private ApplyCrowdPresenter presenter;
    private LoadingProgressDialog progressDialog;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_new_crowd_apply;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        applyAdapter = new CrowdApplyAdapter(this, crowdList, this);
        recyclerApplyCrowd.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerApplyCrowd.setAdapter(applyAdapter);
    }

    @Override
    public void initPresenter() {
        presenter = new ApplyCrowdPresenter(this, this);
    }

    @OnClick({R.id.iv_back, R.id.re_toolbar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.re_toolbar:
                break;
        }
    }

    @Override
    public void agreeApply(Crowd crowd) {
        presenter.agreeApply(crowd);
    }

    @Override
    public void ignoreApply(Crowd crowd) {
        presenter.ignoreApply(crowd);
    }

    @Override
    public void showApplyCrowd(List<Crowd> crowds) {
        frameList.setVisibility(View.VISIBLE);
        reNoApply.setVisibility(View.GONE);
        if (null != crowds && crowds.size() > 0) {
            crowdList.clear();
            crowdList.addAll(crowds);
            applyAdapter.setAdapterData(crowds);
        } else {
            showNullView();
        }
    }

    @Override
    public void showNullView() {
        reNoApply.setVisibility(View.VISIBLE);
        frameList.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressDialog = new LoadingProgressDialog(this, false);
        progressDialog.show();
    }

    @Override
    public void changeLoadingDes(String des) {
        if (null != progressDialog) {
            progressDialog.setLoadingText(des);
        }
    }

    @Override
    public void dismissedLoading() {
        if (null != progressDialog) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMessageToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }
}
