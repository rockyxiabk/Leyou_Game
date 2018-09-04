package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.BankCardInfoBean;

/**
 * Description : com.leyou.game.ipresenter.mine
 *
 * @author : rocky
 * @Create Time : 2017/8/23 上午11:06
 * @Modified Time : 2017/8/23 上午11:06
 */
public interface IWithCashApply {
    void showBankInfo(BankCardInfoBean bankCardInfoBean);

    void showApplyCashResult(boolean result, String msg);

    void showMessageToast(String msg);
}
