package com.leyou.game.activity.friend;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.friend.ContactsListAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.dao.Friend;
import com.leyou.game.event.FriendAddEvent;
import com.leyou.game.event.FriendDeleteEvent;
import com.leyou.game.event.MainTabEvent;
import com.leyou.game.event.NewFriendAddEvent;
import com.leyou.game.ipresenter.friend.IContactsActivity;
import com.leyou.game.presenter.friend.ContactsPresenter;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.PinyinComparator;
import com.leyou.game.widget.CustomSlideBar;
import com.leyou.game.widget.DragPointView;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Description : 好友通信录列表页面
 *
 * @author : rocky
 * @Create Time : 2017/8/5 上午10:51
 * @Modified By: rocky
 * @Modified Time : 2017/8/5 上午10:51
 */
public class FriendContactsActivity extends BaseActivity implements IContactsActivity, CustomSlideBar.OnTouchingLetterChangedListener {
    @BindView(R.id.tv_new_friend_tips_count)
    TextView tvNewFriendTipsCount;
    @BindView(R.id.re_contact)
    RecyclerView recycler;
    @BindView(R.id.tv_letter_show)
    TextView tvLetterShow;
    @BindView(R.id.slideBar)
    CustomSlideBar slideBar;
    private List<Friend> friendList = new ArrayList<>();
    private ContactsListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean isMove;
    private int mIndex = 0;
    private ContactsPresenter presenter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newFriend(NewFriendAddEvent event) {
        if (event.isFlag()) {
            presenter.getConfirmFriend();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void friendAddRefresh(FriendAddEvent event) {
        presenter.getConfirmFriend();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteFriend(FriendDeleteEvent event) {
        boolean result = event.isResult();
        if (result) {
            initData();
        }
    }


    @PermissionYes(19542)
    private void getPermissionYes(List<String> grantedPermissions) {
        startActivity(new Intent(this, PhoneContactsActivity.class));
    }

    @PermissionNo(19542)
    private void getPermissionNo(List<String> deniedPermissions) {
    }

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_friend_contacts;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        slideBar.setOnTouchingLetterChangedListener(this);
        slideBar.setTextView(tvLetterShow);
        adapter = new ContactsListAdapter(this, friendList);
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
        friendList = DBUtil.getInstance(FriendContactsActivity.this).queryMyFriendList();
        Collections.sort(friendList, new PinyinComparator());
        adapter.setMyFriendAdapter(friendList);
    }

    @Override
    public void initPresenter() {
        presenter = new ContactsPresenter(this, this);
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

    @OnClick({R.id.iv_order_pay_back, R.id.tv_new_friend_tips_count, R.id.ll_friend_contacts, R.id.ll_friend_new_friend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_order_pay_back:
                finishCurrentActivity();
                break;
            case R.id.ll_friend_contacts:
                List<String> permissionList = new ArrayList<>();
                permissionList.add(Manifest.permission.READ_CONTACTS);
                if (AndPermission.hasPermission(this, permissionList)) {
                    startActivity(new Intent(this, PhoneContactsActivity.class));
                } else {
                    if (!AndPermission.hasAlwaysDeniedPermission(this, permissionList)) {
                        AndPermission.with(this).requestCode(19542).permission(Manifest.permission.READ_CONTACTS).callback(this).start();
                    } else {
                        startActivity(new Intent(this, PhoneContactsActivity.class));
                    }
                }
                break;
            case R.id.ll_friend_new_friend:
                startActivity(new Intent(this, NewFriendActivity.class));
                break;
            case R.id.tv_new_friend_tips_count:
                finishCurrentActivity();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
        adapter.setMyFriendAdapter(friendList);
        presenter.getMyFriends();
        presenter.getConfirmFriend();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setNewFriendCount(int count) {
        if (count > 0) {
            tvNewFriendTipsCount.setVisibility(View.VISIBLE);
            tvNewFriendTipsCount.setText(count + "");
        } else {
            tvNewFriendTipsCount.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMyFriendNet(List<Friend> list) {
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                DBUtil.getInstance(this).updateMyFriend(list.get(i));
            }
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 2000);
    }
}
