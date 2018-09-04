package com.leyou.game.ipresenter.treasure;

import com.leyou.game.bean.WorkerBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/6/5 下午4:34
 * @Modified Time : 2017/6/5 下午4:34
 */
public interface IWorkerUpgradeActivity {
    void showMineWorkerData(List<WorkerBean> data);

    void showUpgradeWorkerInfo();

    void showUpgradeNumber();

    void showUpgradeFailed();

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void showMessageToast(String msg);

    void sendEventUpgradeSuccess();
}
