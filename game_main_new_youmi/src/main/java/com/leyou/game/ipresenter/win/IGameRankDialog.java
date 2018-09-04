package com.leyou.game.ipresenter.win;

import com.leyou.game.bean.game.GameRankBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/4/25 下午6:12
 * @Modified Time : 2017/4/25 下午6:12
 */
public interface IGameRankDialog {

    void showLoadingView();

    void showErrorView();

    void showListView();

    void showData(List<GameRankBean> list);

    void showError(String error);
}
