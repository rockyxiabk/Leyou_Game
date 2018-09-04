package com.leyou.game.fragment.mine;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.mine.TradeNoteAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.TradeBean;
import com.leyou.game.ipresenter.mine.ITradeNoteFragmentPresenter;
import com.leyou.game.presenter.mine.TradeNoteFragmentPresenter;
import com.leyou.game.util.SpaceItemDecoration;
import com.leyou.game.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Description : 用户买入和卖出明细列F表
 *
 * @author : rocky
 * @Create Time : 2017/5/10 下午1:51
 * @Modified By: rocky
 * @Modified Time : 2017/5/10 下午1:51
 */
public class TradeNoteFragment extends BaseFragment implements ITradeNoteFragmentPresenter, SwipeRefreshLayout.OnRefreshListener {
    private final static String TYPE = "id";
    @BindView(R.id.recycler_trade_note)
    RecyclerView recycler;
    @BindView(R.id.swipeRefresh_trade_note)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_layout_error)
    ImageView ivLayoutError;
    @BindView(R.id.re_trade_note_error)
    RelativeLayout reTradeNoteError;
    Unbinder unbinder;
    @BindView(R.id.re_trade_note_null)
    RelativeLayout reTradeNoteNull;
    private TradeNoteFragmentPresenter presenter;
    private List<TradeBean> list = new ArrayList<>();
    private TradeNoteAdapter adapter;
    private Handler handler = new Handler();

    public TradeNoteFragment() {

    }

    public static TradeNoteFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        TradeNoteFragment fragment = new TradeNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_trade_note;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        //设置下拉刷新背景颜色
        swipeRefresh.setProgressBackgroundColorSchemeColor(this.getResources().getColor(R.color.theme_color));
        //设置刷新进度颜色
        swipeRefresh.setColorSchemeResources(R.color.white, R.color.yellow, R.color.red_f2, R.color.purple);
        swipeRefresh.setOnRefreshListener(this);

        adapter = new TradeNoteAdapter(context, list, getArguments().getInt(TYPE, 0));
        recycler.setLayoutManager(new LinearLayoutManager(context));
        recycler.addItemDecoration(new SpaceItemDecoration(context, 1, R.color.white_e9));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void initPresenter() {
        presenter = new TradeNoteFragmentPresenter(context, this);
        presenter.getDataList(getArguments().getInt(TYPE));
    }

    @Override
    public void showErrorView() {
        reTradeNoteError.setVisibility(View.VISIBLE);
        reTradeNoteNull.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.GONE);
    }

    @Override
    public void showInduceView() {
        swipeRefresh.setRefreshing(false);
        reTradeNoteNull.setVisibility(View.VISIBLE);
        reTradeNoteError.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.GONE);
    }

    @Override
    public void showDataList(List<TradeBean> tradeBeanList) {
        swipeRefresh.setRefreshing(false);
        reTradeNoteError.setVisibility(View.GONE);
        reTradeNoteNull.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.VISIBLE);
        adapter.setTradeNotAdapter(tradeBeanList);
    }

    @Override
    public void showMassage(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
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
        presenter.unSubscribe();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_layout_error)
    public void onViewClicked() {
        presenter.getDataList(getArguments().getInt(TYPE));
    }

    @Override
    public void onRefresh() {
        presenter.getDataList(getArguments().getInt(TYPE));
    }
}
