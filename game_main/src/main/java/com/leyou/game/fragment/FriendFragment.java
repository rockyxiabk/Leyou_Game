package com.leyou.game.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.activity.MainActivity;
import com.leyou.game.activity.friend.CreateCrowdActivity;
import com.leyou.game.activity.friend.FriendContactsActivity;
import com.leyou.game.activity.friend.FriendRankActivity;
import com.leyou.game.activity.friend.MessageActivity;
import com.leyou.game.activity.friend.NewFriendActivity;
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
import com.leyou.game.rong.RongCloudEvent;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.widget.DragPointView;
import com.leyou.game.widget.dialog.ConversationSettingDialog;
import com.leyou.game.widget.dialog.LogInDialog;
import com.umeng.socialize.utils.Log;

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
 * Description : 好友页面
 *
 * @author : rocky
 * @Create Time : 2017/7/11 上午11:05
 * @Modified By: rocky
 * @Modified Time : 2017/7/11 上午11:05
 */
public class FriendFragment extends BaseFragment implements IFriendFragment, CustomItemClickListener, CustomOnLongClickListener, IUnReadMessageObserver, RongIM.UserInfoProvider, RongIM.GroupInfoProvider {

    private static final String TAG = "FriendFragment";
    @BindView(R.id.tv_to_crowd)
    TextView tvToCrowd;
    @BindView(R.id.ll_friend_message)
    LinearLayout llFriendMessage;
    @BindView(R.id.ll_friend_rank)
    LinearLayout llFriendRank;
    @BindView(R.id.ll_friend_contacts)
    LinearLayout llFriendContacts;
    @BindView(R.id.re_contact)
    RecyclerView reContact;
    Unbinder unbinder;
    @BindView(R.id.ll_conversation_null)
    LinearLayout llConversationNull;
    @BindView(R.id.tv_message_tips_count)
    DragPointView tvMessageTipsCount;
    @BindView(R.id.tv_friend_tips_count)
    DragPointView tvFriendTipsCount;
    private LogInDialog logInDialog;
    private FriendFragmentPresenter presenter;
    private List<Conversation> list = new ArrayList<>();
    private FriendConversationAdapter adapter;
    private Conversation.ConversationType[] conversationTypes;
    private int messageCount;
    private int newFriendCount;
    private MainActivity mainActivity;

    public FriendFragment() {
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = ((MainActivity) context);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && UserData.getInstance().isLogIn()) {
            userLogInEd();
            presenter.getConversationListAll();
            presenter.checkUnReadMessage();
            presenter.getNewFriend();
        } else if (getUserVisibleHint() && !UserData.getInstance().isLogIn()) {
            setConversationListNull(true);
            userUnLogIn();
        }
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_friend;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
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

    @OnClick({R.id.tv_message_tips_count, R.id.tv_to_crowd, R.id.ll_friend_message, R.id.ll_friend_rank, R.id.ll_friend_contacts, R.id.ll_conversation_null})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_to_crowd:
                context.startActivity(new Intent(context, CreateCrowdActivity.class));
                break;
            case R.id.ll_friend_message:
                context.startActivity(new Intent(context, MessageActivity.class));
                break;
            case R.id.ll_friend_rank:
                context.startActivity(new Intent(context, FriendRankActivity.class));
                break;
            case R.id.ll_friend_contacts:
                context.startActivity(new Intent(context, FriendContactsActivity.class));
                break;
            case R.id.ll_friend_new_friend:
                context.startActivity(new Intent(context, NewFriendActivity.class));
                break;
            case R.id.ll_conversation_null:
                presenter.getConversationListAll();
                break;
        }
    }

    public void userUnLogIn() {
        logInDialog = new LogInDialog(context, false);
        logInDialog.show();
    }

    public void userLogInEd() {
        if (null != logInDialog) {
            logInDialog.dismiss();
        }
    }

    @Override
    public void setMessageCount(int count) {
        if (count > 0) {
            tvMessageTipsCount.setVisibility(View.VISIBLE);
            tvMessageTipsCount.setText(count + "");
            messageCount = count;
        } else {
            tvMessageTipsCount.setVisibility(View.GONE);
            messageCount = 0;
        }
    }

    @Override
    public void setNewFriendCount(int count) {
        if (count > 0) {
            tvFriendTipsCount.setVisibility(View.VISIBLE);
            tvFriendTipsCount.setText(count + "");
            newFriendCount = count;
        } else {
            tvFriendTipsCount.setVisibility(View.GONE);
            newFriendCount = 0;
        }
    }

    @Override
    public void onCountChanged(int i) {
        presenter.getConversationListAll();
    }

    @Override
    public void setConversationList(List<Conversation> data) {
        adapter.setAdapter(data);
    }

    @Override
    public void setConversationListNull(boolean flag) {
        if (flag) {
            reContact.setVisibility(View.GONE);
            llConversationNull.setVisibility(View.VISIBLE);
        } else {
            reContact.setVisibility(View.VISIBLE);
            llConversationNull.setVisibility(View.GONE);
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
                    friend.setNickName(userInfo.getName());
                    friend.setPictureUrl(userInfo.getPortraitUri().toString());
                    friend.setUserId(userInfo.getUserId());
                }
                currentTitle = !TextUtils.isEmpty(friend.getRemarkName()) ? friend.getRemarkName() : friend.getNickName();
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
                context.startActivity(new Intent(context, NewFriendActivity.class));
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public UserInfo getUserInfo(String userId) {
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(userId);
        if (null == userInfo) {
            userInfo = new UserInfo(userId, "陌生人", Uri.parse("http://www.baidu.com"));
        }
        return userInfo;
    }

    @Override
    public Group getGroupInfo(String groupId) {
        Group groupInfo = RongUserInfoManager.getInstance().getGroupInfo(groupId);
        if (null == groupInfo) {
            groupInfo = new Group(groupId, "群聊", Uri.parse("http://www/baidu.com"));
        }
        return groupInfo;
    }
}
