package com.leyou.game.ipresenter;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/4/19 下午3:02
 * @Modified Time : 2017/4/19 下午3:02
 */
public interface IRegisterActivity extends IBasePresenter {

    void sendSmsStatus(boolean isSuccess);

    void toMainActivity();
}
