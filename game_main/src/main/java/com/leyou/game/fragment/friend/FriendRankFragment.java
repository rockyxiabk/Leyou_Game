package com.leyou.game.fragment.friend;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.friend.FriendRankAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.ContactBean;
import com.leyou.game.bean.GameRankBean;
import com.leyou.game.ipresenter.friend.IFriendRankFragment;
import com.leyou.game.presenter.friend.FriendRankFragmentPresenter;
import com.leyou.game.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Description : 好友游戏排行 单个游戏数据页面
 *
 * @author : rocky
 * @Create Time : 2017/7/26 上午9:49
 * @Modified By: rocky
 * @Modified Time : 2017/7/26 上午9:49
 */
public class FriendRankFragment extends BaseFragment implements IFriendRankFragment, SwipeRefreshLayout.OnRefreshListener {

    private final static String TITLE = "title";
    private final static String MARK_ID = "id";
    @BindView(R.id.recycler_win_result)
    RecyclerView recyclerWinResult;
    @BindView(R.id.swipeRefresh_win_result)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_layout_error)
    ImageView ivLayoutError;
    @BindView(R.id.re_win_result_error)
    RelativeLayout reWinResultError;
    Unbinder unbinder;
    private List<GameRankBean> list = new ArrayList<>();
    private FriendRankAdapter adapter;
    private FriendRankFragmentPresenter presenter;

    public FriendRankFragment() {

    }

    public static FriendRankFragment newInstance(String title, long mark) {
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putLong(MARK_ID, mark);
        FriendRankFragment fragment = new FriendRankFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_friend_rank;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        //设置下拉刷新背景颜色
        swipeRefresh.setProgressBackgroundColorSchemeColor(this.getResources().getColor(R.color.theme_color));
        //设置刷新进度颜色
        swipeRefresh.setColorSchemeResources(R.color.white, R.color.yellow, R.color.red_f2, R.color.purple);
        swipeRefresh.setOnRefreshListener(this);

        //添加适配器
        adapter = new FriendRankAdapter(getContext(), list);
        recyclerWinResult.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerWinResult.setAdapter(adapter);
    }

    @Override
    protected void initPresenter() {
        presenter = new FriendRankFragmentPresenter(context, this, getArguments().getLong(MARK_ID));
    }

    @Override
    public void showData(List<GameRankBean> resultList) {
        reWinResultError.setVisibility(View.GONE);
        list.clear();
        list.addAll(resultList);
        adapter.refreshAdapterData(resultList);
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showError() {
        swipeRefresh.setRefreshing(false);
        if (null == list || list.size() == 0) {
            reWinResultError.setVisibility(View.VISIBLE);
        } else {
            reWinResultError.setVisibility(View.GONE);
        }
        ToastUtils.showToastShort(getString(R.string.net_error));
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName() + getArguments().getString(TITLE));
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName() + getArguments().getString(TITLE));
    }

    @OnClick(R.id.iv_layout_error)
    public void onViewClicked() {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        presenter.getWinResultPersonList(getArguments().getLong(MARK_ID));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
