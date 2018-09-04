package com.leyou.game.fragment.mine;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.mine.ExChangeSaleAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.diamond.DiamondExchangeBean;
import com.leyou.game.event.PayEvent;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.ipresenter.mine.IExChange;
import com.leyou.game.presenter.mine.ExChangeSalePresenter;
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
 * Description : 交易所 钻石出售
 *
 * @author : rocky
 * @Create Time : 2017/6/20 下午5:36
 * @Modified By: rocky
 * @Modified Time : 2017/6/20 下午5:36
 */
public class SaleFragment extends BaseFragment implements IExChange, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "SaleFragment";
    @BindView(R.id.cb_0)
    CheckBox cb0;
    @BindView(R.id.cb_1)
    CheckBox cb1;
    @BindView(R.id.cb_2)
    CheckBox cb2;
    @BindView(R.id.cb_3)
    CheckBox cb3;
    @BindView(R.id.cb_4)
    CheckBox cb4;
    @BindView(R.id.ll_cb)
    LinearLayout llCb;
    @BindView(R.id.recycler_exchange_sale)
    RecyclerView recycler;
    @BindView(R.id.swipeRefresh_exchange_sale)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_layout_error)
    ImageView ivLayoutError;
    @BindView(R.id.re_exchange_sale_error)
    RelativeLayout reExchangeSaleError;
    @BindView(R.id.re_exchange_sale_null)
    RelativeLayout reExchangeSaleNull;
    Unbinder unbinder;
    private CheckBox[] cbArr = new CheckBox[5];
    private Handler handler = new Handler();
    private List<DiamondExchangeBean> list = new ArrayList<>();
    private ExChangeSaleAdapter adapter;
    private boolean isLoading;
    private int maxCoin = 9999999;
    private int minCoin = 0;
    private ExChangeSalePresenter presenter;

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

    public SaleFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_sale;
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
        adapter = new ExChangeSaleAdapter(getContext(), list);
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
                                presenter.getMoreExChangeList(minCoin, maxCoin);
                                isLoading = false;
                            }
                        }, 10);
                    }
                }
            }
        });
        recycler.setAdapter(adapter);

        cbArr[0] = cb0;
        cbArr[1] = cb1;
        cbArr[2] = cb2;
        cbArr[3] = cb3;
        cbArr[4] = cb4;
    }

    @Override
    protected void initPresenter() {
        presenter = new ExChangeSalePresenter(getContext(), this);
        presenter.getExChangeList(minCoin, maxCoin);
    }

    @OnClick({R.id.cb_0, R.id.cb_1, R.id.cb_2, R.id.cb_3, R.id.cb_4, R.id.iv_layout_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_0:
                minCoin = 0;
                maxCoin = 9999999;
                setCheckBox(0);
                break;
            case R.id.cb_1:
                minCoin = 0;
                maxCoin = 1000;
                setCheckBox(1);
                break;
            case R.id.cb_2:
                minCoin = 0;
                maxCoin = 5000;
                setCheckBox(2);
                break;
            case R.id.cb_3:
                minCoin = 0;
                maxCoin = 10000;
                setCheckBox(3);
                break;
            case R.id.cb_4:
                minCoin = 10001;
                maxCoin = 9999999;
                setCheckBox(4);
                break;
            case R.id.iv_layout_error:
                break;
        }
    }

    @Override
    public void onRefresh() {
        adapter.setLoadAllData(false);
        presenter.getExChangeList(minCoin, maxCoin);
    }

    @Override
    public void showRefreshData(List<DiamondExchangeBean> list) {
        swipeRefresh.setRefreshing(false);
        adapter.updateAdapter(list);
    }

    @Override
    public void loadMoreData(List<DiamondExchangeBean> list) {
        adapter.addAdapter(list);
    }

    @Override
    public void showRefreshBuyData(List<DiamondExchangeBean> list) {

    }

    @Override
    public void loadMoreBuyData(List<DiamondExchangeBean> list) {

    }

    @Override
    public void isShowNullListView(boolean flag) {
        if (flag) {
            reExchangeSaleNull.setVisibility(View.VISIBLE);
        } else {
            reExchangeSaleNull.setVisibility(View.GONE);
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

    public void setCheckBox(int index) {
        for (int i = 0; i < cbArr.length; i++) {
            cbArr[i].setChecked(false);
            cbArr[i].setTextColor(getResources().getColor(R.color.black_a46));
        }
        cbArr[index].setChecked(true);
        cbArr[index].setTextColor(getResources().getColor(R.color.white));
        swipeRefresh.setRefreshing(true);
        presenter.getExChangeList(minCoin, maxCoin);
    }
}
