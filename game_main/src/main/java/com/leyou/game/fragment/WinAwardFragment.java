package com.leyou.game.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.win.WinAwardAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.GameBean;
import com.leyou.game.event.RefreshWinAwardEvent;
import com.leyou.game.ipresenter.win.IWinAwardFragment;
import com.leyou.game.presenter.win.WinAwardFragmentPresenter;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.WebViewDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Description : 赢大奖页面
 *
 * @author : rocky
 * @Create Time : 2017/3/29 下午4:23
 * @Modified By: rocky
 * @Modified Time : 2017/3/29 下午4:23
 */
public class WinAwardFragment extends BaseFragment implements IWinAwardFragment, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.tv_game_explain)
    TextView tvGameExplain;
    @BindView(R.id.recycler_winAward)
    RecyclerView recyclerWinAward;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.re_win_toolBar)
    RelativeLayout reWinToolBar;
    @BindView(R.id.re_load_try)
    RelativeLayout reLoadTry;
    @BindView(R.id.iv_layout_error)
    ImageView ivLayoutError;
    private Unbinder unbinder;
    private WinAwardFragmentPresenter presenter;
    private List<GameBean> list = new ArrayList<>();
    private WinAwardAdapter adapter;
    private boolean isAutoRefresh = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUserData(RefreshWinAwardEvent event) {
        if (event.getEvent() == RefreshWinAwardEvent.REFRESH) {
            onRefresh();
        }
    }

    public WinAwardFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_win_award;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        //设置下拉刷新背景颜色
        swipeRefresh.setProgressBackgroundColorSchemeColor(getContext().getResources().getColor(R.color.theme_color));
        //设置刷新进度颜色
        swipeRefresh.setColorSchemeResources(R.color.white, R.color.yellow, R.color.red_f2, R.color.purple);
        swipeRefresh.setOnRefreshListener(this);

        //添加适配器
        adapter = new WinAwardAdapter(getContext(), list);
        recyclerWinAward.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerWinAward.setAdapter(adapter);
    }

    @Override
    protected void initPresenter() {
        presenter = new WinAwardFragmentPresenter(context, this);
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        presenter.unSubscribe();
    }

    @OnClick({R.id.tv_game_explain, R.id.iv_layout_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_game_explain:
                WebViewDialog webViewDialog = new WebViewDialog(context, getString(R.string.game_explain), Constants.WIN_AWARD_EXPLAIN);
                webViewDialog.show();
                break;
            case R.id.iv_layout_error:
                presenter.request();
                swipeRefresh.setRefreshing(true);
                adapter.updateAdapter(null);
                showView(true);
                break;
        }
    }

    @Override
    public void onRefresh() {
        presenter.request();
        isAutoRefresh = false;
    }

    @Override
    public void showView(boolean isError) {
        if (!isError) {
            reLoadTry.setVisibility(View.VISIBLE);
            swipeRefresh.setVisibility(View.GONE);
        } else {
            swipeRefresh.setVisibility(View.VISIBLE);
            reLoadTry.setVisibility(View.GONE);
        }
    }

    @Override
    public void showGameList(List<GameBean> gameBeenList) {
        swipeRefresh.setRefreshing(false);
        adapter.updateAdapter(gameBeenList);
        if (!isAutoRefresh) {
            ToastUtils.showToastShort(getString(R.string.data_refresh_success));
        }
    }

    @Override
    public void showError(String error) {
        ToastUtils.showToastShort(error);
        swipeRefresh.setRefreshing(false);
    }
}
