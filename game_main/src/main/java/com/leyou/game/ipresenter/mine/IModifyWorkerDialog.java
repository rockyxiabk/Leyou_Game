package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.WorkerBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/6/20 上午11:20
 * @Modified Time : 2017/6/20 上午11:20
 */
public interface IModifyWorkerDialog {

    void showMessageToast(String msg);

    void showMineWorker(List<WorkerBean> data);

    void showMineCanUseWorker(List<WorkerBean> workerBeanList);

    void deleteTreasure(boolean flag);

    void modifyTreasure(boolean flag);
}
