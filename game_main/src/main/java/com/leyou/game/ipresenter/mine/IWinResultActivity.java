package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.GamePrizeResult;

import java.util.List;

/**
 * Description : 上期获奖游戏列表项
 *
 * @author : rocky
 * @Create Time : 2017/4/24 下午8:00
 * @Modified Time : 2017/4/24 下午8:00
 */
public interface IWinResultActivity {

    void showData(List<GamePrizeResult> list);

    void showError();
}
