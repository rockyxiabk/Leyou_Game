package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.diamond.DiamondBean;
import com.leyou.game.bean.game.GameWinPriseBean;
import com.leyou.game.ipresenter.mine.IAwardListActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.DiamondApi;
import com.leyou.game.util.newapi.GameApi;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/6/19 下午5:33
 * @Modified Time : 2017/6/19 下午5:33
 */
public class AwardListPresenter {
    private Context context;
    private IAwardListActivity iAwardListActivity;
    private Subscription subscribe;
    private boolean isLoadAll = false;
    private int currentPage = 1;
    private int pageSize = 6;
    private Observer<ResultArray<GameWinPriseBean>> observer = new Observer<ResultArray<GameWinPriseBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iAwardListActivity.showErrorView();
        }

        @Override
        public void onNext(ResultArray<GameWinPriseBean> awardInfoBeanResultArray) {
            if (1 == awardInfoBeanResultArray.result) {
                List<GameWinPriseBean> data = awardInfoBeanResultArray.data;
                if (null != data && data.size() > 0) {
                    if (currentPage == 1) {
                        if (data.size() < pageSize) {
                            isLoadAll = true;
                            iAwardListActivity.setAwardLoadAll(true);
                            iAwardListActivity.showMyAwardList(data);
                        } else if (data.size() == pageSize) {
                            isLoadAll = false;
                            currentPage++;
                            iAwardListActivity.setAwardLoadAll(false);
                            iAwardListActivity.showMyAwardList(data);
                        }
                    } else {
                        if (data.size() < pageSize) {
                            isLoadAll = true;
                            iAwardListActivity.setAwardLoadAll(true);
                            iAwardListActivity.showLoadMoreList(data);
                        } else if (data.size() == pageSize) {
                            isLoadAll = false;
                            currentPage++;
                            iAwardListActivity.setAwardLoadAll(false);
                            iAwardListActivity.showLoadMoreList(data);
                        }
                    }
                } else {
                    isLoadAll = true;
                    // TODO: 2017/12/11 后台修改返回流程
                }
            } else {
                isLoadAll = true;
                iAwardListActivity.showErrorView();
            }
//            if (awardInfoBeanResultArray.result == 1) {
//                List<GameWinPriseBean> data = awardInfoBeanResultArray.data;
//                if (null != data && data.size() > 0) {
//                    iAwardListActivity.showMyAwardList(data);
//                } else {
//                    iAwardListActivity.showErrorView();
//                }
//            } else {
//                iAwardListActivity.showErrorView();
//            }
        }
    };

    public AwardListPresenter(Context context, IAwardListActivity iAwardListActivity) {
        this.context = context;
        this.iAwardListActivity = iAwardListActivity;
        getUserAwardList();
    }

    public void getUserAwardList() {
        currentPage = 1;
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getMyWinList(currentPage, pageSize), observer);
    }

    public void loadMoreAwardList() {
        if (!isLoadAll) {
            subscribe = HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getMyWinList(currentPage, pageSize), observer);
        }
    }

    /**
     * 解除订阅，停止数据请求
     */
    public void destroy() {
        if (null != subscribe && subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }


}
