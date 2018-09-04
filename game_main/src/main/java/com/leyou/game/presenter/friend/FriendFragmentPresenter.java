package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.GameApplication;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.UserData;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.friend.IFriendFragment;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.api.FriendApi;
import com.leyou.game.util.api.UserApi;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
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
            GameApplication.connect(UserData.getInstance().getToken());
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
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).checkUnReadMessage(), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iFriendFragment.setMessageCount(0);
            }

            @Override
            public void onNext(Result result) {
                iFriendFragment.setMessageCount(result.result);
            }
        });
    }

    public void getMyFriends() {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getMyFriends(), new Observer<ResultArray<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ResultArray<Friend> contactBeanResultArray) {
                if (contactBeanResultArray.result == 1) {
                    List<Friend> data = contactBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            DBUtil.getInstance(context).updateFriend(data.get(i));
                        }
                    }
                }
            }
        });
    }

    public void getNewFriend() {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getConfirmFriend(), new Observer<ResultArray<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iFriendFragment.setNewFriendCount(0);
            }

            @Override
            public void onNext(ResultArray<Friend> contactBeanResultArray) {
                if (contactBeanResultArray.result == 1) {
                    List<Friend> data = contactBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            Friend friend = data.get(i);
                            friend.setSource(Friend.FRIEND);
                            DBUtil.getInstance(context).updateFriend(friend);
                        }
                        iFriendFragment.setNewFriendCount(data.size());
                    } else {
                        iFriendFragment.setNewFriendCount(0);
                    }
                } else {
                    iFriendFragment.setNewFriendCount(0);
                }
            }
        });
    }
}
