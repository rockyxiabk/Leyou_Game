package com.leyou.game.presenter.game;

import android.content.Context;
import android.text.TextUtils;

import com.leyou.game.Constants;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.dao.SearchHistory;
import com.leyou.game.ipresenter.fight.IGameSearch;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.GameApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.game
 *
 * @author : rocky
 * @Create Time : 2017/9/25 下午7:19
 * @Modified Time : 2017/9/25 下午7:19
 */
public class GameSearchPresenter {
    private static final String TAG = "GameSearchPresenter";

    private Context context;
    private IGameSearch iGameSearch;

    public GameSearchPresenter(Context context, IGameSearch iGameSearch) {
        this.context = context;
        this.iGameSearch = iGameSearch;
        queryListAuto();
        getSearchWord();
    }

    public void queryListAuto() {
        List<SearchHistory> searchHistories = DBUtil.getInstance(context).queryHistorySearchAuto();
        iGameSearch.setSearchHistoryData(searchHistories);
    }

    public void queryListByKey(String keyWord) {
        List<SearchHistory> searchHistories = DBUtil.getInstance(context).queryHistorySearch(keyWord);
        iGameSearch.setSearchHistoryData(searchHistories);
    }

    public void search(String keyWord) {
        if (!TextUtils.isEmpty(keyWord)) {
            SearchHistory searchHistory = DBUtil.getInstance(context).querySearchHistoryByKeyWord(keyWord);
            if (null == searchHistory) {
                SearchHistory searchHistory1 = new SearchHistory(System.currentTimeMillis(), keyWord, System.currentTimeMillis());
                DBUtil.getInstance(context).insertSearchHistory(searchHistory1);
            }
            iGameSearch.showLoading();
            iGameSearch.changeLoadingDes("搜索关键词中...");
            getSearchByKeyWord(keyWord);
        } else {
            queryListAuto();
        }
    }


    public void getSearchWord() {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getHotWord(), new Observer<ResultArray<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                iGameSearch.setHotSearchList(null);
            }

            @Override
            public void onNext(ResultArray<String> stringResultArray) {
                if (stringResultArray.result == 1) {
                    List<String> data = stringResultArray.data;
                    iGameSearch.setHotSearchList(data);
                } else {
                    iGameSearch.setHotSearchList(null);
                }
            }
        });
    }

    public void getSearchByKeyWord(String keyWord) {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL)
                .getGameListByHotWord(keyWord, 1, Constants.FIFTEEN), new Observer<ResultArray<GameBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                iGameSearch.setSearchByKeyData(null);
            }

            @Override
            public void onNext(ResultArray<GameBean> gameBeanResultArray) {
                if (gameBeanResultArray.result == 1) {
                    List<GameBean> data = gameBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iGameSearch.setSearchByKeyData(data);
                    } else {
                        iGameSearch.setSearchByKeyData(null);
                    }
                } else {
                    iGameSearch.setSearchByKeyData(null);
                }
            }
        });
    }
}
