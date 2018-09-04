package com.leyou.game.presenter.friend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.dao.Friend;
import com.leyou.game.dao.PhoneContact;
import com.leyou.game.ipresenter.friend.IPhoneContacts;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.SPUtil;
import com.leyou.game.util.newapi.FriendApi;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/14 下午2:18
 * @Modified Time : 2017/8/14 下午2:18
 */
public class PhoneContactsActivityPresenter {
    private static final String TAG = "PhoneContactsActivityPresenter";

    private final Context context;
    private final IPhoneContacts iPhoneContacts;
    private List<PhoneContact> friendList;

    public PhoneContactsActivityPresenter(Context context, IPhoneContacts iPhoneContacts) {
        this.context = context;
        this.iPhoneContacts = iPhoneContacts;
        friendList = new ArrayList<>();
    }

    public void updateFriendList(final List<PhoneContact> friends) {
        friendList.clear();
        friendList.addAll(friends);
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).syncPhoneBook(friends), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                iPhoneContacts.dismissedLoading();
                iPhoneContacts.showPhoneContactsList(friendList);
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    getMyBookPhoneList();
                } else {
                    iPhoneContacts.dismissedLoading();
                    iPhoneContacts.showPhoneContactsList(friendList);
                }
            }
        });
    }

    public void getMyBookPhoneList() {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).updateFriendList(friendList), new Observer<ResultArray<PhoneContact>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                e.printStackTrace();
                iPhoneContacts.dismissedLoading();
                iPhoneContacts.showPhoneContactsList(DBUtil.getInstance(context).queryPhoneContactList());
            }

            @Override
            public void onNext(ResultArray<PhoneContact> friendResultArray) {
                if (friendResultArray.result == 1) {
                    SPUtil.setLong(context, SPUtil.UPDATE_CONTACTS, "currentTime", System.currentTimeMillis());
                    List<PhoneContact> friendList = friendResultArray.data;
                    DBUtil.getInstance(context).updatePhoneContactList(friendList);
                    iPhoneContacts.showNetContactSList(friendList);
                } else {
                    iPhoneContacts.showPhoneContactsList(DBUtil.getInstance(context).queryPhoneContactList());
                }
            }
        });
    }

    public void invite(final PhoneContact friend) {
        inviteSMS(friend);
    }

    /**
     * 短信邀请
     *
     * @param friend
     */
    private void inviteSMS(PhoneContact friend) {
        Uri uri = Uri.parse("smsto:" + friend.getPhone());
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
        sendIntent.putExtra("sms_body", "【熊猫玩玩】我刚刚发现一款好玩的App，来和我一起来玩啊，不止能提现，还能赢得千元大奖，点击->http://t.cn/R9j6YtE");
        context.startActivity(sendIntent);
    }

    public void addFriend(final PhoneContact friend, final int position, final List<PhoneContact> friends) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).addFriend(friend.getPhone()), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iPhoneContacts.showMessageToast("添加好友失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iPhoneContacts.showMessageToast("等待好友确认");
                    friend.setStatus(Friend.ADDING_WAITING_CONFIRM);
                    DBUtil.getInstance(context).updatePhoneContact(friend);
                    friends.remove(position);
                    friends.add(position, friend);
                    iPhoneContacts.showPhoneContactsList(friends);
                } else {
                    iPhoneContacts.showMessageToast("添加好友失败");
                }
            }
        });
    }
}
