package com.leyou.game.ipresenter;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/5/16 下午3:37
 * @Modified Time : 2017/5/16 下午3:37
 */
public interface IOrderPayActivity {

    void checkOrderState();

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void showMessageToast(String msg);

    void updateWealth();

    void startOrderResultPage(String tradeNo);

}
