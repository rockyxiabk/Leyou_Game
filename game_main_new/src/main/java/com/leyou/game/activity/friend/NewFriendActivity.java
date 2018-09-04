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
import com.leyou.game.adapter.friend.NewContactsAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.dao.Friend;
import com.leyou.game.dao.PhoneContact;
import com.leyou.game.event.NewFriendAddEvent;
import com.leyou.game.ipresenter.friend.INewFriend;
import com.leyou.game.presenter.friend.NewFriendPresenter;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.PhoneContactComparator;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 添加新的朋友页面 新的朋友
 *
 * @author : rocky
 * @Create Time : 2017/7/25 下午6:27
 * @Modified By: rocky
 * @Modified Time : 2017/7/25 下午6:27
 */
public class NewFriendActivity extends BaseActivity implements INewFriend, CustomItemClickListener {
    @BindView(R.id.iv_order_pay_back)
    ImageView ivOrderPayBack;
    @BindView(R.id.frame_list)
    FrameLayout frameList;
    @BindView(R.id.re_contact)
    RecyclerView recycler;
    @BindView(R.id.re_no_new_friend)
    RelativeLayout reNoNewFriend;
    private List<PhoneContact> contractsList = new ArrayList<>();
    private NewContactsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean isMove;
    private int mIndex = 0;
    private NewFriendPresenter presenter;
    private LoadingProgressDialog progressDialog;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_new_friend;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        adapter = new NewContactsAdapter(this, contractsList);
        adapter.setCustomItemClickListener(this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isMove) {
                    isMove = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = mIndex - layoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < recyclerView.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = recyclerView.getChildAt(n).getTop();
                        //最后的移动
                        recyclerView.scrollBy(0, top);
                    }
                }
            }
        });
        recycler.setAdapter(adapter);
    }

    private void initData() {
        contractsList.clear();
        List<PhoneContact> list = DBUtil.getInstance(this).queryNewFriendList();
        if (null != list && list.size() > 0) {
            contractsList.addAll(list);
            Collections.sort(contractsList, new PhoneContactComparator());
            if (null != contractsList && contractsList.size() > 0) {
                frameList.setVisibility(View.VISIBLE);
                reNoNewFriend.setVisibility(View.GONE);
                adapter.setAdapterData(contractsList);
            } else {
                reNoNewFriend.setVisibility(View.VISIBLE);
                frameList.setVisibility(View.GONE);
            }
        } else {
            reNoNewFriend.setVisibility(View.VISIBLE);
            frameList.setVisibility(View.GONE);
        }
    }

    @Override
    public void initPresenter() {
        presenter = new NewFriendPresenter(this, this);
    }

    @OnClick(R.id.iv_order_pay_back)
    public void onViewClicked() {
        finishCurrentActivity();
    }

    @Override
    public void setAddFriendList(boolean hasData, List<Friend> data) {
        if (hasData) {
            initData();
        } else {
            reNoNewFriend.setVisibility(View.VISIBLE);
            frameList.setVisibility(View.GONE);
        }
    }

    @Override
    public void newFriendAddResult(boolean result) {
        if (result) {
            EventBus.getDefault().post(new NewFriendAddEvent(true));
        }
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
        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        PhoneContact friend = contractsList.get(position);
        presenter.agreeAddFriend(friend, 1);
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
}
