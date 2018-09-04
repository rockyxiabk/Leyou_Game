package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.diamond.DiamondBean;
import com.leyou.game.bean.game.GameWinPriseBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/6/19 下午5:30
 * @Modified Time : 2017/6/19 下午5:30
 */
public interface IAwardListActivity {
    void showMyAwardList(List<GameWinPriseBean> list);

    void showLoadMoreList(List<GameWinPriseBean> list);

    void setAwardLoadAll(boolean isLoadAll);

    void showNullView();

    void showErrorView();

    void showMessageToast(String msg);
}
