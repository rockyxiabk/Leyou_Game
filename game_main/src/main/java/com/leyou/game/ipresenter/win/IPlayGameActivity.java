package com.leyou.game.ipresenter.win;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/5/25 下午1:56
 * @Modified Time : 2017/5/25 下午1:56
 */
public interface IPlayGameActivity {

    void loadUrl(String gameCountId);

    void showGameView();

    void sendOrderState(boolean flag);

    void showChongZhiDialog();

    void reStartPay();

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void showMessageToast(String msg);

}
