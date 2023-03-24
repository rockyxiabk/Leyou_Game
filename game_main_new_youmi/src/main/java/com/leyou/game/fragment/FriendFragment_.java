package com.leyou.game.fragment;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.activity.friend.AddFriendActivity;
import com.leyou.game.activity.friend.CreateCrowdActivity;
import com.leyou.game.activity.friend.CreateCrowdInfoActivity;
import com.leyou.game.activity.friend.FriendContactsActivity;
import com.leyou.game.activity.friend.FriendRankActivity;
import com.leyou.game.activity.friend.MessageActivity;
import com.leyou.game.activity.friend.NewCrowdApplyActivity;
import com.leyou.game.activity.friend.NewFriendActivity;
import com.leyou.game.activity.friend.PhoneContactsActivity;
import com.leyou.game.activity.friend.SearchFriendActivity;
import com.leyou.game.activity.mine.FeedBackActivity;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.CustomOnLongClickListener;
import com.leyou.game.adapter.friend.FriendConversationAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.UserData;
import com.leyou.game.dao.Friend;
import com.leyou.game.event.CrowdEvent;
import com.leyou.game.event.FriendAddEvent;
import com.leyou.game.event.FriendDeleteEvent;
import com.leyou.game.event.FriendFragmentRefreshEvent;
import com.leyou.game.ipresenter.friend.IFriendFragment;
import com.leyou.game.presenter.friend.FriendFragmentPresenter;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.MobClickUtil;
import com.leyou.game.util.SPUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.CustomDialog;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.dialog.LogInDialog;
import com.leyou.game.widget.dialog.friend.AddPopup;
import com.leyou.game.widget.dialog.friend.ConversationSettingDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * Description : 新版页面
 *
 * @author : rocky
 * @Create Time : 2017/10/23 下午5:46
 * @Modified By: rocky
 * @Modified Time : 2017/10/23 下午5:46
 */
public class FriendFragment_ extends BaseFragment implements IFriendFragment, CustomItemClickListener, CustomOnLongClickListener, IUnReadMessageObserver, RongIM.UserInfoProvider, RongIM.GroupInfoProvider {

    private static final String TAG = "FriendFragment_";

