package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.diamond.DiamondBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/8/10 下午3:58
 * @Modified Time : 2017/8/10 下午3:58
 */
public interface IConsumeActivity {

    void showConsumeTab(List<DiamondBean> list);


    void showDataList(List<DiamondBean> messageBeanList);

    void showLoadMoreList(List<DiamondBean> messageBeanList);

    void setConsumeLoadAll(boolean isLoadAll);

    void showNullView();

    void showErrorView();

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void showMessageToast(String msg);
}
