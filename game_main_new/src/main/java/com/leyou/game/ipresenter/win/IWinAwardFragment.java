package com.leyou.game.ipresenter.win;

import com.leyou.game.bean.game.GameBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/4/13 上午10:43
 * @Modified Time : 2017/4/13 上午10:43
 */
public interface IWinAwardFragment {

    void showView(boolean isError);

    void showGameList(List<GameBean> gameBeenList);

    void showError(String error);
}
