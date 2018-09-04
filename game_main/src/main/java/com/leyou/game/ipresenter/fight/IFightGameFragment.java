package com.leyou.game.ipresenter.fight;

import com.leyou.game.bean.FightGameBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/7/15 下午12:09
 * @Modified Time : 2017/7/15 下午12:09
 */
public interface IFightGameFragment {

    void showRefreshData(List<FightGameBean> list);

    void isShowNullListView(boolean flag);

    void showMessage(String msg);

    void userUnLogIn();

    void userLogInEd();
}
