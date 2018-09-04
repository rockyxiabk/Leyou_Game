package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.user.SignBean;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/5/10 下午4:02
 * @Modified Time : 2017/5/10 下午4:02
 */
public interface ISignDiamond {


    void showSignInfo(SignBean signBean);

    void showSignSuccess(String virtualCoin);

    void showSignFailed();

    void showMessage(String msg);
}
