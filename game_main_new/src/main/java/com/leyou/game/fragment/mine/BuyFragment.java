package com.leyou.game.fragment.mine;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.mine.ExChangeBuyAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.diamond.DiamondExchangeBean;
import com.leyou.game.event.PayEvent;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.ipresenter.mine.IExChange;
import com.leyou.game.presenter.mine.ExChangeBuyPresenter;
import com.leyou.game.util.LogUtil;
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
import butterknife.Unbinder;

/**
 * Description : 交易所 钻石购买
 *
 * @author : rocky
 * @Create Time : 2017/6/20 下午5:13
 * @Modified By: rocky
 * @Modified Time : 2017/6/20 下午5:13
 */
public class BuyFragment extends BaseFragment implements IExChange, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "BuyFragment";
    @BindView(R.id.recycler_exchange_buy)
    RecyclerView recycler;
    @BindView(R.id.swipeRefresh_exchange_buy)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_layout_error)
    ImageView ivLayoutError;
    @BindView(R.id.re_exchange_buy_error)
    RelativeLayout reExchangeBuyError;
    @BindView(R.id.iv_layout_null)
    ImageView ivLayoutNull;
    @BindView(R.id.re_exchange_buy_null)
    RelativeLayout reExchangeBuyNull;
    Unbinder unbinder;
    private Handler handler = new Handler();
    private List<DiamondExchangeBean> list = new ArrayList<>();
    private ExChangeBuyAdapter adapter;
    private boolean isLoading;
    private ExChangeBuyPresenter presenter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUserData(RefreshEvent event) {
        if (event.getIsRefresh() == RefreshEvent.REFRESH) {
            if (event.getSourceType() == RefreshEvent.MINE) {
                onRefresh();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payResult(PayEvent event) {
        onRefresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public BuyFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_buy;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        //设置下拉刷新背景颜色
        swipeRefresh.setProgressBackgroundColorSchemeColor(this.getResources().getColor(R.color.white_1));
        //设置刷新进度颜色
        swipeRefresh.setColorSchemeResources(R.color.blue_44, R.color.blue_42, R.color.purple_62, R.color.purple_74);
        swipeRefresh.setOnRefreshListener(this);

        //添加适配器
        adapter = new ExChangeBuyAdapter(getContext(), list);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recycler.setLayoutManager(gridLayoutManager);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LogUtil.d(TAG, "StateChanged = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("test", "onScrolled");
                int lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    boolean isRefreshing = swipeRefresh.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                presenter.getMoreExChangeList();
                                isLoading = false;
                            }
                        }, 10);
                    }
                }
            }
        });
        recycler.setAdapter(adapter);
    }

    @Override
    protected void initPresenter() {
        presenter = new ExChangeBuyPresenter(getContext(), this);
        presenter.getExChangeList();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.iv_layout_null)
    public void onViewClicked() {
        presenter.getExChangeList();
    }

    @Override
    public void onRefresh() {
        adapter.setLoadAllData(false);
        presenter.getExChangeList();
    }

    @Override
    public void showRefreshData(List<DiamondExchangeBean> list) {
    }

    @Override
    public void loadMoreData(List<DiamondExchangeBean> list) {

    }

    @Override
    public void showRefreshBuyData(List<DiamondExchangeBean> lists) {
        swipeRefresh.setRefreshing(false);
        list.clear();
        list.addAll(lists);
        adapter.updateAdapter(lists);
    }

    @Override
    public void loadMoreBuyData(List<DiamondExchangeBean> lists) {
        list.addAll(lists);
        adapter.addAdapter(lists);
    }

    @Override
    public void isShowNullListView(boolean flag) {
        if (flag) {
            reExchangeBuyNull.setVisibility(View.VISIBLE);
        } else {
            reExchangeBuyNull.setVisibility(View.GONE);
        }
    }

    @Override
    public void setAdapterIsLoadAll(boolean flag) {
        adapter.setLoadAllData(flag);
    }

    @Override
    public void showMessage(final String msg) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastLong(msg);
            }
        });
    }
}
