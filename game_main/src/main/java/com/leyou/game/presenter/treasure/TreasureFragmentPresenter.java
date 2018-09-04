package com.leyou.game.presenter.treasure;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.PropBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.TreasureBean;
import com.leyou.game.bean.TreasureExtBean;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.bean.WorkerExtBean;
import com.leyou.game.ipresenter.treasure.ITreasureFragment;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.TreasureApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/5/11 下午3:40
 * @Modified Time : 2017/5/11 下午3:40
 */
public class TreasureFragmentPresenter {
    private Context context;
    private ITreasureFragment iTreasureFragment;

    public TreasureFragmentPresenter(Context context, ITreasureFragment iTreasureFragment) {
        this.context = context;
        this.iTreasureFragment = iTreasureFragment;
        getCanEmployWorker();
        queryTreasures();
        getCanEmployRefreshTime();
    }

    public void getUserWorkerPlace() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getUserWorkerCount(), new Observer<Result<WorkerExtBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iTreasureFragment.setUserWorkerPlaceNumber(0, 10);
            }

            @Override
            public void onNext(Result<WorkerExtBean> workerExtBeanResult) {
                if (workerExtBeanResult.result == 1) {
                    WorkerExtBean data = workerExtBeanResult.data;
                    iTreasureFragment.setUserWorkerPlaceNumber(data.presentWorkerNumber, data.presentWorkerPlaceholder);
                } else {
                    iTreasureFragment.setUserWorkerPlaceNumber(0, 10);
                }
            }
        });
    }

    public void getCanEmployWorker() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getUserCanEmployWorker(), new Observer<ResultArray<WorkerBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultArray<WorkerBean> workerBeanResultArray) {
                if (workerBeanResultArray.result == 1) {
                    List<WorkerBean> data = workerBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iTreasureFragment.setCanEmployWorkerData(data);
                    } else {
                        iTreasureFragment.setCanEmployWorkerData(null);
                    }
                } else {
                    iTreasureFragment.setCanEmployWorkerData(null);
                }
            }
        });

    }

    public void refreshWorker(int coin) {
        iTreasureFragment.showLoadingView();
        iTreasureFragment.showLoadingViewText("矿工刷新中...");
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getUserCanEmployWorkerBuyRefresh(coin), new Observer<ResultArray<WorkerBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iTreasureFragment.dismissedLoadingView();
            }

            @Override
            public void onNext(ResultArray<WorkerBean> workerBeanResultArray) {
                if (workerBeanResultArray.result == 1) {
                    List<WorkerBean> data = workerBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iTreasureFragment.setCanEmployWorkerData(data);
                        getCanEmployRefreshTime();
                    } else {
                        iTreasureFragment.showMessageToast("刷新矿工失败，查看钻石数量是否不足！");
                    }
                } else {
                    iTreasureFragment.showMessageToast("刷新矿工失败，查看钻石数量是否不足！");
                }
                iTreasureFragment.dismissedLoadingView();
            }
        });
    }

    public void getCanEmployRefreshTime() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getCanEmployRefreshTime(), new Observer<Result<WorkerExtBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                long nextTime = System.currentTimeMillis() + 30 * 1000;
                iTreasureFragment.setCanEmployCountDownTime(nextTime);
            }

            @Override
            public void onNext(Result<WorkerExtBean> workerExtBeanResult) {
                if (workerExtBeanResult.result == 1) {
                    WorkerExtBean data = workerExtBeanResult.data;
                    iTreasureFragment.setCanEmployCountDownTime(data.nextRefreshTime);
                } else {
                    long nextTime = System.currentTimeMillis() + 30 * 1000;
                    iTreasureFragment.setCanEmployCountDownTime(nextTime);
                }
            }
        });
    }

    public void queryTreasures() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).queryTreasures(), new Observer<ResultArray<TreasureBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iTreasureFragment.setTreasureData(null);
                iTreasureFragment.dismissedLoadingView();
            }

            @Override
            public void onNext(ResultArray<TreasureBean> treasureBeanResultArray) {
                int result = treasureBeanResultArray.result;
                if (result == 1) {
                    List<TreasureBean> data = treasureBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iTreasureFragment.setTreasureData(treasureBeanResultArray.data);
                    } else {
                        iTreasureFragment.setTreasureData(null);
                    }
                } else {
                    iTreasureFragment.setTreasureData(null);
                }
                iTreasureFragment.dismissedLoadingView();
            }
        });
    }

    public void queryCanHarvestChips(String treasureId) {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).queryCanHarvestChips(treasureId), new Observer<Result<TreasureExtBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iTreasureFragment.showChipsView(0);
            }

            @Override
            public void onNext(Result<TreasureExtBean> treasureExtBeanResult) {
                if (treasureExtBeanResult.result == 1) {
                    TreasureExtBean data = treasureExtBeanResult.data;
                    if (null != data) {
                        iTreasureFragment.showChipsView(data.chipsGainNumber);
                    } else {
                        iTreasureFragment.showChipsView(0);
                    }
                } else {
                    iTreasureFragment.showChipsView(0);
                }
            }
        });
    }

    public void queryCanHarvestProp(String treasuredId) {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getCanHarvestProp(treasuredId), new Observer<ResultArray<PropBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultArray<PropBean> propBeanResultArray) {
                if (propBeanResultArray.result == 1) {
                    List<PropBean> data = propBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iTreasureFragment.showPropView(data);
                    }
                } else {

                }
            }
        });
    }

    public void deleteTreasure(String treasureId) {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).deleteTreasure(treasureId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iTreasureFragment.showMessageToast("废除宝窟失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iTreasureFragment.showMessageToast("废除宝窟成功");
                    queryTreasures();
                } else {
                    iTreasureFragment.showMessageToast("废除宝窟失败");
                }
            }
        });
    }

    public void harvestChips(final TreasureBean treasureBean, final int chipsNumber) {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).harvestChips(treasureBean.id), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iTreasureFragment.showMessageToast("收获碎钻失败");
                iTreasureFragment.removeChipView();
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iTreasureFragment.showMessageToast("成功收获" + chipsNumber + "碎钻");
                    iTreasureFragment.deleteTreasure(treasureBean);
                }
                iTreasureFragment.removeChipView();
            }
        });
    }
}
