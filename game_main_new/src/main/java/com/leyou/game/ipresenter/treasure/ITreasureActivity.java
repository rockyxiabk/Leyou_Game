package com.leyou.game.ipresenter.treasure;

import com.leyou.game.bean.treasure.PropBean;
import com.leyou.game.bean.treasure.TreasureBean;
import com.leyou.game.bean.treasure.WorkerBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/6/23 下午12:07
 * @Modified Time : 2017/6/23 下午12:07
 */
public interface ITreasureActivity {
    void setTreasureData(List<TreasureBean> data);

    void setTreasureWorkerData(List<WorkerBean> data);

    void showChipsView(int number);

    void showPropView(List<PropBean> propList);

    void showCurrentTreasureInfo(TreasureBean treasureBean);

    void raceTreasure(TreasureBean data);

    void removeChipView();

    void deleteTreasure(TreasureBean treasureBean, boolean isHarvest, int chipsNumber);

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void showMessageToast(String msg);
}
