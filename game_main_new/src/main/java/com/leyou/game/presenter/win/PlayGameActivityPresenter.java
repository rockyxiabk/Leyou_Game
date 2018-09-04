package com.leyou.game.presenter.win;

import android.content.Context;
import android.text.TextUtils;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.bean.game.GameExtBean;
import com.leyou.game.ipresenter.win.IPlayGameActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.GameApi;
import com.leyou.game.util.newapi.PayApi;
import com.leyou.game.util.newapi.UserApi;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/5/25 下午2:20
 * @Modified Time : 2017/5/25 下午2:20
 */
public class PlayGameActivityPresenter {
    private Context context;
    private IPlayGameActivity iPlayGameActivity;

    public PlayGameActivityPresenter(Context context, IPlayGameActivity iPlayGameActivity) {
        this.context = context;
        this.iPlayGameActivity = iPlayGameActivity;
    }

    public void startGame(String uniqueMark) {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getGameCountId(uniqueMark), new Observer<Result<GameExtBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result<GameExtBean> result) {
                if (result.result == 1) {
                    GameExtBean data = result.data;
                    if (null != data) {
                        iPlayGameActivity.setGameCountId(data.gameCountId);
                    }
                }
            }
        });
    }

    public void getRecommendData() {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getRecommendList(), new Observer<ResultArray<GameBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultArray<GameBean> gameBeanResultArray) {
                if (gameBeanResultArray.result == 1) {
                    List<GameBean> data = gameBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iPlayGameActivity.setRecommendData(data);
                    }
                }
            }
        });
    }

    public void destroy() {
    }
}