    @BindView(R.id.ivbtn_friend_address_book)
    ImageButton ivbtnFriendAddressBook;
    @BindView(R.id.tv_friend_tips)
    TextView tvTips;
    @BindView(R.id.iv_friend_create_crowd)
    ImageView ivFriendCreateCrowd;
    @BindView(R.id.ll_friend_rank)
    LinearLayout llFriendRank;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.ll_hot_point)
    LinearLayout llHotPoint;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.recycler_chat)
    RecyclerView reContact;
    @BindView(R.id.cd_chat_null)
    CardView cdChatNull;
    @BindView(R.id.view_bg)
    View viewBg;
    Unbinder unbinder;
    private Conversation.ConversationType[] conversationTypes;
    private FriendFragmentPresenter presenter;
    private FriendConversationAdapter adapter;
    private List<Conversation> list = new ArrayList<>();
    private LogInDialog logInDialog;
    private LoadingProgressDialog loadingProgressDialog;
    private boolean isShowAddPopup = false;
    private AddPopup addPopup;

    public FriendFragment_() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void friendRefresh(FriendFragmentRefreshEvent event) {
        presenter.checkUnReadMessage();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void friendAddRefresh(FriendAddEvent event) {
        presenter.getNewFriend();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteFriend(FriendDeleteEvent event) {
        boolean result = event.isResult();
        if (result) {
            String userId = event.getUserId();
            RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, userId, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    LogUtil.d(TAG, "----delete  friend:" + aBoolean);
                    presenter.getConversationListAll();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtil.e(TAG, "----delete friend:" + errorCode.getValue() + "---msg:" + errorCode.getMessage());
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCrowdInfo(CrowdEvent event) {
        if (event.isDeleteResult()) {
            presenter.getConversationListAll();
        }
        if (event.isInviteResult()) {
            presenter.getConversationListAll();
        }
    }

    @PermissionYes(19541)
    private void getPermissionYes(List<String> grantedPermissions) {
        startActivity(new Intent(context, PhoneContactsActivity.class));
    }

    @PermissionNo(19541)
    private void getPermissionNo(List<String> deniedPermissions) {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (UserData.getInstance().isLogIn()) {
                userLogInEd();
                if (null != presenter) {
                    presenter.checkUnReadMessage();
                    presenter.getNewFriend();
                    showPhoneContactsDialog();
                }
            } else {
                setConversationListNull(true);
                userUnLogIn();
            }
        } else {
            hideFriendAddView();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_friend_fragment_;
    }

    @Override
    protected void initView(View rootView, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);

        conversationTypes = new Conversation.ConversationType[]{
                Conversation.ConversationType.NONE,
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.DISCUSSION,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.CHATROOM,
                Conversation.ConversationType.CUSTOMER_SERVICE,
                Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.APP_PUBLIC_SERVICE,
                Conversation.ConversationType.PUBLIC_SERVICE,
                Conversation.ConversationType.PUSH_SERVICE
        };
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);

        adapter = new FriendConversationAdapter(context, list);
        adapter.setCustomOnItemListener(this);
        adapter.setCustomOnLongClickListener(this);
        reContact.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        reContact.setAdapter(adapter);
        reContact.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initPresenter() {
        presenter = new FriendFragmentPresenter(context, this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideFriendAddView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.ivbtn_friend_address_book, R.id.iv_friend_create_crowd,
            R.id.ll_friend_rank, R.id.ll_message, R.id.ll_hot_point, R.id.recycler_chat, R.id.view_bg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivbtn_friend_address_book:
                hideFriendAddView();
                context.startActivity(new Intent(context, FriendContactsActivity.class));
                break;
            case R.id.iv_friend_create_crowd:
//                /**
//                 * 启动客户服聊天界面。
//                 *
//                 * @param context           应用上下文。
//                 * @param customerServiceId 要与之聊天的客服 Id。
//                 * @param title             聊天的标题，如果传入空值，则默认显示与之聊天的客服名称。
//                 * @param customServiceInfo 当前使用客服者的用户信息。{@link io.rong.imlib.model.CSCustomServiceInfo}
//                 */
//                //首先需要构造使用客服者的用户信息
//                CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
//                CSCustomServiceInfo csInfo = csBuilder.nickName(context.getResources().getString(R.string.app_name)).build();
//                RongIM.getInstance().startCustomerServiceChat(getActivity(), Constants.RONGYUN_ID, "在线客服", csInfo);
//                context.startActivity(new Intent(context, CreateCrowdActivity.class));
                if (!isShowAddPopup) {
                    showFriendAddView();
                } else {
                    hideFriendAddView();
                }
                break;
            case R.id.ll_friend_rank:
                MobClickUtil.mobEvent(context, MobClickUtil.EVENT_FRIEND_RANK);
                context.startActivity(new Intent(context, FriendRankActivity.class));
                break;
            case R.id.ll_message:
                context.startActivity(new Intent(context, MessageActivity.class));
                break;
            case R.id.ll_hot_point:
                context.startActivity(new Intent(context, FeedBackActivity.class));
                break;
            case R.id.view_bg:
                hideFriendAddView();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        String currentTitle = "";
        switch (list.get(position).getConversationType()) {
            case NONE:
                break;
            case PRIVATE:
                Friend friend = DBUtil.getInstance(context).queryFriendByUserId(list.get(position).getTargetId());
                if (null == friend) {
                    friend = new Friend();
                    UserInfo userInfo = getUserInfo(list.get(position).getTargetId());
                    friend.setNickname(userInfo.getName());
                    friend.setHeadImgUrl(userInfo.getPortraitUri().toString());
                    friend.setUserId(userInfo.getUserId());
                }
                currentTitle = !TextUtils.isEmpty(friend.getComment()) ? friend.getComment() : friend.getNickname();
                RongIM.getInstance().startConversation(context,
                        list.get(position).getConversationType(),
                        list.get(position).getTargetId(),
                        currentTitle);
                break;
            case DISCUSSION:
                break;
            case GROUP:
                Group groupInfo = getGroupInfo(list.get(position).getTargetId());
                currentTitle = groupInfo.getName();
                RongIM.getInstance().startConversation(context,
                        list.get(position).getConversationType(),
                        list.get(position).getTargetId(),
                        currentTitle);
                break;
            case CHATROOM:
                break;
            case SYSTEM:
                Conversation conversation = list.get(position);
                String senderUserId = conversation.getSenderUserId();
                if ("admin".equalsIgnoreCase(senderUserId)) {
                    context.startActivity(new Intent(context, NewCrowdApplyActivity.class));
                } else {
                    context.startActivity(new Intent(context, NewFriendActivity.class));
                }
                break;
        }
        presenter.clearUnReadMessageStatus(list.get(position));
    }

    @Override
    public void onItemLongClick(View view, int position) {
        final ConversationSettingDialog conversationSettingDialog = new ConversationSettingDialog(context, list.get(position));
        conversationSettingDialog.setConversationListener(new ConversationSettingDialog.ConversationSettingListener() {
            @Override
            public void success() {
                conversationSettingDialog.dismiss();
                presenter.getConversationListAll();
            }

            @Override
            public void failed() {
                conversationSettingDialog.dismiss();

            }
        });
        conversationSettingDialog.show();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.layout_add_friend:
                hideFriendAddView();
                Intent intent1 = new Intent(context, SearchFriendActivity.class);
                intent1.putExtra("type", 1);
                context.startActivity(intent1);
                break;
            case R.id.layout_add_crowd:
                hideFriendAddView();
                Intent intent2 = new Intent(context, SearchFriendActivity.class);
                intent2.putExtra("type", 2);
                context.startActivity(intent2);
                break;
            case R.id.layout_create_crowd:
                hideFriendAddView();
                Intent intent3 = new Intent(context, CreateCrowdInfoActivity.class);
                intent3.putExtra("type", 1);
                context.startActivity(intent3);
                break;
        }
    }

    @Override
    public void showLoading() {
        loadingProgressDialog = new LoadingProgressDialog(context, false);
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
    public void showFriendAddView() {
        isShowAddPopup = true;
        addPopup = new AddPopup(context, this);
        addPopup.setOutsideTouchable(true);
        addPopup.showAsDropDown(ivFriendCreateCrowd);
        viewBg.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFriendAddView() {
        if (isShowAddPopup && null != addPopup) {
            isShowAddPopup = false;
            addPopup.dismiss();
            viewBg.setVisibility(View.GONE);
        }
    }

    @Override
    public void setNewFriendCount(int count) {
        if (count > 0) {
            tvTips.setVisibility(View.GONE);
            tvTips.setText("" + count + "");
        } else {
            tvTips.setVisibility(View.GONE);
        }
    }

    @Override
    public void setConversationList(List<Conversation> data) {
        adapter.setAdapter(data);
    }

    @Override
    public void setConversationListNull(boolean flag) {
        if (null != nestedScrollView && null != cdChatNull)
            if (flag) {
                nestedScrollView.setVisibility(View.GONE);
                cdChatNull.setVisibility(View.VISIBLE);
            } else {
                nestedScrollView.setVisibility(View.VISIBLE);
                cdChatNull.setVisibility(View.GONE);
            }
    }

    @Override
    public Group getGroupInfo(String groupId) {
        Group groupInfo = RongUserInfoManager.getInstance().getGroupInfo(groupId);
        if (null == groupInfo) {
            groupInfo = new Group(groupId, "群聊", Uri.parse("http://www.baidu.com"));
            presenter.getCrowdDetail(groupId);
        }
        return groupInfo;
    }

    @Override
    public UserInfo getUserInfo(String userId) {
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(userId);
        if (null == userInfo) {
            userInfo = new UserInfo(userId, "陌生人", Uri.parse("http://www.baidu.com"));
            presenter.getFriendInfo(userId);
        }
        return userInfo;
    }

    @Override
    public void onCountChanged(int i) {
        presenter.getConversationListAll();
    }

    private void showPhoneContactsDialog() {
        final CustomDialog customDialog = new CustomDialog(context, "导入联系人",
                "\t\t是否导入手机通讯录，邀请你的朋友一起加入战斗，更有机会在好友排行中夺取神秘奖品呦～",
                getString(R.string.cancel), getString(R.string.confirm));
        customDialog.setCustomListener(new CustomDialog.CustomListener() {
            @Override
            public void cancel() {
                SPUtil.setBoolean(context, SPUtil.SETTING, "isInputFriend", true);
            }

            @Override
            public void confirm() {
                SPUtil.setBoolean(context, SPUtil.SETTING, "isInputFriend", true);
                List<String> permissionList = new ArrayList<>();
                permissionList.add(Manifest.permission.READ_CONTACTS);
                if (AndPermission.hasPermission(context, permissionList)) {
                    startActivity(new Intent(context, PhoneContactsActivity.class));
                } else {
                    AndPermission.with(context).requestCode(19541).permission(Manifest.permission.READ_CONTACTS).callback(this).start();
                }
            }
        });
        boolean isInputFriend = SPUtil.getBoolean(context, SPUtil.SETTING, "isInputFriend");
        if (!isInputFriend) {
            customDialog.show();
        }
    }

    public void userUnLogIn() {
//        logInDialog = new LogInDialog(context, false);
//        logInDialog.show();
    }

    public void userLogInEd() {
        if (null != logInDialog) {
            logInDialog.dismiss();
        }
    }
}
