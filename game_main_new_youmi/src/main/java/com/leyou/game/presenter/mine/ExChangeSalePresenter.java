package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.diamond.DiamondExchangeBean;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.ipresenter.mine.IExChange;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.DiamondApi;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/4/25 上午11:19
 * @Modified Time : 2017/4/25 上午11:19
 */
public class ExChangeSalePresenter {
    private Context context;
    private IExChange iExChangeActivity;
    private Subscription subscribe;
    private int page = 0;
    private boolean loadMoreFlag = true;

    private Observer<ResultArray<DiamondExchangeBean>> observer = new Observer<ResultArray<DiamondExchangeBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iExChangeActivity.isShowNullListView(true);
        }

        @Override
        public void onNext(ResultArray<DiamondExchangeBean> exChangeBeanResultArray) {
            if (null != exChangeBeanResultArray) {
                int result = exChangeBeanResultArray.result;
                if (0 != result) {
                    List<DiamondExchangeBean> data = exChangeBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        if (data.size() == Constants.FIFTEEN) {
                            if (page == 1) {
                                iExChangeActivity.isShowNullListView(false);
                                iExChangeActivity.showRefreshData(data);
                            } else {
                                iExChangeActivity.loadMoreData(data);
                            }
                            page += 1;
                        } else if (data.size() < Constants.FIFTEEN) {
                            loadMoreFlag = false;
                            if (page == 1) {
                                iExChangeActivity.isShowNullListView(false);
                                iExChangeActivity.showRefreshData(data);
                            } else {
                                iExChangeActivity.loadMoreData(data);
                            }
                            iExChangeActivity.setAdapterIsLoadAll(true);
                        }
                    } else {
                        if (page == 1) {
                            iExChangeActivity.isShowNullListView(true);
                            iExChangeActivity.showRefreshData(data);
                        }
                        iExChangeActivity.setAdapterIsLoadAll(true);
                    }
                } else {
                    iExChangeActivity.isShowNullListView(true);
                    iExChangeActivity.showMessage(context.getString(R.string.no_more_data));
                }
            } else {
                iExChangeActivity.isShowNullListView(true);
                iExChangeActivity.showMessage(context.getString(R.string.no_more_data));
            }
        }
    };

    public ExChangeSalePresenter(Context context, IExChange iExChangeActivity) {
        this.context = context;
        this.iExChangeActivity = iExChangeActivity;
        page = 1;
    }

    public void getExChangeList(int minCoin, int maxCoin) {
        page = 1;
        loadMoreFlag = true;
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(DiamondApi.class, Constants.URL).getSaleList(minCoin, maxCoin, page, Constants.FIFTEEN), observer);
    }

    public void getMoreExChangeList(int minCoin, int maxCoin) {
        if (loadMoreFlag) {
            subscribe = HttpUtil.subscribe(HttpUtil.createApi(DiamondApi.class, Constants.URL).getSaleList(minCoin, maxCoin, page, Constants.FIFTEEN), observer);
        } else {
            if (page > 1)
                iExChangeActivity.showMessage(context.getString(R.string.no_more_data));
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
