package com.leyou.game.presenter;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.bean.game.GameCommentBean;
import com.leyou.game.ipresenter.IGameDetailActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.GameApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/10/30 下午7:09
 * @Modified Time : 2017/10/30 下午7:09
 */
public class GameDetailPresenter {
    private static final String TAG = "GameDetailPresenter";
    private Context context;
    private IGameDetailActivity iGameDetailActivity;

    public GameDetailPresenter(Context context, IGameDetailActivity iGameDetailActivity) {
        this.context = context;
        this.iGameDetailActivity = iGameDetailActivity;
    }

    public void getGameDetail(final GameBean gameBean) {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getGameDetail(gameBean.uniqueMark), new Observer<Result<GameBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iGameDetailActivity.showGameDetail(gameBean);
            }

            @Override
            public void onNext(Result<GameBean> gameBeanResult) {
                if (gameBeanResult.result == 1) {
                    iGameDetailActivity.showGameDetail(gameBeanResult.data);
                } else {
                    iGameDetailActivity.showGameDetail(gameBean);
                }
            }
        });
    }

    public void getGameCommentList(GameBean gameBean) {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getGameCommentList(gameBean.uniqueMark, 1, 10), new Observer<ResultArray<GameCommentBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                iGameDetailActivity.showCommentListData(null);
            }

            @Override
            public void onNext(ResultArray<GameCommentBean> gameCommentBeanResultArray) {
                if (gameCommentBeanResultArray.result == 1) {
                    List<GameCommentBean> data = gameCommentBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iGameDetailActivity.showCommentListData(data);
                    } else {
                        iGameDetailActivity.showCommentListData(null);
                    }
                } else {
                    iGameDetailActivity.showCommentListData(null);
                }
            }
        });
    }
}
