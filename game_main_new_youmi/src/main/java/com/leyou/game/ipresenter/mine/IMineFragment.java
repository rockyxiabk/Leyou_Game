package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.game.GameBean;

import java.util.List;

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

    void isLogIn(boolean flag);

    void setPrizeDynamic(int num);

    void setMyPlayedList(List<GameBean> data);

    void startOtherActivity(Class<?> cls, Class<?> unLogIn);
}
