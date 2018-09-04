package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.friend.FriendExtBean;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.friend.IContactsActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.FriendApi;

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
    }

    public void getConfirmFriend() {
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
                    iContactsActivity.setNewFriendCount(friendResultArray.data.number);
                }
            }
        });
    }

    public void getMyFriends() {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getMyFriendList(), new Observer<ResultArray<Friend>>() {
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
