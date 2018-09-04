package com.leyou.game.presenter.friend;

import android.content.Context;
import android.view.View;

import com.leyou.game.Constants;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.game.GameRankBean;
import com.leyou.game.bean.win.WinGameAwardBean;
import com.leyou.game.ipresenter.friend.IFriendRankFragment;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.GameApi;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/7/26 上午10:06
 * @Modified Time : 2017/7/26 上午10:06
 */
public class FriendRankFragmentPresenter {
    private Context context;
    private IFriendRankFragment iFriendRankFragment;
    private String markId;
    private Subscription subscribe;
    Observer<ResultArray<GameRankBean>> observer = new Observer<ResultArray<GameRankBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iFriendRankFragment.showError(false);
        }

        @Override
        public void onNext(ResultArray<GameRankBean> gamePrize) {
            int result = gamePrize.result;
            if (0 != result) {
                List<GameRankBean> data = gamePrize.data;
                if (null != data && data.size() > 0) {
                    iFriendRankFragment.showData(data);
                } else {
                    iFriendRankFragment.showError(true);
                }
            } else {
                iFriendRankFragment.showError(true);
            }
        }
    };

    public FriendRankFragmentPresenter(Context context, IFriendRankFragment iFriendRankFragment, String uniqueMarkId) {
        this.context = context;
        this.iFriendRankFragment = iFriendRankFragment;
        this.markId = uniqueMarkId;
        getWinResultPersonList(uniqueMarkId);
        getDataList(uniqueMarkId);
    }

    public void getWinResultPersonList(String markId) {
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getGameRankFriendsList(markId), observer);
    }

    public void getDataList(String markId) {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getFriendGameBonus(markId), new Observer<ResultArray<WinGameAwardBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iFriendRankFragment.showError(true);
            }

            @Override
            public void onNext(ResultArray<WinGameAwardBean> winGameAwardBeanResultArray) {
                if (winGameAwardBeanResultArray.result == 1) {
                    List<WinGameAwardBean> data = winGameAwardBeanResultArray.data;
                    if (null != data) {
                        List<WinGameAwardBean> data1 = new ArrayList<>();
                        data1.add(data.get(0));
                        iFriendRankFragment.showPrizeData(data1);
                    } else {
                        iFriendRankFragment.showError(true);
                    }
                } else {
                    iFriendRankFragment.showError(true);
                }
            }
        });
    }

    /**
     * 解除订阅，停止数据请求
     */
    public void unSubscribe() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }
}
