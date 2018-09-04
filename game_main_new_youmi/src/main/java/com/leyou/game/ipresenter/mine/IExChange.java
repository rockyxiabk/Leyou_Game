package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.diamond.DiamondExchangeBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/4/20 下午4:42
 * @Modified Time : 2017/4/20 下午4:42
 */
public interface IExChange {

    void showRefreshData(List<DiamondExchangeBean> list);

    void loadMoreData(List<DiamondExchangeBean> list);

    void showRefreshBuyData(List<DiamondExchangeBean> list);

    void loadMoreBuyData(List<DiamondExchangeBean> list);

    void isShowNullListView(boolean flag);

    void setAdapterIsLoadAll(boolean flag);

    void showMessage(String msg);
}
