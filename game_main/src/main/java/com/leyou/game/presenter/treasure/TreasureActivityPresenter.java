package com.leyou.game.presenter.treasure;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.PropBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.TreasureBean;
import com.leyou.game.bean.TreasureExtBean;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.ipresenter.treasure.ITreasureActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.TreasureApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/6/23 下午12:07
 * @Modified Time : 2017/6/23 下午12:07
 */
public class TreasureActivityPresenter {
    private Context context;
    private ITreasureActivity iTreasureActivity;

    public TreasureActivityPresenter(Context context, ITreasureActivity iTreasureActivity) {
        this.context = context;
        this.iTreasureActivity = iTreasureActivity;
    }

    public void queryTreasures() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).queryTreasures(), new Observer<ResultArray<TreasureBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iTreasureActivity.setTreasureData(null);
                iTreasureActivity.dismissedLoading();
            }

            @Override
            public void onNext(ResultArray<TreasureBean> treasureBeanResultArray) {
                int result = treasureBeanResultArray.result;
                if (result == 1) {
                    List<TreasureBean> data = treasureBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iTreasureActivity.setTreasureData(treasureBeanResultArray.data);
                    } else {
                        iTreasureActivity.setTreasureData(null);
                    }
                } else {
                    iTreasureActivity.setTreasureData(null);
                }
                iTreasureActivity.dismissedLoading();
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
                iTreasureActivity.showChipsView(0);
            }

            @Override
            public void onNext(Result<TreasureExtBean> treasureExtBeanResult) {
                if (treasureExtBeanResult.result == 1) {
                    TreasureExtBean data = treasureExtBeanResult.data;
                    if (null != data) {
                        iTreasureActivity.showChipsView(data.chipsGainNumber);
                    } else {
                        iTreasureActivity.showChipsView(0);
                    }
                } else {
                    iTreasureActivity.showChipsView(0);
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
                        iTreasureActivity.showPropView(data);
                    }
                } else {

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
                iTreasureActivity.showMessageToast("收获碎钻失败");
                iTreasureActivity.removeChipView();
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iTreasureActivity.showMessageToast("成功收获" + chipsNumber + "碎钻");
                    iTreasureActivity.deleteTreasure(treasureBean);
                }
                iTreasureActivity.removeChipView();
            }
        });
    }

    public void refreshTreasure() {
        iTreasureActivity.showLoading();
        iTreasureActivity.changeLoadingDes(context.getString(R.string.treasure_refresh));
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).refreshTreasure(), new Observer<Result<TreasureBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iTreasureActivity.raceTreasure(null);
                iTreasureActivity.showMessageToast(context.getString(R.string.data_refresh_failed));
                iTreasureActivity.dismissedLoading();
            }

            @Override
            public void onNext(Result<TreasureBean> treasureBeanResultArray) {
                int result = treasureBeanResultArray.result;
                if (result == 1) {
                    TreasureBean data = treasureBeanResultArray.data;
                    if (null != data) {
                        iTreasureActivity.raceTreasure(data);
                    } else {
                        iTreasureActivity.raceTreasure(null);
                    }
                } else {
                    iTreasureActivity.raceTreasure(null);
                    iTreasureActivity.showMessageToast(context.getString(R.string.data_refresh_failed));
                }
                iTreasureActivity.dismissedLoading();
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
                iTreasureActivity.showMessageToast("废除宝窟失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iTreasureActivity.showMessageToast("废除宝窟成功");
                    queryTreasures();
                } else {
                    iTreasureActivity.showMessageToast("废除宝窟失败");
                }
            }
        });
    }

    public void getTreasureWorker(String treasureId) {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getCurrentTreasureWorker(treasureId), new Observer<ResultArray<WorkerBean>>() {
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
                        iTreasureActivity.setTreasureWorkerData(data);
                    } else {
                        iTreasureActivity.setTreasureWorkerData(null);
                    }
                }
            }
        });
    }
}
