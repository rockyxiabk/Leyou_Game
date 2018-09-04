package com.leyou.game.presenter.friend;

import android.content.Context;
import android.net.Uri;

import com.leyou.game.Constants;
import com.leyou.game.GameApplication;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.friend.FriendExtBean;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.dao.PhoneContact;
import com.leyou.game.ipresenter.friend.IFriendFragment;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.CrowdApi;
import com.leyou.game.util.newapi.FriendApi;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import rx.Observer;

/**
 * Description : com.leyou.game.fragment
 *
 * @author : rocky
 * @Create Time : 2017/7/22 下午2:19
 * @Modified Time : 2017/7/22 下午2:19
 */
public class FriendFragmentPresenter {
    private static final String TAG = "FriendFragmentPresenter";
    private Context context;
    private IFriendFragment iFriendFragment;
    private Conversation.ConversationType[] conversationTypes = new Conversation.ConversationType[]{
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

    public FriendFragmentPresenter(Context context, IFriendFragment iFriendFragment) {
        this.context = context;
        this.iFriendFragment = iFriendFragment;
        getMyFriends();
        getConversationListAll();
    }

    public void getConversationListAll() {
        LogUtil.d(TAG, "----" + UserData.getInstance().isRcIsConnected());
        if (UserData.getInstance().isRcIsConnected()) {
            RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                @Override
                public void onSuccess(List<Conversation> conversations) {
                    if (null != conversations && conversations.size() > 0) {
                        iFriendFragment.setConversationListNull(false);
                        iFriendFragment.setConversationList(conversations);
                    } else {
                        iFriendFragment.setConversationListNull(true);
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtil.d(TAG, "----errorCode:" + errorCode.getMessage() + "---" + errorCode.getValue());
                    iFriendFragment.setConversationListNull(true);
                }
            }, conversationTypes);
        } else {
            iFriendFragment.setConversationListNull(true);
            GameApplication.connect(UserData.getInstance().getRongToken());
        }
    }

    public void clearUnReadMessageStatus(Conversation conversation) {
        RongIM.getInstance().clearMessagesUnreadStatus(conversation.getConversationType(), conversation.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                getConversationListAll();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    public void checkUnReadMessage() {
    }

    public void getMyFriends() {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getMyFriendList(), new Observer<ResultArray<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ResultArray<Friend> contactBeanResultArray) {
                if (contactBeanResultArray.result == 1) {
                    List<Friend> data = contactBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            DBUtil.getInstance(context).updateMyFriend(data.get(i));
                        }
                    }
                }
            }
        });
    }

    public void getNewFriend() {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getWattingFriendNum(), new Observer<Result<FriendExtBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result<FriendExtBean> friendResultArray) {
                if (friendResultArray.result == 1) {
                    iFriendFragment.setNewFriendCount(friendResultArray.data.number);
                }
            }
        });
    }

    public void getFriendInfo(String userId) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getFriendInfo(userId), new Observer<Result<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Result<Friend> friendResult) {
                if (friendResult.result == 1) {
                    Friend data = friendResult.data;
                    if (null != data) {
                        DBUtil.getInstance(context).updateFriend(data);
                        DBUtil.getInstance(context).updatePhoneContact(new PhoneContact(System.currentTimeMillis(),
                                Integer.valueOf(data.status), data.userId,
                                data.headImgUrl, data.name, data.nickname, "", data.phone));
                        RongUserInfoManager.getInstance().setUserInfo(new UserInfo(data.getUserId(),
                                data.getNickname(), Uri.parse(data.getHeadImgUrl())));
                    }
                }
            }
        });
    }

    public void getCrowdDetail(String crowdId) {
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
                    Crowd crowd = crowdBeanResult.data;
                    if (null != crowd) {
                        RongIM.getInstance().refreshGroupInfoCache(new Group(crowd.getGroupId(), crowd.getName(),
                                Uri.parse(crowd.getHeadImgUrl())));
                    }
                }
            }
        });
    }
}
