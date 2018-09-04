package com.leyou.game.presenter.friend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.leyou.game.Constants;
import com.leyou.game.bean.ContactMap;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.friend.IPhoneContacts;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.SPUtil;
import com.leyou.game.util.api.FriendApi;

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
    private List<Friend> friendList;

    public PhoneContactsActivityPresenter(Context context, IPhoneContacts iPhoneContacts) {
        this.context = context;
        this.iPhoneContacts = iPhoneContacts;
        friendList = new ArrayList<>();
    }

    public void updateFriendList(final List<Friend> friends) {
        friendList.clear();
        friendList.addAll(friends);
        final ContactMap contactMap = new ContactMap();
        contactMap.map = friends;

        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).updateFriendList(contactMap), new Observer<ResultArray<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                iPhoneContacts.dismissedLoading();
                iPhoneContacts.showMessageToast("联系人同步失败");
                iPhoneContacts.showPhoneContactsList(friends);
            }

            @Override
            public void onNext(ResultArray<Friend> friendResultArray) {
                if (friendResultArray.result == 1) {
                    SPUtil.setLong(context, SPUtil.UPDATE_CONTACTS, "currentTime", System.currentTimeMillis());
                    List<Friend> friendList = friendResultArray.data;
                    iPhoneContacts.showNetContactSList(friendList);
                    DBUtil.getInstance(context).updateFriendList(friendList);
                } else {
                    iPhoneContacts.showPhoneContactsList(friends);
                }
            }
        });
    }

    public void invite(final Friend friend) {
        inviteSMS(friend);
    }

    /**
     * 短信邀请
     *
     * @param friend
     */
    private void inviteSMS(Friend friend) {
        Uri uri = Uri.parse("smsto:" + friend.getPhone());
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
        sendIntent.putExtra("sms_body", "【熊猫玩玩】您的好朋友发现一款好玩的App，邀请你一起来赢大奖，有机会获取iPhone Plus 7 128G一部呦，点击->t.cn/R9j6YtE获取最新应用，邀请新用户还有钻石赠送呦～");
        context.startActivity(sendIntent);
    }

    public void addFriend(final Friend friend, final int position, final List<Friend> friends) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).inviteFriend(friend.getPhone()), new Observer<Result>() {
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
                    friend.setSource(1);
                    DBUtil.getInstance(context).updateFriend(friend);
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
