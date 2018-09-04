package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.TradeBean;
import com.leyou.game.ipresenter.mine.ITradeNoteFragmentPresenter;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.TradeApi;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/5/10 下午2:12
 * @Modified Time : 2017/5/10 下午2:12
 */
public class TradeNoteFragmentPresenter {
    private Context context;
    private ITradeNoteFragmentPresenter iTradeNoteFragmentPresenter;
    private Observer<ResultArray<TradeBean>> observerBuy = new Observer<ResultArray<TradeBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iTradeNoteFragmentPresenter.showErrorView();
            iTradeNoteFragmentPresenter.showMassage(context.getString(R.string.data_load_failed));
        }

        @Override
        public void onNext(ResultArray<TradeBean> tradeBeanResultArray) {
            int result = tradeBeanResultArray.result;
            if (1 == result) {
                List<TradeBean> data = tradeBeanResultArray.data;
                if (null != data && data.size() > 0) {
                    iTradeNoteFragmentPresenter.showDataList(data);
                } else {
                    iTradeNoteFragmentPresenter.showInduceView();
                }
            } else {
                iTradeNoteFragmentPresenter.showInduceView();
            }
        }
    };
    private Observer<ResultArray<TradeBean>> observerSell = new Observer<ResultArray<TradeBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iTradeNoteFragmentPresenter.showErrorView();
            iTradeNoteFragmentPresenter.showMassage(context.getString(R.string.data_load_failed));
        }

        @Override
        public void onNext(ResultArray<TradeBean> tradeBeanResultArray) {
            int result = tradeBeanResultArray.result;
            if (1 == result) {
                List<TradeBean> data = tradeBeanResultArray.data;
                if (null != data && data.size() > 0) {
                    iTradeNoteFragmentPresenter.showDataList(data);
                } else {
                    iTradeNoteFragmentPresenter.showInduceView();
                }
            } else {
                iTradeNoteFragmentPresenter.showInduceView();
            }
        }
    };
    private Subscription subscribeBuy;
    private Subscription subscribeSell;

    public TradeNoteFragmentPresenter(Context context, ITradeNoteFragmentPresenter iTradeNoteFragmentPresenter) {
        this.context = context;
        this.iTradeNoteFragmentPresenter = iTradeNoteFragmentPresenter;
    }

    public void getDataList(int type) {
        if (1 == type) {
            subscribeBuy = HttpUtil.subscribe(HttpUtil.createApi(TradeApi.class, Constants.URL).getBuyRecords(), observerBuy);
        } else {
            subscribeSell = HttpUtil.subscribe(HttpUtil.createApi(TradeApi.class, Constants.URL).getSellRecords(), observerSell);
        }
    }

    /**
     * 解除订阅，停止数据请求
     */
    public void unSubscribe() {
        if (subscribeBuy != null && !subscribeBuy.isUnsubscribed()) {
            subscribeBuy.unsubscribe();
        }
        if (subscribeSell != null && !subscribeSell.isUnsubscribed()) {
            subscribeSell.unsubscribe();
        }
    }
}
