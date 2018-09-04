package com.leyou.game.ipresenter.treasure;

import com.leyou.game.bean.WorkerBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/6/7 下午5:47
 * @Modified Time : 2017/6/7 下午5:47
 */
public interface IWorkerComposeFragment {
    void showMineWorkerData(List<WorkerBean> beanList);

    void showComposeWorkerInfo();

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void showComposeResult(boolean state,String des);

    void showMessageToast(String msg);

    void sendEventComposedSuccess();
}
