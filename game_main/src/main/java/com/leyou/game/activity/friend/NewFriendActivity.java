package com.leyou.game.activity.friend;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.friend.NewContactsAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.dao.Friend;
import com.leyou.game.event.NewFriendAddEvent;
import com.leyou.game.ipresenter.friend.INewFriend;
import com.leyou.game.presenter.friend.NewFriendPresenter;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.PinyinComparator;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.CustomSlideBar;
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
public class NewFriendActivity extends BaseActivity implements INewFriend, CustomItemClickListener, CustomSlideBar.OnTouchingLetterChangedListener {
    @BindView(R.id.iv_order_pay_back)
    ImageView ivOrderPayBack;
    @BindView(R.id.frame_list)
    FrameLayout frameList;
    @BindView(R.id.re_contact)
    RecyclerView recycler;
    @BindView(R.id.tv_letter_show)
    TextView tvLetterShow;
    @BindView(R.id.slideBar)
    CustomSlideBar slideBar;
    @BindView(R.id.re_no_new_friend)
    RelativeLayout reNoNewFriend;
    private List<Friend> contractsList = new ArrayList<>();
    private NewContactsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean isMove;
    private int mIndex = 0;
    private NewFriendPresenter presenter;

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

        slideBar.setOnTouchingLetterChangedListener(this);
        slideBar.setTextView(tvLetterShow);

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

        initData();
    }

    private void initData() {
        contractsList = DBUtil.getInstance(this).queryNewFriendList();
        Collections.sort(contractsList, new PinyinComparator());
        if (null != contractsList && contractsList.size() > 0) {
            frameList.setVisibility(View.VISIBLE);
            reNoNewFriend.setVisibility(View.GONE);
        } else {
            reNoNewFriend.setVisibility(View.VISIBLE);
            frameList.setVisibility(View.GONE);
        }
        adapter.setAdapterData(contractsList);
    }

    @Override
    public void initPresenter() {
        presenter = new NewFriendPresenter(this, this);
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        if (!TextUtils.isEmpty(s)) {
            int position = adapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                mIndex = position;
                moveToPosition(position);
            }
        }
    }

    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            recycler.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = recycler.getChildAt(n - firstItem).getTop();
            recycler.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            recycler.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            isMove = true;
        }
    }

    @OnClick(R.id.iv_order_pay_back)
    public void onViewClicked() {
        finishCurrentActivity();
    }

    @Override
    public void setAddFriendList(boolean hasData, List<Friend> data) {
        if (hasData) {
            this.contractsList = data;
            for (int i = 0; i < data.size(); i++) {
                Friend friend = data.get(i);
                friend.setSource(1);
                DBUtil.getInstance(this).updateFriend(friend);
            }
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 200);
    }

    @Override
    public void newFriendAddResult(boolean result) {
        if (result) {
            EventBus.getDefault().post(new NewFriendAddEvent(true));
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
        Friend friend = contractsList.get(position);
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
