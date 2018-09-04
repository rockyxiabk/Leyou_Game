package com.leyou.game.ipresenter.mine;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/5/10 下午4:02
 * @Modified Time : 2017/5/10 下午4:02
 */
public interface ISignDiamond {

    void showDayAndDiamondNum(int day, int number);

    void showSignSuccess(int virtualCoin);

    void showSignFailed();

    void showMessage(String msg);
}
