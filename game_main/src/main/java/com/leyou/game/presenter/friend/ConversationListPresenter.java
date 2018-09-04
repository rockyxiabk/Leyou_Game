package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.friend.IConversationList;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.api.FriendApi;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/26 下午6:07
 * @Modified Time : 2017/8/26 下午6:07
 */
public class ConversationListPresenter {
    private static final String TAG = "ConversationListPresenter";
    private Context context;
    private IConversationList iConversationList;

    public ConversationListPresenter(Context context, IConversationList iConversationList) {
        this.context = context;
        this.iConversationList = iConversationList;
    }

    public void getFriendInfo(String userId) {
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
                    if (null != data) {
                        iConversationList.setFriendInfo(data);
                    }
                } else {

                }
            }
        });
    }

    public void getCrowdInfo(String crowdId) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getCrowdInfo(crowdId), new Observer<Result<Crowd>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                iConversationList.setCrowdInfo(false, null);
            }

            @Override
            public void onNext(Result<Crowd> crowdBeanResult) {
                if (crowdBeanResult.result == 1) {
                    Crowd crowd = crowdBeanResult.data;
                    if (null != crowd) {
                        iConversationList.setCrowdInfo(true, crowd);
                    } else {
                        iConversationList.setCrowdInfo(false, null);
                    }
                } else {
                    iConversationList.setCrowdInfo(false, null);
                }
            }
        });
    }
}
