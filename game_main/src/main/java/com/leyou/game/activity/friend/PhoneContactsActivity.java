package com.leyou.game.activity.friend;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.friend.PhoneContactsAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.friend.IPhoneContacts;
import com.leyou.game.presenter.friend.PhoneContactsActivityPresenter;
import com.leyou.game.util.ContactsUtil;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.PinyinComparator;
import com.leyou.game.util.SPUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.CustomSlideBar;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.service.notification.Condition.SCHEME;

/**
 * Description : 手机通信录列表 可以邀请好友
 *
 * @author : rocky
 * @Create Time : 2017/8/11 上午11:41
 * @Modified By: rocky
 * @Modified Time : 2017/8/11 上午11:41
 */
public class PhoneContactsActivity extends BaseActivity implements IPhoneContacts, CustomSlideBar.OnTouchingLetterChangedListener, CustomItemClickListener {

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
    @BindView(R.id.re_no_authority)
    RelativeLayout reNoAuthority;
    @BindView(R.id.btn_setting)
    Button btnSetting;
    private List<Friend> friends = new ArrayList<>();
    private PhoneContactsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean isMove;
    private int mIndex = 0;
    private PhoneContactsActivityPresenter presenter;
    private LoadingProgressDialog loadingProgressDialog;

    @PermissionYes(69542)
    private void getPermissionYes(List<String> grantedPermissions) {
        initData();
    }

    @PermissionNo(69542)
    private void getPermissionNo(List<String> deniedPermissions) {
    }

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_phone_contacts;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        slideBar.setOnTouchingLetterChangedListener(this);
        slideBar.setTextView(tvLetterShow);

        adapter = new PhoneContactsAdapter(this, friends);
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

    @Override
    public void initPresenter() {
        presenter = new PhoneContactsActivityPresenter(this, this);
        showLoading();
    }

    private void initData() {
        friends.clear();
        if (AndPermission.hasPermission(this, Manifest.permission.READ_CONTACTS)) {
            long currentTime = SPUtil.getLong(this, SPUtil.UPDATE_CONTACTS, "currentTime");
            int days = 0;
            if (currentTime > 0) {
                days = DataUtil.distance_days(System.currentTimeMillis() + "", currentTime + "");
            } else {
                days = 1;
            }
            if (days >= 1) {//超过一天 没有更新
                changeLoadingDes("正在导入联系人数据...");
                friends = ContactsUtil.getContractsList(this);
                Collections.sort(friends, new PinyinComparator());
                if (null != friends && friends.size() > 0) {
                    changeLoadingDes("同步联系人数据...");
                    presenter.updateFriendList(friends);
                } else {
                    showListOrErrorView(false);
                    dismissedLoading();
                }
            } else {//当前已经更新过
                friends = DBUtil.getInstance(this).queryFriendList();
                Collections.sort(friends, new PinyinComparator());
                if (null != friends && friends.size() > 0) {
                    changeLoadingDes("读取联系人数据...");
                    adapter.updateAdapter(friends);
                } else {
                    showListOrErrorView(false);
                }
                dismissedLoading();
            }
        } else {
            showListOrErrorView(false);
            dismissedLoading();
        }
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

    @OnClick({R.id.iv_order_pay_back, R.id.btn_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_order_pay_back:
                finishCurrentActivity();
                break;
            case R.id.btn_setting:
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", Constants.getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 695);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 695) {
            initData();
        }
    }

    @Override
    public void showPhoneContactsList(List<Friend> friends) {
        showListOrErrorView(true);
        this.friends = friends;
        adapter.updateAdapter(this.friends);
    }

    @Override
    public void showNetContactSList(final List<Friend> friend) {
        showListOrErrorView(true);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Collections.sort(friend, new PinyinComparator());
                List<Friend> currentList = new ArrayList<>();
                currentList.clear();
                currentList.addAll(friend);
                friends.clear();
                friends.addAll(currentList);
                adapter.updateAdapter(friends);
                dismissedLoading();
                showMessageToast("联系人同步成功");
            }
        }, 10);
    }

    @Override
    public void showListOrErrorView(boolean isAuthority) {
        if (isAuthority) {
            reNoAuthority.setVisibility(View.GONE);
            frameList.setVisibility(View.VISIBLE);
        } else {
            reNoAuthority.setVisibility(View.VISIBLE);
            frameList.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoading() {
        loadingProgressDialog = new LoadingProgressDialog(this, false);
        loadingProgressDialog.show();
    }

    @Override
    public void changeLoadingDes(String des) {
        if (null != loadingProgressDialog)
            loadingProgressDialog.setLoadingText(des);
    }

    @Override
    public void dismissedLoading() {
        if (null != loadingProgressDialog)
            loadingProgressDialog.dismiss();
    }

    @Override
    public void showMessageToast(String message) {
        ToastUtils.showToastShort(message);
    }

    @Override
    public void onItemClick(View view, int position) {
        Friend friend = friends.get(position);
        if (friend.getStatus() == Friend.NO_SYSTEM) {
            presenter.invite(friend);
        } else if (friend.getStatus() == Friend.SYSTEM_NO_FRIEND) {
            presenter.addFriend(friend, position, friends);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }
}
