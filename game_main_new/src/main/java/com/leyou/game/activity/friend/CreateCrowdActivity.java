package com.leyou.game.activity.friend;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.friend.FriendChoseAdapter;
import com.leyou.game.adapter.friend.FriendCreateCrowdAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.friend.ICreateCrowd;
import com.leyou.game.presenter.friend.CreateCrowdPresenter;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.PinyinComparator;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.CustomSlideBar;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.RongGridView;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

/**
 * Description : 创建群聊
 *
 * @author : rocky
 * @Create Time : 2017/8/30 下午8:35
 * @Modified By: rocky
 * @Modified Time : 2017/8/30 下午8:35
 */


public class CreateCrowdActivity extends BaseActivity implements ICreateCrowd, CustomSlideBar.OnTouchingLetterChangedListener, FriendCreateCrowdAdapter.onMapSelectorListener {

    @BindView(R.id.iv_order_pay_back)
    ImageView ivOrderPayBack;
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
    private List<Friend> choseContactsList = new ArrayList<>();
    private FriendChoseAdapter choseAdapter;
    private List<Friend> friendList = new ArrayList<>();
    private FriendCreateCrowdAdapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean isMove;
    private int mIndex = 0;
    private CreateCrowdPresenter presenter;
    private Crowd crowdInfo;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_create_crowd;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        crowdInfo = getIntent().getParcelableExtra("crowdInfo");
        slideBar.setOnTouchingLetterChangedListener(this);
        friendList = DBUtil.getInstance(this).queryMyFriendList();
        Collections.sort(friendList, new PinyinComparator());
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
        presenter = new CreateCrowdPresenter(this, this);
    }

    @OnClick({R.id.iv_order_pay_back, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_order_pay_back:
                finishCurrentActivity();
                break;
            case R.id.tv_confirm:
                if (null != choseContactsList && choseContactsList.size() >= 2) {
                    presenter.createCrowd(choseContactsList,crowdInfo);
                } else {
                    showMessageToast("至少选择2个群聊成员");
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
        adapter.setContactsAdapter(friendList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
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
    public void showCreateCrowdResult(boolean result, Crowd crowd) {
        if (result) {
            DBUtil.getInstance(this).insertCrowd(crowd);
            RongIM.getInstance().refreshGroupInfoCache(new Group(crowd.getGroupId(), crowd.getName(), Uri.parse(crowd.getHeadImgUrl())));
            RongIM.getInstance().startConversation(this, Conversation.ConversationType.GROUP, crowd.getGroupId(), crowd.getName());
            finishCurrentActivity();
        }
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
}
