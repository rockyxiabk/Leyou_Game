package com.leyou.game.rong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.leyou.game.Constants;
import com.leyou.game.activity.friend.FriendDetailActivity;
import com.leyou.game.activity.friend.NewFriendActivity;
import com.leyou.game.bean.Result;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.dao.PhoneContact;
import com.leyou.game.event.CrowdEvent;
import com.leyou.game.listener.MyConnectionStatusListener;
import com.leyou.game.listener.MyReceiverMessageListener;
import com.leyou.game.rong.message.DiamondRedPacketMessage;
import com.leyou.game.rong.message.InviteJoinWolfKillMessage;
import com.leyou.game.rong.message.ShareGameMessage;
import com.leyou.game.rong.plugin.MyCustomExtensionModule;
import com.leyou.game.rong.provider.DiamondRedPacketMessageItemProvider;
import com.leyou.game.rong.provider.InviteJoinWolfKillMessageItemProvider;
import com.leyou.game.rong.provider.ShareGameMessageItemProvider;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.CrowdApi;
import com.leyou.game.util.newapi.FriendApi;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;
import rx.Observer;

/**
 * Description : com.leyou.game
 *
 * @author : rocky
 * @Create Time : 2017/7/28 下午2:49
 * @Modified Time : 2017/7/28 下午2:49
 */
