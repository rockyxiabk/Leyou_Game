package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.BankCardInfoBean;
import com.leyou.game.bean.Result;
import com.leyou.game.ipresenter.mine.IWithCashApply;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.UserApi;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.mine
 *
 * @author : rocky
 * @Create Time : 2017/8/23 上午11:06
 * @Modified Time : 2017/8/23 上午11:06
 */
public class WithCashApplyPresenter {

    private Context context;
    private IWithCashApply iWithCashApply;

    public WithCashApplyPresenter(Context context, IWithCashApply iWithCashApply) {
        this.context = context;
        this.iWithCashApply = iWithCashApply;
        getBankInfo();
    }

    private void getBankInfo() {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getBindCardInfo(), new Observer<Result<BankCardInfoBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result<BankCardInfoBean> bankCardInfoBeanResult) {
                if (bankCardInfoBeanResult.result == 1) {
                    iWithCashApply.showBankInfo(bankCardInfoBeanResult.data);
                }
            }
        });
    }

    public void commitApply(double money) {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).applyCash(money), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iWithCashApply.showApplyCashResult(false, "提交失败，请重新提交");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iWithCashApply.showApplyCashResult(true, "提交成功，请注意查收");
                } else {
                    iWithCashApply.showApplyCashResult(false, "提交失败，请重新提交");
                }
            }
        });
    }
}
