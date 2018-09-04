package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.ExChangeBuyBean;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.ipresenter.mine.IExChange;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.TradeApi;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/6/20 下午6:58
 * @Modified Time : 2017/6/20 下午6:58
 */
public class ExChangeBuyPresenter {

    private Context context;
    private IExChange iExChangeActivity;
    private Subscription subscribe;
    private int page = 0;
    private boolean loadMoreFlag = true;

    private Observer<ResultArray<ExChangeBuyBean>> observer = new Observer<ResultArray<ExChangeBuyBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iExChangeActivity.isShowNullListView(true);
            iExChangeActivity.showMessage(context.getString(R.string.data_load_failed));
        }

        @Override
        public void onNext(ResultArray<ExChangeBuyBean> exChangeBeanResultArray) {
            if (null != exChangeBeanResultArray) {
                int result = exChangeBeanResultArray.result;
                if (0 != result) {
                    List<ExChangeBuyBean> data = exChangeBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        if (data.size() == Constants.FIFTEEN) {
                            if (page == 1) {
                                iExChangeActivity.isShowNullListView(false);
                                iExChangeActivity.showRefreshBuyData(data);
                            } else {
                                iExChangeActivity.loadMoreBuyData(data);
                            }
                            page += 1;
                        } else if (data.size() < Constants.FIFTEEN) {
                            loadMoreFlag = false;
                            if (page == 1) {
                                iExChangeActivity.isShowNullListView(false);
                                iExChangeActivity.showRefreshBuyData(data);
                            } else {
                                iExChangeActivity.loadMoreBuyData(data);
                            }
                            iExChangeActivity.setAdapterIsLoadAll(true);
                        }
                    } else {
                        if (page == 1) {
                            iExChangeActivity.isShowNullListView(true);
                            iExChangeActivity.showRefreshBuyData(data);
                        }
                        iExChangeActivity.setAdapterIsLoadAll(true);
                    }
                } else {
                    iExChangeActivity.isShowNullListView(true);
                    iExChangeActivity.showMessage(context.getString(R.string.data_load_failed));
                }
            } else {
                iExChangeActivity.isShowNullListView(true);
                iExChangeActivity.showMessage(context.getString(R.string.data_load_failed));
            }
        }
    };

    public ExChangeBuyPresenter(Context context, IExChange iExChange) {
        this.context = context;
        this.iExChangeActivity = iExChange;
        page = 1;
    }

    public void getExChangeList() {
        page = 1;
        loadMoreFlag = true;
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(TradeApi.class, Constants.URL).getExChangeBuyList(page, Constants.FIFTEEN), observer);
    }

    public void getMoreExChangeList() {
        if (loadMoreFlag) {
            subscribe = HttpUtil.subscribe(HttpUtil.createApi(TradeApi.class, Constants.URL).getExChangeBuyList(page, Constants.FIFTEEN), observer);
        } else {
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
