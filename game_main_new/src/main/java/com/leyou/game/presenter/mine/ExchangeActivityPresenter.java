package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.diamond.DiamondBean;
import com.leyou.game.ipresenter.mine.IExchangeActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.DiamondApi;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.mine
 *
 * @author : rocky
 * @Create Time : 2017/8/14 下午5:14
 * @Modified Time : 2017/8/14 下午5:14
 */
public class ExchangeActivityPresenter {

    private final Context context;
    private final IExchangeActivity iExchangeActivity;

    public ExchangeActivityPresenter(Context context, IExchangeActivity iExchangeActivity) {
        this.context = context;
        this.iExchangeActivity = iExchangeActivity;
        getCurrentPrice();
    }

    private void getCurrentPrice() {
        HttpUtil.subscribe(HttpUtil.createApi(DiamondApi.class, Constants.URL).getDiamondPrice(), new Observer<Result<DiamondBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iExchangeActivity.setCurrentPrice(0.09, 0.11);
            }

            @Override
            public void onNext(Result<DiamondBean> markerPriceBeanResult) {
                if (markerPriceBeanResult.result == 1) {
                    DiamondBean data = markerPriceBeanResult.data;
                    iExchangeActivity.setCurrentPrice(data.minPrice, data.maxPrice);
                } else {
                    iExchangeActivity.setCurrentPrice(0.09, 0.11);
                }
            }
        });
    }
}
