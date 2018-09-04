package com.leyou.game.fragment.friend;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.leyou.game.R;
import com.leyou.game.activity.friend.SearchFriendActivity;
import com.leyou.game.adapter.friend.CrowdAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.friend.IAddFriend;
import com.leyou.game.presenter.friend.AddFriendPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Description : 添加群聊-搜索群聊
 *
 * @author : rocky
 * @Create Time : 2017/12/6 下午8:48
 * @Modified By: rocky
 * @Modified Time : 2017/12/6 下午8:48
 */
public class AddCrowdFragment extends BaseFragment implements IAddFriend {


    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    Unbinder unbinder;
    private List<Crowd> list = new ArrayList<>();
    private AddFriendPresenter presenter;
    private CrowdAdapter adapter;

    public AddCrowdFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_add_crowd;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        adapter = new CrowdAdapter(context, list);
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void initPresenter() {
        presenter = new AddFriendPresenter(context, this);
        presenter.getRecommendCrowdList();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void changeLoadingDes(String des) {

    }

    @Override
    public void dismissedLoading() {

    }

    @Override
    public void showMessageToast(String msg) {

    }

    @Override
    public void showRecommendFriend(List<Friend> friends) {

    }

    @Override
    public void showRecommendCrowd(List<Crowd> crowds) {
        if (null != crowds && crowds.size() > 0) {
            adapter.setAdapterData(crowds);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ll_search)
    public void onViewClicked() {
        Intent intent = new Intent(context, SearchFriendActivity.class);
        intent.putExtra("type", 2);
        context.startActivity(intent);
    }
}