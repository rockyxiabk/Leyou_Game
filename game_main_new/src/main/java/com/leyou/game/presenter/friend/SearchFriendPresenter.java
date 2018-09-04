package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.dao.PhoneContact;
import com.leyou.game.ipresenter.friend.ISearchFriend;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.CrowdApi;
import com.leyou.game.util.newapi.FriendApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/12/6 下午6:03
 * @Modified Time : 2017/12/6 下午6:03
 */
public class SearchFriendPresenter {
    private static final String TAG = "SearchFriendPresenter";

    private Context context;
    private ISearchFriend iSearchFriend;

    public SearchFriendPresenter(Context context, ISearchFriend iSearchFriend) {
        this.context = context;
        this.iSearchFriend = iSearchFriend;
    }

    public void searchFriendByIdNo(String idNo) {
        iSearchFriend.showLoading();
        iSearchFriend.changeLoadingDes("查询中...");
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getFriendInfoByIdNo(idNo), new Observer<ResultArray<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.toString());
                iSearchFriend.showNullView();
            }

            @Override
            public void onNext(ResultArray<Friend> friendResultArray) {
                if (friendResultArray.result == 1) {
                    List<Friend> data = friendResultArray.data;
                    iSearchFriend.showSearchFriend(data);
                } else {
                    iSearchFriend.showNullView();
                }
            }
        });
    }

    public void searchCrowdByIdNo(String crowdIdNo) {
        iSearchFriend.showLoading();
        iSearchFriend.changeLoadingDes("查询中...");
        HttpUtil.subscribe(HttpUtil.createApi(CrowdApi.class, Constants.URL).getGroupInfoByGroupNo(crowdIdNo), new Observer<ResultArray<Crowd>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.toString());
                iSearchFriend.showNullView();
            }

            @Override
            public void onNext(ResultArray<Crowd> crowdResultArray) {
                if (crowdResultArray.result == 1) {
                    List<Crowd> data = crowdResultArray.data;
                    iSearchFriend.showSearchCrowd(data);
                } else {
                    iSearchFriend.showNullView();
                }
            }
        });
    }

    public void addFriend(Friend friend) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).addFriend(friend.getPhone()), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iSearchFriend.showMessageToast("添加好友失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iSearchFriend.showMessageToast("等待好友确认");
                } else {
                    iSearchFriend.showMessageToast(result.msg);
                }
            }
        });
    }
}
