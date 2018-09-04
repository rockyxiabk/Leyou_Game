package com.leyou.game.ipresenter.treasure;

import com.leyou.game.bean.PropBean;
import com.leyou.game.bean.TreasureBean;
import com.leyou.game.bean.WorkerBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/6/30 上午10:59
 * @Modified Time : 2017/6/30 上午10:59
 */
public interface IPropFragment {

    void showWorkerPopUpWindow();

    void showPropInfo(PropBean propBean);

    void showWorkerData(List<WorkerBean> data);

    void hiddenWorkerPopUpWindow();

    void showPropListData(List<PropBean> data);

    void showRefreshTreasure(int type, boolean flag);

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void showMessageToast(String msg);

    void raceTreasure(TreasureBean treasureBean);
}
