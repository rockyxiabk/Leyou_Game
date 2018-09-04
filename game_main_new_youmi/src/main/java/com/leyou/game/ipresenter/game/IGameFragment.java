package com.leyou.game.ipresenter.game;

import com.leyou.game.bean.game.GameBean;
import com.leyou.game.ipresenter.IBasePresenter;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter.game
 *
 * @author : rocky
 * @Create Time : 2017/10/18 下午3:42
 * @Modified Time : 2017/10/18 下午3:42
 */
public interface IGameFragment extends IBasePresenter {
    void setBannerList(List<GameBean> data);

    void showCategoryList(List<GameBean> data);

    void  showAllCategory();

    void showDataList(List<GameBean> dataList);

    void showDataLoadMoreList(List<GameBean> gameBeanList);

    void setDataLoadAll(boolean isLoadAll);

    void showNoNetWorkDialog();

}
