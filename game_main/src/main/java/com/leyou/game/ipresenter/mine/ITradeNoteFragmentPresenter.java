package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.TradeBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/5/10 下午2:01
 * @Modified Time : 2017/5/10 下午2:01
 */
public interface ITradeNoteFragmentPresenter {
    void showErrorView();

    void showInduceView();

    void showDataList(List<TradeBean> tradeBeanList);

    void showMassage(String msg);
}
