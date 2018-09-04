package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.game.GameWinPriseBean;

import java.util.List;

/**
 * Description : 上期获奖结果名单接口
 *
 * @author : rocky
 * @Create Time : 2017/4/24 下午8:26
 * @Modified Time : 2017/4/24 下午8:26
 */
public interface IWinResultFragmentPresenter {
    void showData(List<GameWinPriseBean> list);

    void showError();

}
