package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.dao.Friend;
import com.leyou.game.dao.PhoneContact;
import com.leyou.game.event.FriendAddEvent;
import com.leyou.game.ipresenter.friend.INewFriend;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.FriendApi;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/12 上午11:18
 * @Modified Time : 2017/8/12 上午11:18
 */
public class NewFriendPresenter {
    private static final String TAG = "NewFriendPresenter";

    private Context context;
    private INewFriend iNewFriend;

    public NewFriendPresenter(Context context, INewFriend iNewFriend) {
        this.context = context;
        this.iNewFriend = iNewFriend;
        getAddFriend();
    }

    private void getAddFriend() {
        iNewFriend.showLoading();
        iNewFriend.changeLoadingDes(context.getResources().getString(R.string.data_loading));
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getNewFriend(1, 100), new Observer<ResultArray<PhoneContact>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                e.printStackTrace();
                iNewFriend.setAddFriendList(false, null);
                iNewFriend.dismissedLoading();
            }

            @Override
            public void onNext(ResultArray<PhoneContact> friendArray) {
                iNewFriend.dismissedLoading();
                if (friendArray.result == 1) {
                    List<PhoneContact> friends = friendArray.data;
                    if (null != friends && friends.size() > 0) {
                        DBUtil.getInstance(context).updatePhoneContactList(friends);
                        iNewFriend.setAddFriendList(true, null);
                    } else {
                        iNewFriend.setAddFriendList(false, null);
                    }
                } else {
                    iNewFriend.setAddFriendList(false, null);
                }
            }
        });
    }

    public void agreeAddFriend(final PhoneContact friend, int type) {
        iNewFriend.showLoading();
        iNewFriend.changeLoadingDes("通过验证中...");
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).confirm(friend.getUserId(), type), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iNewFriend.dismissedLoading();
                iNewFriend.showMessageToast("好友添加失败");
            }

            @Override
            public void onNext(Result result) {
                iNewFriend.dismissedLoading();
                if (result.result == 1) {
                    iNewFriend.showMessageToast("好友添加成功");
                    iNewFriend.newFriendAddResult(true);
                    friend.setStatus(Friend.FRIEND);
                    DBUtil.getInstance(context).updatePhoneContact(friend);
                    getAddFriend();
                    EventBus.getDefault().post(new FriendAddEvent(true, friend.getUserId()));
                } else {
                    iNewFriend.showMessageToast(result.msg);
                }
            }
        });
    }
}
