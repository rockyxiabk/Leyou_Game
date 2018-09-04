package com.leyou.game.fragment.friend;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.leyou.game.R;
import com.leyou.game.activity.friend.SearchFriendActivity;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.friend.FriendAdapter;
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
 * Description : 添加好友页面-搜索好友
 *
 * @author : rocky
 * @Create Time : 2017/12/6 下午8:40
 * @Modified By: rocky
 * @Modified Time : 2017/12/6 下午8:40
 */
public class AddFriendFragment extends BaseFragment implements IAddFriend, CustomItemClickListener {


    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    Unbinder unbinder;
    private List<Friend> list = new ArrayList<>();
    private AddFriendPresenter presenter;
    private FriendAdapter adapter;

    public AddFriendFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_add_friend;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        adapter = new FriendAdapter(context, list);
        adapter.setCustomItemClickListener(this);
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void initPresenter() {
        presenter = new AddFriendPresenter(context, this);
        presenter.getRecommendFriendList();
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
        if (null != friends && friends.size() > 0) {
            list.clear();
            list.addAll(friends);
            adapter.setAdapterData(friends);
        }
    }

    @Override
    public void showRecommendCrowd(List<Crowd> crowds) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ll_search)
    public void onViewClicked() {
        Intent intent = new Intent(context, SearchFriendActivity.class);
        intent.putExtra("type", 1);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        Friend friend = list.get(position);
        presenter.addFriend(friend);
    }
}
