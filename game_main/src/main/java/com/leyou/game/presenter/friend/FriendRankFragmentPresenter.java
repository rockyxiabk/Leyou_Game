package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.AwardPersonInfo;
import com.leyou.game.bean.GameRankBean;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.ipresenter.friend.IFriendRankFragment;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.FriendApi;
import com.leyou.game.util.api.WinAwardApi;

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
    private long markId;
    private Subscription subscribe;
    Observer<ResultArray<GameRankBean>> observer = new Observer<ResultArray<GameRankBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iFriendRankFragment.showError();
        }

        @Override
        public void onNext(ResultArray<GameRankBean> gamePrize) {
            if (null != gamePrize) {
                int result = gamePrize.result;
                if (0 != result) {
                    List<GameRankBean> data = gamePrize.data;
                    iFriendRankFragment.showData(data);
                } else {
                    iFriendRankFragment.showError();
                }
            } else {
                iFriendRankFragment.showError();
            }
        }
    };

    public FriendRankFragmentPresenter(Context context, IFriendRankFragment iFriendRankFragment, long markId) {
        this.context = context;
        this.iFriendRankFragment = iFriendRankFragment;
        this.markId = markId;
        getWinResultPersonList(markId);
    }

    public void getWinResultPersonList(long markId) {
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getFriendRankByMarkId(markId), observer);
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
