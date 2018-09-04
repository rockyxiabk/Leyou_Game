package com.leyou.game.activity.friend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.event.FriendDeleteEvent;
import com.leyou.game.ipresenter.friend.IConversationList;
import com.leyou.game.presenter.friend.ConversationListPresenter;
import com.leyou.game.rong.RongCloudEvent;
import com.leyou.game.util.DBUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * Description : 聊天页面
 *
 * @author : rocky
 * @Create Time : 2017/7/25 下午2:59
 * @Modified By: rocky
 * @Modified Time : 2017/7/25 下午2:59
 */
public class ConversationActivity extends FragmentActivity implements IConversationList, RongIM.UserInfoProvider, RongIM.GroupInfoProvider {

    private static final String TAG = "ConversationActivity";
    @BindView(R.id.iv_conversation_back)
    ImageView ivConversationBack;
    @BindView(R.id.tv_conversation_title)
    TextView tvConversationTitle;
    @BindView(R.id.iv_message_not_tips)
    ImageView ivMessageNotTips;
    @BindView(R.id.iv_conversation_detail)
    ImageView ivConversationDetail;
    private String targetId;
    private String title;
    private String conversation;
    private int conversationType = 1;//1:私聊 3：群聊 2：讨论组 4：聊天室 6: 系统消息
    private Conversation.ConversationType conversationType_EMUN;//融云自带会话类型
    private Friend friend;
    private Crowd crowd;
    private ConversationListPresenter presenter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteFriend(FriendDeleteEvent event) {
        boolean result = event.isResult();
        if (result) {
            this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Uri uri = getIntent().getData();
        conversation = uri.getPath();
        targetId = uri.getQueryParameter("targetId").toString();
        title = uri.getQueryParameter("title").toString();
        tvConversationTitle.setText(title);

        initPresenter();
        initTitle();
    }

    private void initTitle() {
        if (conversation.contains(Conversation.ConversationType.PRIVATE.getName().toLowerCase())) {
            ivConversationDetail.setImageResource(R.mipmap.icon_friend_private);
            conversationType = 1;
            conversationType_EMUN = Conversation.ConversationType.PRIVATE;
            friend = DBUtil.getInstance(this).queryFriendByUserId(targetId);
            if (null != friend) {
                title = !TextUtils.isEmpty(friend.getRemarkName()) ? friend.getRemarkName() : friend.getNickName();
            } else {
                title = getUserInfo(targetId).getName();
            }
            tvConversationTitle.setText(title);
            presenter.getFriendInfo(targetId);
        } else if (conversation.contains(Conversation.ConversationType.DISCUSSION.getName().toLowerCase())) {
            ivConversationDetail.setImageResource(R.mipmap.icon_friend_crowd);
            conversationType = 2;
            conversationType_EMUN = Conversation.ConversationType.DISCUSSION;
        } else if (conversation.contains(Conversation.ConversationType.GROUP.getName().toLowerCase())) {
            ivConversationDetail.setImageResource(R.mipmap.icon_friend_crowd);
            conversationType = 3;
            conversationType_EMUN = Conversation.ConversationType.GROUP;
            crowd = DBUtil.getInstance(this).queryCrowdByCrowdId(targetId);
            if (null != crowd) {
                tvConversationTitle.setText(crowd.getCrowdName());
            } else {
                Group groupInfo = getGroupInfo(targetId);
                tvConversationTitle.setText(groupInfo.getName());
            }
            presenter.getCrowdInfo(targetId);
        } else if (conversation.contains(Conversation.ConversationType.CHATROOM.getName().toLowerCase())) {
            ivConversationDetail.setImageResource(R.mipmap.icon_friend_crowd);
            conversationType = 4;
            conversationType_EMUN = Conversation.ConversationType.CHATROOM;
        } else if (conversation.contains(Conversation.ConversationType.SYSTEM.getName().toLowerCase())) {
            ivConversationDetail.setImageResource(R.mipmap.icon_friend_private);
            conversationType = 6;
            conversationType_EMUN = Conversation.ConversationType.SYSTEM;
        }
        RongCloudEvent.getInstance().putActivity(10090, this);
    }

    private void initPresenter() {
        presenter = new ConversationListPresenter(this, this);
    }

    @OnClick({R.id.iv_conversation_back, R.id.iv_conversation_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_conversation_back:
                finish();
                break;
            case R.id.iv_conversation_detail:
                switch (conversationType) {
                    case 1:
                        Intent intentPrivate = new Intent(this, FriendDetailActivity.class);
                        intentPrivate.putExtra("userId", targetId);
                        intentPrivate.putExtra("userInfo", getUserInfo(targetId));
                        startActivity(intentPrivate);
                        break;
                    case 2:
                        break;
                    case 3:
                        Intent crowdIntent = new Intent(this, CrowdDetailActivity.class);
                        crowdIntent.putExtra("targetId", targetId);
                        if (null != this.crowd) {
                            crowdIntent.putExtra("crowd", this.crowd);
                        }
                        startActivity(crowdIntent);
                        break;
                    case 4:
                        break;
                    case 6:
                        break;
                }
                break;
        }
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
    public void setFriendInfo(Friend friend) {
        DBUtil.getInstance(this).updateFriend(friend);
        this.friend = friend;
        tvConversationTitle.setText(!TextUtils.isEmpty(friend.getRemarkName()) ? friend.getRemarkName() : friend.getNickName());
        ivMessageNotTips.setVisibility(View.GONE);
    }

    @Override
    public void setCrowdInfo(boolean flag, Crowd crowd) {
        if (flag) {
            DBUtil.getInstance(this).updateCrowd(crowd);
            this.crowd = crowd;
            if (crowd.getCrowdName().length() > 10) {
                tvConversationTitle.setText("群聊(" + crowd.getCrowdNumbers() + ")");
            } else {
                tvConversationTitle.setText(crowd.getCrowdName());
            }
            if (crowd.getListenMessage() == 1) {
                ivMessageNotTips.setVisibility(View.VISIBLE);
            } else {
                ivMessageNotTips.setVisibility(View.GONE);
            }
        } else {
            ivMessageNotTips.setVisibility(View.GONE);
            ivConversationDetail.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
        initTitle();
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
    public Group getGroupInfo(String crowdId) {
        Group groupInfo = RongUserInfoManager.getInstance().getGroupInfo(crowdId);
        if (null == groupInfo) {
            groupInfo = new Group(crowdId, "群聊", Uri.parse("http://www.baidu.com"));
        }
        return groupInfo;
    }
}
