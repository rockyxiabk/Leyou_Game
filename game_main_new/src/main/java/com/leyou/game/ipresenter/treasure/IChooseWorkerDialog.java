package com.leyou.game.ipresenter.treasure;

import com.leyou.game.bean.treasure.TreasureBean;
import com.leyou.game.bean.treasure.WorkerBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/6/12 下午5:52
 * @Modified Time : 2017/6/12 下午5:52
 */
public interface IChooseWorkerDialog {
    void showMineWorker(List<WorkerBean> workerBeanList);

    void showEnemyWorker(List<WorkerBean> data);

    void showChips(int chipsCount);

    void showMessage(String msg);

    void raceState(boolean flag,TreasureBean treasureBean,int harvest);

    void dismissView();

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void showMessageToast(String string);

    void refreshTreasure(TreasureBean treasureBean);
}