public class RongCloudEvent implements RongIM.ConversationListBehaviorListener,
        RongIM.UserInfoProvider,
        RongIM.GroupInfoProvider,
        RongIM.GroupUserInfoProvider,
        RongIMClient.ConnectionStatusListener,
        RongIM.IGroupMembersProvider {

    private static final String TAG = "RongCloudEvent";

    private static RongCloudEvent instance;
    private static ArrayList<Activity> mActivities;
    private static Map<Integer, Activity> mapActivitys;
    private final Context context;

    public static RongCloudEvent getInstance() {
        return instance;
    }

    public RongCloudEvent(Context context) {
        this.context = context;
        mActivities = new ArrayList<>();
        mapActivitys = new HashMap<>();
        initListener();
        initMessageTemple();
        intInputProvider();
    }

    public static void init(Context context) {
        if (null == instance) {
            synchronized (RongCloudEvent.class) {
                if (null == instance) {
                    instance = new RongCloudEvent(context);
                }
            }
        }
    }

    private void initListener() {
        RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
        RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
        RongIM.setConnectionStatusListener(new MyConnectionStatusListener());//监听链接状态
        RongIM.setOnReceiveMessageListener(new MyReceiverMessageListener());//接收消息监听器的实现，所有接收到的消息、通知、状态都经由此处设置的监听器处理
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                //在这里处理你想要跳转的activity
                Intent in = new Intent(context, FriendDetailActivity.class);
                switch (conversationType) {
                    case PRIVATE:
                        in.putExtra("type", FriendDetailActivity.PRIVATE);
                        break;
                    case GROUP:
                        in.putExtra("type", FriendDetailActivity.GROUP);
                        break;
                }
                in.putExtra("userInfo", userInfo);
                in.putExtra("userId", userInfo.getUserId());
                context.startActivity(in);
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                //点击消息处理事件，示例代码展示了如何获得消息内容
                if (message.getContent() instanceof LocationMessage) {
                    Conversation.ConversationType conversationType = message.getConversationType();
                    String targetId = message.getTargetId();
                    RongIM.getInstance().startConversation(context, conversationType, targetId, "");
                } else if (message.getContent() instanceof RichContentMessage) {
                    RichContentMessage mRichContentMessage = (RichContentMessage) message.getContent();
                    LogUtil.d("Begavior", "extra:" + mRichContentMessage.getExtra());
                }
                LogUtil.d("Begavior", message.getObjectName() + ":" + message.getMessageId());
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
    }

    private void initMessageTemple() {
        RongIM.registerMessageType(InviteJoinWolfKillMessage.class);
        RongIM.getInstance().registerMessageTemplate(new InviteJoinWolfKillMessageItemProvider());
        RongIM.registerMessageType(ShareGameMessage.class);
        RongIM.getInstance().registerMessageTemplate(new ShareGameMessageItemProvider());
        RongIM.registerMessageType(DiamondRedPacketMessage.class);
        RongIM.getInstance().registerMessageTemplate(new DiamondRedPacketMessageItemProvider());
    }

    private void intInputProvider() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new MyCustomExtensionModule());
            }
        }
    }

    /**
     * ConversationListBehaviorListener start
     */
    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        MessageContent messageContent = uiConversation.getMessageContent();
        if (messageContent instanceof ContactNotificationMessage) {
            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
            if (contactNotificationMessage.getOperation().equals("AcceptResponse")) {
                // 被加方同意请求后
                if (contactNotificationMessage.getExtra() != null) {
//                    ContactNotificationMessageData bean = null;
//                    try {
//                        bean = JsonManager.jsonToBean(contactNotificationMessage.getExtra(), ContactNotificationMessageData.class);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    RongIM.getInstance().startConversation(context, uiConversation.getConversationType(), uiConversation.getConversationSenderId(), uiConversation.getUIConversationTitle());

                }
            } else {
                context.startActivity(new Intent(context, NewFriendActivity.class));
            }
            return true;
        }
        return false;
    }

    /**
     * ConversationListBehaviorListener end
     */
    @Override
    public Group getGroupInfo(String groupId) {
        Group groupInfo = RongUserInfoManager.getInstance().getGroupInfo(groupId);
        if (null == groupInfo) {
            getCrowdInfo(groupId);
        }
        return null;
    }

    @Override
    public GroupUserInfo getGroupUserInfo(String s, String s1) {
        return null;
    }

    @Override
    public UserInfo getUserInfo(String userId) {
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(userId);
        if (null == userInfo) {
            getFriendInfo(userId);
        }
        return null;
    }

    @Override
    public void getGroupMembers(String groupId, RongIM.IGroupMemberCallback iGroupMemberCallback) {
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {

    }

    public void pushActivity(Activity activity) {
        mActivities.add(activity);
    }

    public void putActivity(int index, Activity activity) {
        mapActivitys.put(index, activity);
    }

    public void removeActivity(int index) {
        if (mapActivitys.containsKey(index)) {
            mapActivitys.get(index).finish();
            mapActivitys.remove(index);
        }
    }

    public void popActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            activity.finish();
            mActivities.remove(activity);
        }
    }

    public void getFriendInfo(final String userId) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getFriendInfo(userId), new Observer<Result<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
            }

            @Override
            public void onNext(Result<Friend> friendResult) {
                if (friendResult.result == 1) {
                    Friend data = friendResult.data;
                    PhoneContact phoneContact = DBUtil.getInstance(context).queryPhoneContactByUserId(data.userId);
                    phoneContact.setStatus(data.getStatus());
                    DBUtil.getInstance(context).updatePhoneContact(phoneContact);
                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(userId, data.getNickname(), Uri.parse(data.getHeadImgUrl())));
                    EventBus.getDefault().post(new CrowdEvent(true));
                }
            }
        });
    }

    public void getCrowdInfo(final String crowdId) {
        HttpUtil.subscribe(HttpUtil.createApi(CrowdApi.class, Constants.URL).getGroupDetail(crowdId), new Observer<Result<Crowd>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
            }

            @Override
            public void onNext(Result<Crowd> crowdBeanResult) {
                if (crowdBeanResult.result == 1) {
                    Crowd data = crowdBeanResult.data;
                    RongIM.getInstance().refreshGroupInfoCache(new Group(crowdId, data.getName(), Uri.parse(data.getHeadImgUrl())));
                    EventBus.getDefault().post(new CrowdEvent(true));
                }
            }
        });
    }
}
