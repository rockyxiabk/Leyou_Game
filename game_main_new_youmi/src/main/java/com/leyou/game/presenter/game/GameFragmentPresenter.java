package com.leyou.game.presenter.game;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.ipresenter.game.IGameFragment;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.GameApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.game
 *
 * @author : rocky
 * @Create Time : 2017/10/18 下午3:41
 * @Modified Time : 2017/10/18 下午3:41
 */
public class GameFragmentPresenter {

    private static final String TAG = "GameFragmentPresenter";
    private Context context;
    private IGameFragment iGameFragment;
    private int page = 1;
    private int pageSize = Constants.TEN;
    private boolean isLoadAll = false;
    private Observer<ResultArray<GameBean>> observer = new Observer<ResultArray<GameBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            LogUtil.e(TAG, e.toString());
            iGameFragment.showDataList(null);
        }

        @Override
        public void onNext(ResultArray<GameBean> gameBeanResultArray) {
            if (gameBeanResultArray.result == 1) {
                List<GameBean> data = gameBeanResultArray.data;
                if (null != data && data.size() > 0) {
                    if (page == 1) {
                        if (data.size() == pageSize) {
                            iGameFragment.showDataList(data);
                            iGameFragment.setDataLoadAll(false);
                            isLoadAll = false;
                            page++;
                        } else {
                            iGameFragment.showDataList(data);
                            iGameFragment.setDataLoadAll(true);
                            isLoadAll = true;
                        }
                    } else {
                        if (data.size() == pageSize) {
                            iGameFragment.showDataLoadMoreList(data);
                            iGameFragment.setDataLoadAll(false);
                            page++;
                            isLoadAll = false;
                        } else {
                            iGameFragment.showDataLoadMoreList(data);
                            iGameFragment.setDataLoadAll(true);
                            isLoadAll = true;
                        }
                    }
                } else {
                    isLoadAll = true;
                }
            } else {
                isLoadAll = true;
            }
        }
    };
    private int type=0;

    public GameFragmentPresenter(Context context, IGameFragment iGameFragment) {
        this.context = context;
        this.iGameFragment = iGameFragment;
        getBannerData();
        getCategory();
        getGame(0);
    }

    /**
     * 游戏轮播位数据
     */
    public void getBannerData() {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getGameBanner(), new Observer<ResultArray<GameBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.fillInStackTrace());
                iGameFragment.setBannerList(null);
            }

            @Override
            public void onNext(ResultArray<GameBean> gameBeanResultArray) {
                if (gameBeanResultArray.result == 1) {
                    List<GameBean> data = gameBeanResultArray.data;
                    iGameFragment.setBannerList(data);
                } else {
                    iGameFragment.setBannerList(null);
                }
            }
        });
    }

    public void getCategory() {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getCategoryTypeList(), new Observer<ResultArray<GameBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iGameFragment.showCategoryList(null);
            }

            @Override
            public void onNext(ResultArray<GameBean> gameBeanResultArray) {
                if (gameBeanResultArray.result == 1) {
                    List<GameBean> data = gameBeanResultArray.data;
                    iGameFragment.showCategoryList(data);
                } else {
                    iGameFragment.showCategoryList(null);
                }
            }
        });
    }

    public void getGame(int type) {
        page = 1;
        this.type = type;
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getCategoryGameList(type, page, pageSize), observer);
    }

    public void loadMoreGameList() {
        if (!isLoadAll) {
            HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getCategoryGameList(type, page, pageSize), observer);
        } else {

        }
    }
}
