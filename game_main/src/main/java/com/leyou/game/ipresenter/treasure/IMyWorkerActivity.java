package com.leyou.game.ipresenter.treasure;

import com.leyou.game.bean.PropBean;
import com.leyou.game.bean.WorkerBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/6/19 下午2:06
 * @Modified Time : 2017/6/19 下午2:06
 */
public interface IMyWorkerActivity {

    void showWorkerPopUpWindow();

    void showWorkerInfo(WorkerBean workerBean);

    void showPropData(List<PropBean> data);

    void hiddenWorkerPopUpWindow();

    void setTreasureChips(int chipsNumber);

    void setUserWorkerPlaceNumber(int holder, int holderMax);

    void setMineWorkerData(List<WorkerBean> workerBeanList);

    void showConvertDiamondResult(boolean result, int diamondCount);

    void showWorkerUpStar(boolean result, String msg);

    void showMessageToast(String msg);
}
