package com.leyou.game.presenter.treasure;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.treasure.WorkerBean;
import com.leyou.game.ipresenter.treasure.IWorkerDismissActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.TreasureApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/6/5 下午4:34
 * @Modified Time : 2017/6/5 下午4:34
 */
public class WorkerDismissPresenter {
    private Context context;
    private IWorkerDismissActivity iWorkerDismissActivity;

    public WorkerDismissPresenter(Context context, IWorkerDismissActivity iWorkerDismissActivity) {
        this.context = context;
        this.iWorkerDismissActivity = iWorkerDismissActivity;
        getMineWorker();
    }

    public void getMineWorker() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getUserWorker(0), new Observer<ResultArray<WorkerBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iWorkerDismissActivity.showMessageToast(context.getString(R.string.data_load_failed));
            }

            @Override
            public void onNext(ResultArray<WorkerBean> workerBeanResultArray) {
                if (workerBeanResultArray.result == 1) {
                    List<WorkerBean> data = workerBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iWorkerDismissActivity.showMineWorkerData(data);
                    }else {
                        iWorkerDismissActivity.showMineWorkerData(data);
                    }
                } else {
                    iWorkerDismissActivity.showMessageToast(context.getString(R.string.data_load_failed));
                }
            }
        });
    }

    public void dismissWorker(String workerId) {
        iWorkerDismissActivity.changeLoadingDes("矿工解雇中...");
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).dismissWorker(workerId), new Observer<Result<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iWorkerDismissActivity.showMessageToast("解雇失败");
                iWorkerDismissActivity.dismissedLoading();
            }

            @Override
            public void onNext(Result<String> stringResult) {
                if (stringResult.result == 1) {
                    getMineWorker();
                    iWorkerDismissActivity.sendEventDismissSuccess();
                    iWorkerDismissActivity.showMessageToast("解雇成功");
                } else {
                    iWorkerDismissActivity.showMessageToast("解雇失败");
                }
                iWorkerDismissActivity.dismissedLoading();
            }
        });
    }
}
