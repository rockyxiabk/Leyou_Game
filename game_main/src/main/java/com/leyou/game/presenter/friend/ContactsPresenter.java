package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.UserData;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.friend.IContactsActivity;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.FriendApi;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/5 上午11:23
 * @Modified Time : 2017/8/5 上午11:23
 */
public class ContactsPresenter {

    private Context context;
    private IContactsActivity iContactsActivity;

    public ContactsPresenter(Context context, IContactsActivity iContactsActivity) {
        this.context = context;
        this.iContactsActivity = iContactsActivity;
        getConfirmFriend();
    }

    public void getConfirmFriend() {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getConfirmFriend(), new Observer<ResultArray<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iContactsActivity.setNewFriendCount(0);
            }

            @Override
            public void onNext(ResultArray<Friend> contactBeanResultArray) {
                if (contactBeanResultArray.result == 1) {
                    List<Friend> data = contactBeanResultArray.data;
                    iContactsActivity.setNewFriendCount(data.size());
                } else {
                    iContactsActivity.setNewFriendCount(0);
                }
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
                iContactsActivity.setNewFriendCount(0);
            }

            @Override
            public void onNext(ResultArray<Friend> contactBeanResultArray) {
                if (contactBeanResultArray.result == 1) {
                    List<Friend> data = contactBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iContactsActivity.showMyFriendNet(data);
                    }
                }
            }
        });
    }
}
