package com.leyou.game.presenter.treasure;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.TreasureBean;
import com.leyou.game.bean.TreasureExtBean;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.ipresenter.treasure.IChooseWorkerDialog;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.api.TreasureApi;

import java.util.List;
import java.util.Map;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/6/12 下午5:53
 * @Modified Time : 2017/6/12 下午5:53
 */
public class ChooseWorkerDialogPresenter {
    public static final String TAG = "ChooseWorkerDialogPresenter";
    private Context context;
    private IChooseWorkerDialog iChooseWorkerDialog;

    public ChooseWorkerDialogPresenter(Context context, IChooseWorkerDialog iChooseWorkerDialog) {
        this.context = context;
        this.iChooseWorkerDialog = iChooseWorkerDialog;
    }

    public void getMineWorker(int type, int level) {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getUserCanFightWorker(type, level), new Observer<ResultArray<WorkerBean>>() {
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
                        iChooseWorkerDialog.showMineWorker(data);
                    } else {
                        iChooseWorkerDialog.showMineWorker(null);
                    }
                }
            }
        });
    }

    public void getEnemyWorker(String id) {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getTreasureWorker(id), new Observer<ResultArray<WorkerBean>>() {
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
                        iChooseWorkerDialog.showEnemyWorker(data);
                    }
                }
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
                iChooseWorkerDialog.showChips(0);
            }

            @Override
            public void onNext(Result<TreasureExtBean> treasureExtBeanResult) {
                if (treasureExtBeanResult.result == 1) {
                    TreasureExtBean data = treasureExtBeanResult.data;
                    if (null != data) {
                        iChooseWorkerDialog.showChips(data.chipsGainNumber);
                    } else {
                        iChooseWorkerDialog.showChips(0);
                    }
                } else {
                    iChooseWorkerDialog.showChips(0);
                }
            }
        });
    }

    public void refreshTreasure() {
        iChooseWorkerDialog.changeLoadingDes(context.getString(R.string.refresh_treasure));
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).refreshTreasure(), new Observer<Result<TreasureBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iChooseWorkerDialog.dismissedLoading();
                iChooseWorkerDialog.showMessageToast(context.getString(R.string.data_refresh_failed));
            }

            @Override
            public void onNext(Result<TreasureBean> treasureBeanResultArray) {
                int result = treasureBeanResultArray.result;
                if (result == 1) {
                    TreasureBean data = treasureBeanResultArray.data;
                    if (null != data) {
                        iChooseWorkerDialog.refreshTreasure(data);
                    } else {
                        iChooseWorkerDialog.dismissedLoading();
                        iChooseWorkerDialog.showMessageToast(context.getString(R.string.data_refresh_failed));
                    }
                } else {
                    iChooseWorkerDialog.dismissedLoading();
                    iChooseWorkerDialog.showMessageToast(context.getString(R.string.data_refresh_failed));
                }
            }
        });
    }

    public void harvestTreasure(List<WorkerBean> chooseWorkerList, Map<Integer, Boolean> choseMap, final TreasureBean treasureBean) {
        String json = "";
        if (null != chooseWorkerList && chooseWorkerList.size() > 0 && null != choseMap && choseMap.size() > 0) {
            for (int i = 0; i < choseMap.size(); i++) {
                Boolean aBoolean = choseMap.get(i);
                if (aBoolean) {
                    json = json + chooseWorkerList.get(i).id + ",";
                }
            }
        }
        LogUtil.d(TAG, "--" + json);

        String json1 = "";
        if (json.length() > 0) {
            json1 = json.substring(0, json.length() - 1);
        } else {
            iChooseWorkerDialog.dismissedLoading();
            iChooseWorkerDialog.showMessage("请选择要出战的矿工");
            return;
        }
        LogUtil.d(TAG, "--" + json1);

        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getRaceTreasure(json1), new Observer<Result<TreasureBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iChooseWorkerDialog.raceState(false, treasureBean, treasureBean.type);
                iChooseWorkerDialog.dismissedLoading();
                iChooseWorkerDialog.showMessage("抢占失败！");
            }

            @Override
            public void onNext(Result<TreasureBean> treasureBeanResult) {
                if (treasureBeanResult.result == 1) {
                    iChooseWorkerDialog.raceState(true, treasureBeanResult.data, treasureBean.type);
                } else {
                    iChooseWorkerDialog.raceState(false, treasureBean, treasureBean.type);
                }
            }
        });
    }

    public void extractTreasure(String id, List<WorkerBean> chooseWorkerList, Map<Integer, Boolean> choseMap) {
        String json = "";
        if (null != chooseWorkerList && chooseWorkerList.size() > 0 && null != choseMap && choseMap.size() > 0) {
            for (int i = 0; i < choseMap.size(); i++) {
                Boolean aBoolean = choseMap.get(i);
                if (aBoolean) {
                    json = json + chooseWorkerList.get(i).id + ",";
                }
            }
        }
        LogUtil.d(TAG, "--" + json);

        String json1 = "";
        if (json.length() > 0) {
            json1 = json.substring(0, json.length() - 1);
        } else {
            iChooseWorkerDialog.dismissedLoading();
            iChooseWorkerDialog.showMessage("请选择要出战的矿工");
            return;
        }
        LogUtil.d(TAG, "--" + json1);
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).modifyTreasureWorker(id, json1), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iChooseWorkerDialog.dismissedLoading();
                iChooseWorkerDialog.showMessageToast("开采失败");
            }

            @Override
            public void onNext(Result stringResult) {
                if (stringResult.result == 1) {
                    iChooseWorkerDialog.dismissedLoading();
                    iChooseWorkerDialog.showMessageToast("开始开采");
                    iChooseWorkerDialog.dismissView();
                } else {
                    iChooseWorkerDialog.dismissedLoading();
                    iChooseWorkerDialog.showMessageToast("开采失败");
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
                iChooseWorkerDialog.dismissView();
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iChooseWorkerDialog.dismissView();
                } else {
                    iChooseWorkerDialog.dismissView();
                }
            }
        });
    }
}
