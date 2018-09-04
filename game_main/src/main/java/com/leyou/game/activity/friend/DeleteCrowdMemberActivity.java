package com.leyou.game.activity.friend;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.leyou.game.ipresenter.friend.IDeleteCrowdMember;
import com.leyou.game.presenter.friend.DeleteCrowdMemberPresenter;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.PinyinComparator;
import com.leyou.game.util.PinyinUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.CustomSlideBar;
import com.leyou.game.widget.dialog.CustomDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeleteCrowdMemberActivity extends BaseActivity implements IDeleteCrowdMember, CustomSlideBar.OnTouchingLetterChangedListener, FriendCreateCrowdAdapter.onMapSelectorListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.recycler_choose)
    RecyclerView recyclerChoose;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_letter_show)
    TextView tvLetterShow;
    @BindView(R.id.slideBar)
    CustomSlideBar slideBar;
    private String crowdId;
    private FriendChoseAdapter choseAdapter;
    private FriendCreateCrowdAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<Friend> choseContactsList = new ArrayList<>();
    private List<Friend> friendList = new ArrayList<>();
    private boolean isMove;
    private int mIndex = 0;
    private DeleteCrowdMemberPresenter presenter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCrowdInfo(CrowdEvent event) {
        if (event.getDeleteCrowdMember() > 0) {
            finishCurrentActivity();
        }
    }

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_delete_crowd_member;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
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
        presenter = new DeleteCrowdMemberPresenter(this, this, crowdId);
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.tv_confirm:
                if (choseContactsList.size() > 0) {
                    final CustomDialog customDialog = new CustomDialog(this, getResources().getString(R.string.friend_delete), "确定要移除选择的群成员吗？", getResources().getString(R.string.cancel), getResources().getString(R.string.friend_remove_member));
                    customDialog.setCustomListener(new CustomDialog.CustomListener() {
                        @Override
                        public void cancel() {
                            customDialog.dismiss();
                        }

                        @Override
                        public void confirm() {
                            customDialog.dismiss();
                            presenter.deleteCrowdMember(choseContactsList);
                        }
                    });
                    customDialog.show();
                } else {
                    showMessageToast("选择要移除的群成员");
                }
                break;
        }
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
    public void showCrowdMemberAdapter(List<Friend> data) {
        friendList.clear();
        data.remove(data.size() - 1);
        data.remove(data.size() - 1);
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getUserId().equals(UserData.getInstance().getId())) {
                data.remove(i);
            }
        }
        for (int i = 0; i < data.size(); i++) {
            Friend friend = data.get(i);
            String pinyin = PinyinUtil.getPingYin(friend.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                friend.setPhoneNameLetter(sortString.toUpperCase());
            } else if (sortString.matches("[0-9]")) {
                friend.setPhoneNameLetter("*");
            } else {
                friend.setPhoneNameLetter("#");
            }
            friendList.add(friend);
        }
        Collections.sort(friendList, new PinyinComparator());
        adapter.setContactsAdapter(friendList);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
