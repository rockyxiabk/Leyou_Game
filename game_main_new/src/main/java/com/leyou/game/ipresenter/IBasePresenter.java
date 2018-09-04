package com.leyou.game.ipresenter;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/11/18 下午2:26
 * @Modified Time : 2017/11/18 下午2:26
 */
public interface IBasePresenter {
    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void showMessageToast(String msg);
}
