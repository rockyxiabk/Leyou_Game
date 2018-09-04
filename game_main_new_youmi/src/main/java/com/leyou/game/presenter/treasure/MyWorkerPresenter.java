package com.leyou.game.presenter.treasure;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.treasure.PropBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.treasure.TreasureExtBean;
import com.leyou.game.bean.treasure.WorkerBean;
import com.leyou.game.bean.treasure.WorkerExtBean;
import com.leyou.game.ipresenter.treasure.IMyWorkerActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.TreasureApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/6/19 下午2:07
 * @Modified Time : 2017/6/19 下午2:07
 */
public class MyWorkerPresenter {
    private Context context;
    private IMyWorkerActivity iMyWorkerActivity;

    public MyWorkerPresenter(Context context, IMyWorkerActivity iMyWorkerActivity) {
        this.context = context;
        this.iMyWorkerActivity = iMyWorkerActivity;
        getMineWorker();
        getUserWorkerPlace();
        queryChips();
    }

    public void getMineWorker() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getUserWorker(), new Observer<ResultArray<WorkerBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iMyWorkerActivity.setMineWorkerData(null);
            }

            @Override
            public void onNext(ResultArray<WorkerBean> workerBeanResultArray) {
                if (workerBeanResultArray.result == 1) {
                    List<WorkerBean> data = workerBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iMyWorkerActivity.setMineWorkerData(data);
                    } else {
                        iMyWorkerActivity.setMineWorkerData(null);
                    }
                } else {
                    iMyWorkerActivity.setMineWorkerData(null);
                }
            }
        });
    }

    public void getUserWorkerPlace() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getUserWorkerCount(), new Observer<Result<WorkerExtBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iMyWorkerActivity.setUserWorkerPlaceNumber(0, 10);
            }

            @Override
            public void onNext(Result<WorkerExtBean> workerExtBeanResult) {
                if (workerExtBeanResult.result == 1) {
                    WorkerExtBean data = workerExtBeanResult.data;
                    iMyWorkerActivity.setUserWorkerPlaceNumber(data.presentWorkerNumber, data.presentWorkerPlaceholder);
                } else {
                    iMyWorkerActivity.setUserWorkerPlaceNumber(0, 10);
                }
            }
        });
    }

    public void queryChips() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).queryChips(), new Observer<Result<TreasureExtBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iMyWorkerActivity.setTreasureChips(0);
            }

            @Override
            public void onNext(Result<TreasureExtBean> treasureExtBeanResult) {
                if (treasureExtBeanResult.result == 1) {
                    TreasureExtBean data = treasureExtBeanResult.data;
                    if (null != data) {
                        iMyWorkerActivity.setTreasureChips(data.chipsSumNumber);
                    } else {
                        iMyWorkerActivity.setTreasureChips(0);
                    }
                } else {
                    iMyWorkerActivity.setTreasureChips(0);
                }
            }
        });
    }

    public void convertChips(int chipsNumber) {
        final int count = chipsNumber / 10000;
        if (count > 0) {
            HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).convertChips(count * 10000), new Observer<Result>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    iMyWorkerActivity.showConvertDiamondResult(false, count);
                }

                @Override
                public void onNext(Result result) {
                    if (result.result == 1) {
                        iMyWorkerActivity.showConvertDiamondResult(true, count);
                        queryChips();
                    } else {
                        iMyWorkerActivity.showConvertDiamondResult(false, count);
                    }
                }
            });
        } else {
            iMyWorkerActivity.showMessageToast("碎钻至少满10000才能兑换！");
        }
    }

    public void getMyProp() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getUserProp(1, 1), new Observer<ResultArray<PropBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iMyWorkerActivity.showMessageToast("暂无道具可以使用，去商城逛逛吧");
                iMyWorkerActivity.hiddenWorkerPopUpWindow();
            }

            @Override
            public void onNext(ResultArray<PropBean> propBeanResultArray) {
                if (propBeanResultArray.result == 1) {
                    List<PropBean> data = propBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iMyWorkerActivity.showPropData(data);
                        iMyWorkerActivity.showWorkerPopUpWindow();
                    } else {
                        iMyWorkerActivity.showPropData(data);
                        iMyWorkerActivity.showMessageToast("暂无道具可以使用，去商城逛逛吧");
                        iMyWorkerActivity.hiddenWorkerPopUpWindow();
                    }
                }
            }
        });
    }

    public void useProp(String workerId, String propId, final int type) {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).useProp(propId, 0, workerId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iMyWorkerActivity.showMessageToast("道具使用失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    if (type == 0) {
                        iMyWorkerActivity.showWorkPowerResult(true, "体力恢复成功");
                    } else if (type == 2) {
                        iMyWorkerActivity.showWorkerUpStar(true, "矿工升星成功");
                    } else {
                        iMyWorkerActivity.showWorkPowerResult(true, "道具使用成功");
                    }
                } else {
                    if (type == 0) {
                        iMyWorkerActivity.showWorkPowerResult(false, "道具使用成功");
                    } else if (type == 2) {
                        iMyWorkerActivity.showWorkerUpStar(false, "矿工升星失败");
                    } else {
                        iMyWorkerActivity.showWorkPowerResult(false, "道具使用失败");
                    }
                }
            }
        });
    }
}
