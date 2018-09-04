package com.leyou.game.ipresenter.treasure;

import com.leyou.game.bean.WorkerBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/6/5 下午4:32
 * @Modified Time : 2017/6/5 下午4:32
 */
public interface IWorkerDismissActivity {

    void showMineWorkerData(List<WorkerBean> beanList);

    void showDismissWorkerInfo(WorkerBean workerBean);

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void showMessageToast(String msg);

    void sendEventDismissSuccess();
}
