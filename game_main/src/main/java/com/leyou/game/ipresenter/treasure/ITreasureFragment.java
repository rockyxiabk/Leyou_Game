package com.leyou.game.ipresenter.treasure;

import com.leyou.game.bean.PropBean;
import com.leyou.game.bean.TreasureBean;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.bean.WorkerExtBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/5/11 下午3:38
 * @Modified Time : 2017/5/11 下午3:38
 */
public interface ITreasureFragment {

    void setTreasureData(List<TreasureBean> treasureList);

    void showCurrentTreasureInfo(TreasureBean treasureBean);

    void deleteTreasure(TreasureBean treasureBean);

    void showChipsView(int chipsNumber);

    void showPropView(List<PropBean> propList);

    void removeChipView();

    void setCanEmployWorkerData(List<WorkerBean> canEmployWorkerData);

    void setCanEmployCountDownTime(long countDownTime);

    void showLoadingView();

    void showLoadingViewText(String text);

    void dismissedLoadingView();

    void showMessageToast(String msg);

    void userUnLogIn();

    void UserLogIned();

    void setUserWorkerPlaceNumber(int has, int total);
}
