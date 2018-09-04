package com.leyou.game.ipresenter.fight;

import com.leyou.game.bean.game.GameBean;
import com.leyou.game.dao.SearchHistory;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter.fight
 *
 * @author : rocky
 * @Create Time : 2017/9/25 下午7:18
 * @Modified Time : 2017/9/25 下午7:18
 */
public interface IGameSearch {

    void setHotSearchList(List<String> data);

    void setSearchHistoryData(List<SearchHistory> searchHistoryData);

    void setSearchByKeyData(List<GameBean> data);

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void showMessageToast(String msg);

}
