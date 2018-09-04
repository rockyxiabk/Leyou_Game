package com.leyou.game.ipresenter.win;

import com.leyou.game.bean.game.GameBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/5/25 下午1:56
 * @Modified Time : 2017/5/25 下午1:56
 */
public interface IPlayGameActivity {

    void loadGame(GameBean gameBean);

    void setGameCountId(String gameCountId);

    void setRecommendData(List<GameBean> data);

    void showGameManagerDialog();

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void showMessageToast(String msg);

}
