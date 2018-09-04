package com.leyou.game.ipresenter.game;

import com.leyou.game.bean.game.GameBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter.game
 *
 * @author : rocky
 * @Create Time : 2017/10/31 下午9:38
 * @Modified Time : 2017/10/31 下午9:38
 */
public interface ICategory {
    void showDataList(List<GameBean> dataList);

    void showMessage(String msg);
}
