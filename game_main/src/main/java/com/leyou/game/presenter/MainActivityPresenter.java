package com.leyou.game.presenter;

import android.content.Context;
import android.util.Log;

import com.leyou.game.Constants;
import com.leyou.game.activity.MainActivity;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.UpdateAppBean;
import com.leyou.game.bean.UserData;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.IMainActivity;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.api.AppApi;
import com.leyou.game.util.api.FriendApi;
import com.leyou.game.util.api.TreasureApi;
import com.leyou.game.util.api.UserApi;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/5/31 下午5:15
 * @Modified Time : 2017/5/31 下午5:15
 */
public class MainActivityPresenter {
    private static final String TAG = "MainActivityPresenter";
    private IMainActivity iMainActivity;
    private Context context;

    public MainActivityPresenter(IMainActivity iMainActivity, Context context) {
        this.context = context;
        this.iMainActivity = iMainActivity;
    }

    public void checkVersion() {
        HttpUtil.subscribe(HttpUtil.createApi(AppApi.class, Constants.URL).checkUpgrade(Constants.getVerCode()), new Observer<Result<UpdateAppBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onNext(Result<UpdateAppBean> updateAppBeanResult) {
                if (updateAppBeanResult.result == 1) {
                    UpdateAppBean data = updateAppBeanResult.data;
                    if (null != data) {
                        if (Constants.getVerCode() < data.versionCode) {
                            iMainActivity.showNewVersionView(data);
                        } else {
                            iMainActivity.clearDownloadFile(true);
                        }
                    } else {
                        iMainActivity.clearDownloadFile(true);
                    }
                } else {
                    iMainActivity.clearDownloadFile(true);
                }
            }
        });
    }

    public void clearConversationUnReadStatus() {
        Conversation.ConversationType[] mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.PUBLIC_SERVICE,
                Conversation.ConversationType.APP_PUBLIC_SERVICE,
                Conversation.ConversationType.SYSTEM
        };
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null && conversations.size() > 0) {
                    for (Conversation c : conversations) {
                        RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {

            }
        }, mConversationsTypes);
    }

    public void checkUnReadMessage() {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).checkUnReadMessage(), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iMainActivity.setMessageCount(0);
                getNewFriend(0);
            }

            @Override
            public void onNext(Result result) {
                getNewFriend(result.result);
            }
        });
    }

    private void getNewFriend(final int count) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getConfirmFriend(), new Observer<ResultArray<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iMainActivity.setMessageCount(0 + count);
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
                        iMainActivity.setMessageCount(data.size() + count);
                    } else {
                        iMainActivity.setMessageCount(0 + count);
                    }
                } else {
                    iMainActivity.setMessageCount(0 + count);
                }
            }
        });
    }
}
