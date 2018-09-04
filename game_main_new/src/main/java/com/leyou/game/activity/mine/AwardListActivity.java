package com.leyou.game.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.mine.AwardAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.game.GameWinPriseBean;
import com.leyou.game.event.MainTabEvent;
import com.leyou.game.event.PriceStateChangeEvent;
import com.leyou.game.ipresenter.mine.IAwardListActivity;
import com.leyou.game.presenter.mine.AwardListPresenter;
import com.leyou.game.util.SpaceItemDecoration;
import com.leyou.game.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 我的获奖列表
 *
 * @author : rocky
 * @Create Time : 2017/6/19 下午5:29
 * @Modified By: rocky
 * @Modified Time : 2017/6/19 下午5:29
 */
public class AwardListActivity extends BaseActivity implements IAwardListActivity {

    @BindView(R.id.iv_award_back)
    ImageView ivAwardBack;
    @BindView(R.id.recycler_user_award)
    RecyclerView recyclerUserAward;
    @BindView(R.id.iv_layout_null)
    ImageView ivLayoutNull;
    @BindView(R.id.re_exchange_award_null)
    RelativeLayout reExchangeAwardNull;
    private AwardAdapter adapter;
    private List<GameWinPriseBean> list = new ArrayList<>();
    private AwardListPresenter presenter;
    private boolean isLoading = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void prizeStateChangeEvent(PriceStateChangeEvent event) {
        presenter.getUserAwardList();
    }

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_award_list;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        //添加适配器
        adapter = new AwardAdapter(this, list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerUserAward.setLayoutManager(layoutManager);
        recyclerUserAward.addItemDecoration(new SpaceItemDecoration(this, 1, R.color.white_e9));
        recyclerUserAward.setAdapter(adapter);
        recyclerUserAward.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    if (!isLoading) {
                        isLoading = true;
                        presenter.loadMoreAwardList();
                    }
                }
            }
        });
    }

    @Override
    public void initPresenter() {
        presenter = new AwardListPresenter(this, this);
    }

    @OnClick({R.id.iv_award_back, R.id.iv_layout_null})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_award_back:
                finishCurrentActivity();
                break;
            case R.id.iv_layout_null:
                presenter.getUserAwardList();
                break;
        }
    }

    @Override
    public void showMyAwardList(List<GameWinPriseBean> list) {
        isLoading = false;
        reExchangeAwardNull.setVisibility(View.GONE);
        recyclerUserAward.setVisibility(View.VISIBLE);
        adapter.setListAdapter(list);
    }

    @Override
    public void showLoadMoreList(List<GameWinPriseBean> list) {
        isLoading = false;
        adapter.loadMoreData(list);
    }

    @Override
    public void setAwardLoadAll(boolean isLoadAll) {

    }

    @Override
    public void showNullView() {

    }

    @Override
    public void showErrorView() {
        recyclerUserAward.setVisibility(View.GONE);
        reExchangeAwardNull.setVisibility(View.VISIBLE);
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
        presenter.destroy();
        EventBus.getDefault().unregister(this);
    }
}
