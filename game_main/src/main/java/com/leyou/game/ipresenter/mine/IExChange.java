package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.ExChangeBuyBean;
import com.leyou.game.bean.ExChangeSaleBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/4/20 下午4:42
 * @Modified Time : 2017/4/20 下午4:42
 */
public interface IExChange {

    void showRefreshData(List<ExChangeSaleBean> list);

    void loadMoreData(List<ExChangeSaleBean> list);

    void showRefreshBuyData(List<ExChangeBuyBean> list);

    void loadMoreBuyData(List<ExChangeBuyBean> list);

    void isShowNullListView(boolean flag);

    void setAdapterIsLoadAll(boolean flag);

    void showMessage(String msg);
}
