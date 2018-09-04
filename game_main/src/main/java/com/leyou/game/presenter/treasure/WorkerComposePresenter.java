package com.leyou.game.presenter.treasure;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.bean.WorkerExtBean;
import com.leyou.game.ipresenter.treasure.IWorkerComposeFragment;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.TreasureApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/6/6 下午6:31
 * @Modified Time : 2017/6/6 下午6:31
 */
public class WorkerComposePresenter {
    private Context context;
    private IWorkerComposeFragment iWorkerCompose;
    private int typeId;

    public WorkerComposePresenter(Context context, IWorkerComposeFragment iWorkerCompose, int typeId) {
        this.context = context;
        this.iWorkerCompose = iWorkerCompose;
        this.typeId = typeId;
        getMineWorker(typeId);
    }

    public void getMineWorker(int anInt) {
        this.typeId = anInt;
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getUserWorker(anInt), new Observer<ResultArray<WorkerBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iWorkerCompose.showMessageToast(context.getString(R.string.data_load_failed));
            }

            @Override
            public void onNext(ResultArray<WorkerBean> workerBeanResultArray) {
                if (workerBeanResultArray.result == 1) {
                    List<WorkerBean> data = workerBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iWorkerCompose.showMineWorkerData(data);
                    } else {
                        iWorkerCompose.showMineWorkerData(null);
                    }
                } else {
                    iWorkerCompose.showMessageToast(context.getString(R.string.data_load_failed));
                }
            }
        });
    }

    public void composeWorker(final WorkerBean worker1, WorkerBean worker2) {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).composeWorker(worker1.id, worker2.id), new Observer<Result<WorkerExtBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iWorkerCompose.showComposeResult(false, "合成失败");
                iWorkerCompose.dismissedLoading();
            }

            @Override
            public void onNext(Result<WorkerExtBean> workerExtBeanResult) {
                if (workerExtBeanResult.result == 1) {
                    iWorkerCompose.showComposeResult(true, null);
                    UserData.getInstance().setDiamonds(UserData.getInstance().getDiamonds()-worker1.levelUpConsume-worker1.levelUpConsume);
                } else {
                    iWorkerCompose.showComposeResult(false, workerExtBeanResult.data.message);
                }
                getMineWorker(typeId);
                iWorkerCompose.sendEventComposedSuccess();
                iWorkerCompose.dismissedLoading();
            }
        });
    }
}
