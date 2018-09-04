package com.leyou.game.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.friend.FriendChoseAdapter;
import com.leyou.game.adapter.friend.FriendCreateCrowdAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.dao.Friend;
import com.leyou.game.event.CrowdEvent;
import com.leyou.game.ipresenter.friend.IAddCrowdMember;
import com.leyou.game.presenter.friend.AddCrowdMemberPresenter;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.PinyinComparator;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.CustomSlideBar;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 邀请好友进入群聊
 *
 * @author : rocky
 * @Create Time : 2017/8/31 下午6:00
 * @Modified By: rocky
 * @Modified Time : 2017/8/31 下午6:00
 */

public class AddCrowdMemberActivity extends BaseActivity implements IAddCrowdMember, CustomSlideBar.OnTouchingLetterChangedListener, FriendCreateCrowdAdapter.onMapSelectorListener {

    private static final String TAG = "AddCrowdMemberActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.recycler_choose)
    RecyclerView recyclerChoose;
    @BindView(R.id.re_contact)
    RecyclerView recycler;
    @BindView(R.id.tv_letter_show)
    TextView tvLetterShow;
    @BindView(R.id.slideBar)
    CustomSlideBar slideBar;
    private String crowdId;
    private AddCrowdMemberPresenter presenter;
    private LinearLayoutManager layoutManager;
    private FriendCreateCrowdAdapter adapter;
    private FriendChoseAdapter choseAdapter;
    private List<Friend> choseContactsList = new ArrayList<>();
    private List<Friend> friendList = new ArrayList<>();
    private boolean isMove;
    private int mIndex = 0;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_add_crowd_member;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        crowdId = intent.getStringExtra("crowdId");

        slideBar.setOnTouchingLetterChangedListener(this);
        slideBar.setTextView(tvLetterShow);

        choseAdapter = new FriendChoseAdapter(this, choseContactsList);
        recyclerChoose.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerChoose.setAdapter(choseAdapter);


        adapter = new FriendCreateCrowdAdapter(this, friendList);
        adapter.setOnMapSelectorListener(this);
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

    @Override
    public void initPresenter() {
        presenter = new AddCrowdMemberPresenter(this, this, crowdId);
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.tv_confirm:
                presenter.inviteToCrowd(choseContactsList);
                break;
        }
    }

    @Override
    public void showCrowdMemberAdapter(List<Friend> data) {
        friendList.clear();
        data.remove(data.size() - 1);
        if (data.get(data.size() - 1).getUserId().length() < 2) {
            data.remove(data.size() - 1);
        }
        List<Friend> myFriendList = DBUtil.getInstance(this).queryMyFriendList();
        List<Friend> currentList = new ArrayList<>();
        currentList.addAll(myFriendList);
        for (int i = 0; i < myFriendList.size(); i++) {
            Friend friend = myFriendList.get(i);
            for (int j = 0; j < data.size(); j++) {
                Friend crowdFriend = data.get(j);
                if (crowdFriend.getUserId().equals(friend.getUserId())) {
                    LogUtil.d(TAG, "---remove:" + friend.toString());
                    currentList.remove(friend);
                }
            }
        }
        friendList.addAll(currentList);
        adapter.setContactsAdapter(currentList);
    }

    @Override
    public void showInviteResult(boolean result) {
        if (result) {
            EventBus.getDefault().post(new CrowdEvent(false, true));
            showMessageToast("邀请成功");
        } else {
            showMessageToast("添加失败");
        }
        finishCurrentActivity();
    }

    @Override
    public void showMessageToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }

    @Override
    public void showSelectorContacts(Map<Integer, Boolean> map) {
        choseContactsList.clear();
        for (int i = 0; i < friendList.size(); i++) {
            boolean aBoolean = map.get(i);
            if (aBoolean) {
                Friend contactBean = friendList.get(i);
                choseContactsList.add(contactBean);
            }
        }
        choseAdapter.setChoseAdapter(choseContactsList);
        tvConfirm.setText("确认(" + choseContactsList.size() + ")");
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        int position = adapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
            mIndex = position;
            moveToPosition(position);
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
