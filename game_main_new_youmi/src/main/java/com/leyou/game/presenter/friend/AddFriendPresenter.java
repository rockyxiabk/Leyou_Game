package com.leyou.game.presenter.friend;

import android.content.Context;
import android.util.Log;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.friend.IAddFriend;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.newapi.CrowdApi;
import com.leyou.game.util.newapi.FriendApi;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/12/6 下午5:55
 * @Modified Time : 2017/12/6 下午5:55
 */
public class AddFriendPresenter {
    private static final String TAG = "AddFriendPresenter";

    private final Context context;
    private final IAddFriend iAddFriend;

    public AddFriendPresenter(Context context, IAddFriend iAddFriend) {
        this.context = context;
        this.iAddFriend = iAddFriend;
    }


    public void getRecommendFriendList() {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getRecFriendList(), new Observer<ResultArray<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.toString());
            }

            @Override
            public void onNext(ResultArray<Friend> friendResultArray) {
                if (friendResultArray.result == 1) {
                    iAddFriend.showRecommendFriend(friendResultArray.data);
                } else {

                }
            }
        });
    }

    public void getRecommendCrowdList() {
        HttpUtil.subscribe(HttpUtil.createApi(CrowdApi.class, Constants.URL).getRecGroupList(), new Observer<ResultArray<Crowd>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.toString());
            }

            @Override
            public void onNext(ResultArray<Crowd> crowdResultArray) {
                if (crowdResultArray.result == 1) {
                    iAddFriend.showRecommendCrowd(crowdResultArray.data);
                } else {
                }
            }
        });
    }

    public void addFriend(final Friend friend) {
        iAddFriend.showLoading();
        iAddFriend.changeLoadingDes("好友申请中...");
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).addFriend(friend.getPhone()), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.toString());
                ToastUtils.showToastShort("添加好友失败");
                iAddFriend.dismissedLoading();
            }

            @Override
            public void onNext(Result result) {
                iAddFriend.dismissedLoading();
                if (result.result == 1) {
                    ToastUtils.showToastShort("等待好友确认");
                    getRecommendFriendList();
                } else {
                    ToastUtils.showToastShort("添加好友失败");
                }
            }
        });
    }
}
