package com.leyou.game.presenter.treasure;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.PropBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.TreasureBean;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.ipresenter.treasure.IPropFragment;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.TreasureApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/6/30 上午11:07
 * @Modified Time : 2017/6/30 上午11:07
 */
public class MyPropFragmentPresenter {
    private Context context;
    private IPropFragment iPropFragment;

    public MyPropFragmentPresenter(Context context, IPropFragment iPropFragment) {
        this.context = context;
        this.iPropFragment = iPropFragment;
        getMyProp();
    }

    public void getMyProp() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getUserProp(0, 0), new Observer<ResultArray<PropBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iPropFragment.showPropListData(null);
            }

            @Override
            public void onNext(ResultArray<PropBean> propBeanResultArray) {
                if (propBeanResultArray.result == 1) {
                    List<PropBean> data = propBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iPropFragment.showPropListData(data);
                    } else {
                        iPropFragment.showPropListData(null);
                    }
                }
            }
        });
    }

    public void getMineWorker() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getUserWorker(1), new Observer<ResultArray<WorkerBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iPropFragment.showWorkerData(null);
            }

            @Override
            public void onNext(ResultArray<WorkerBean> workerBeanResultArray) {
                if (workerBeanResultArray.result == 1) {
                    List<WorkerBean> data = workerBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iPropFragment.showWorkerData(data);
                    } else {
                        iPropFragment.showWorkerData(null);
                    }
                } else {
                    iPropFragment.showWorkerData(null);
                }
            }
        });
    }

    public void useProp(String propId, final int type, String workerId) {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).useProp(propId, type, workerId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iPropFragment.showMessageToast("道具使用失败");
                iPropFragment.hiddenWorkerPopUpWindow();
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iPropFragment.showRefreshTreasure(type, true);
                    iPropFragment.hiddenWorkerPopUpWindow();
                } else {
                    iPropFragment.showRefreshTreasure(type, false);
                    iPropFragment.hiddenWorkerPopUpWindow();
                }
            }
        });
    }

    public void refreshTreasure() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).refreshTreasure(), new Observer<Result<TreasureBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iPropFragment.raceTreasure(null);
                iPropFragment.showMessageToast("查找宝库失败");
                iPropFragment.dismissedLoading();
            }

            @Override
            public void onNext(Result<TreasureBean> treasureBeanResultArray) {
                int result = treasureBeanResultArray.result;
                if (result == 1) {
                    TreasureBean data = treasureBeanResultArray.data;
                    if (null != data) {
                        iPropFragment.raceTreasure(data);
                    } else {
                        iPropFragment.raceTreasure(null);
                    }
                } else {
                    iPropFragment.raceTreasure(null);
                    iPropFragment.showMessageToast("查找宝库失败");
                }
                iPropFragment.dismissedLoading();
            }
        });
    }
}
