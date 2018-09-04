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
import com.leyou.game.adapter.win.GameAwardAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.game.GameRankBean;
import com.leyou.game.bean.win.WinGameAwardBean;
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

    private static final String TAG = "FriendRankFragment";
    private final static String TITLE = "title";
    private final static String MARK_ID = "id";
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
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
    private List<WinGameAwardBean> listW = new ArrayList<>();
    private GameAwardAdapter adapterW;

    public FriendRankFragment() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public static FriendRankFragment newInstance(String title, String uniqueMarkId) {
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MARK_ID, uniqueMarkId);
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
        swipeRefresh.setProgressBackgroundColorSchemeColor(getContext().getResources().getColor(R.color.white_1));
        //设置刷新进度颜色
        swipeRefresh.setColorSchemeResources(R.color.blue_44, R.color.blue_42, R.color.purple_62, R.color.purple_74);
        swipeRefresh.setOnRefreshListener(this);

        //添加适配器
        adapter = new FriendRankAdapter(getContext(), list);
        recyclerWinResult.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerWinResult.setAdapter(adapter);

        adapterW = new GameAwardAdapter(getContext(), listW);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterW);
    }

    @Override
    protected void initPresenter() {
        presenter = new FriendRankFragmentPresenter(context, this, getArguments().getString(MARK_ID));
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
    public void showPrizeData(List<WinGameAwardBean> list) {
        adapterW.loadMoreData(list);
    }

    @Override
    public void showError(boolean hasNet) {
        swipeRefresh.setRefreshing(false);
        if (hasNet) {
            ToastUtils.showToastShort("暂无排名");
            ivLayoutError.setImageResource(R.mipmap.icon_game_rank_no_rank);
        } else {
            ToastUtils.showToastShort(getString(R.string.data_load_failed_try));
            ivLayoutError.setImageResource(R.mipmap.icon_error);
        }
        if (null == list || list.size() == 0) {
            reWinResultError.setVisibility(View.VISIBLE);
        } else {
            reWinResultError.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick(R.id.iv_layout_error)
    public void onViewClicked() {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        presenter.getWinResultPersonList(getArguments().getString(MARK_ID));
        presenter.getDataList(getArguments().getString(MARK_ID));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
