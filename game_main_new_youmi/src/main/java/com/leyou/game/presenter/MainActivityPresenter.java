package com.leyou.game.presenter;

import android.content.Context;
import android.util.Log;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.UpdateAppBean;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.ipresenter.IMainActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.GameApi;
import com.leyou.game.util.newapi.SystemApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/5/31 下午5:15
 * @Modified Time : 2017/5/31 下午5:15
 */
public class MainActivityPresenter {
    private static final String TAG = "MainActivityPresenter";
    private IMainActivity iMainActivity;
    private Context context;

    public MainActivityPresenter(IMainActivity iMainActivity, Context context) {
        this.context = context;
        this.iMainActivity = iMainActivity;
        getActivityGame();
    }

    public void checkVersion() {
        HttpUtil.subscribe(HttpUtil.createApi(SystemApi.class, Constants.URL).checkAppUpdate(), new Observer<Result<UpdateAppBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onNext(Result<UpdateAppBean> updateAppBeanResult) {
                if (updateAppBeanResult.result == 1) {
                    UpdateAppBean data = updateAppBeanResult.data;
                    if (null != data) {
                        if (Constants.getVerCode() < data.versionCode) {
                            iMainActivity.showNewVersionView(data);
                        } else {
                            iMainActivity.clearDownloadFile(true);
                        }
                    } else {
                        iMainActivity.clearDownloadFile(true);
                    }
                } else {
                    iMainActivity.clearDownloadFile(true);
                }
            }
        });
    }

    public void getActivityGame() {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getGameList( GameBean.TYPE_WIN_DIAMOND, 1, 1), new Observer<ResultArray<GameBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iMainActivity.showWinDiamondGame(null);
            }

            @Override
            public void onNext(ResultArray<GameBean> gameBeanResult) {
                if (gameBeanResult.result == 1) {
                    if (null != gameBeanResult.data && gameBeanResult.data.size() > 0) {
                        List<GameBean> data = gameBeanResult.data;
                        GameBean gameBean = data.get(0);
                        iMainActivity.showWinDiamondGame(gameBean);
                    } else {
                        iMainActivity.showWinDiamondGame(null);
                    }
                } else {
                    iMainActivity.showWinDiamondGame(null);
                }
            }
        });
    }
}
