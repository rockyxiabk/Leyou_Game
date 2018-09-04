package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.GameBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.friend.IFriendDetail;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.api.FriendApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/7 下午2:46
 * @Modified Time : 2017/8/7 下午2:46
 */
public class FriendDetailActivityPresenter {

    private static final String TAG = "FriendDetailActivityPresenter";
    private Context context;
    private IFriendDetail iFriendDetail;
    private String userId;

    public FriendDetailActivityPresenter(Context context, IFriendDetail iFriendDetail, String userId) {
        this.context = context;
        this.iFriendDetail = iFriendDetail;
        this.userId = userId;
        getFriendInfo(userId);
        getUserPlayedGame(userId);
    }

    private void getFriendInfo(String userId) {
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
                        iFriendDetail.setFriendInfo(data);
                    }
                } else {

                }
            }
        });
    }

    public void setRemarkName(final String userId, final String remarkName) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).editRemark(remarkName, userId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    Friend friend = DBUtil.getInstance(context).queryFriendByUserId(userId);
                    friend.setRemarkName(remarkName);
                    iFriendDetail.setFriendInfo(friend);
                }
            }
        });
    }

    private void getUserPlayedGame(String userId) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getFriendPlayedGames(userId), new Observer<ResultArray<GameBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iFriendDetail.setGameList(false, null);
            }

            @Override
            public void onNext(ResultArray<GameBean> gameBeanResultArray) {

                if (gameBeanResultArray.result == 1) {
                    List<GameBean> list = gameBeanResultArray.data;
                    if (null != list && list.size() > 0) {
                        iFriendDetail.setGameList(true, list);
                    } else {
                        iFriendDetail.setGameList(false, null);
                    }
                } else {
                    iFriendDetail.setGameList(false, null);
                }
            }
        });
    }

    public void addFriend(final Friend friend) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).inviteFriend(friend.getPhone()), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iFriendDetail.showMessageToast("添加好友失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iFriendDetail.showMessageToast("等待好友确认");
                    friend.setStatus(Friend.ADDING_WAITING_CONFIRM);
                    friend.setSource(Friend.FRIEND);
                    DBUtil.getInstance(context).updateFriend(friend);
                    iFriendDetail.setFriendInfo(friend);
                } else {
                    iFriendDetail.showMessageToast("添加好友失败");
                }
            }
        });
    }

    public void agreeAddFriend(final Friend friend, int type) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).confirm(friend.getUserId(), type), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iFriendDetail.showMessageToast("好友添加失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iFriendDetail.showMessageToast("好友添加成功");
                    friend.setStatus(Friend.FRIEND);
                    friend.setSource(Friend.FRIEND);
                    DBUtil.getInstance(context).updateFriend(friend);
                    iFriendDetail.setFriendInfo(friend);
                } else {
                    iFriendDetail.showMessageToast("好友添加失败");
                }
            }
        });
    }

    public void deleteFriend(final String userId) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).deleteFriend(userId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                iFriendDetail.showDeleteFriendResult(false, userId);
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iFriendDetail.showDeleteFriendResult(true, userId);
                } else {
                    iFriendDetail.showDeleteFriendResult(false, userId);
                }
            }
        });
    }
}
