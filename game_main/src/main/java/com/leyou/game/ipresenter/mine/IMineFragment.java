package com.leyou.game.ipresenter.mine;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/4/27 下午3:43
 * @Modified Time : 2017/4/27 下午3:43
 */
public interface IMineFragment {

    void showUserData(String name, String pictureUrl, String backgroundImage);

    void showWealth(int diamondCount, double money);

    void isDeveloper(boolean flag, int state);

    void isLogIn(boolean flag);

    void isBindCard(boolean flag);

    void setPrizeDynamic(boolean result);

    void startOtherActivity(Class<?> cls, Class<?> unLogIn);
}
