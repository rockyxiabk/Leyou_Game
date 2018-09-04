package com.leyou.game.presenter.treasure;

import android.content.Context;
import android.text.TextUtils;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.bean.WorkerExtBean;
import com.leyou.game.ipresenter.treasure.IWorkerUpgradeActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.TreasureApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/6/5 下午4:35
 * @Modified Time : 2017/6/5 下午4:35
 */
public class WorkerUpgradePresenter {
    private Context context;
    private IWorkerUpgradeActivity iWorkerUpgradeActivity;

    public WorkerUpgradePresenter(Context context, IWorkerUpgradeActivity iWorkerUpgradeActivity) {
        this.context = context;
        this.iWorkerUpgradeActivity = iWorkerUpgradeActivity;
        getMineWorker();
    }

    public void getMineWorker() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getUserWorker(1), new Observer<ResultArray<WorkerBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iWorkerUpgradeActivity.showMessageToast(context.getString(R.string.data_load_failed));
            }

            @Override
            public void onNext(ResultArray<WorkerBean> workerBeanResultArray) {
                if (workerBeanResultArray.result == 1) {
                    List<WorkerBean> data = workerBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iWorkerUpgradeActivity.showMineWorkerData(data);
                    }
                } else {
                    iWorkerUpgradeActivity.showMessageToast(context.getString(R.string.data_load_failed));
                }
            }
        });
    }

    public void upgradeWorker(final WorkerBean workerBean) {
        iWorkerUpgradeActivity.changeLoadingDes("矿工升级中...");
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).upgradeWorker(workerBean.id), new Observer<Result<WorkerExtBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iWorkerUpgradeActivity.showMessageToast("升级失败");
                iWorkerUpgradeActivity.dismissedLoading();
                iWorkerUpgradeActivity.showUpgradeFailed();
            }

            @Override
            public void onNext(Result<WorkerExtBean> workerExtBeanResult) {
                if (workerExtBeanResult.result == 1) {
                    getMineWorker();
                    iWorkerUpgradeActivity.sendEventUpgradeSuccess();
                    UserData.getInstance().setDiamonds(UserData.getInstance().getDiamonds() - workerBean.levelUpConsume);
                } else {
                    String message = workerExtBeanResult.data.message;
                    if (!TextUtils.isEmpty(message)) {
                        UserData.getInstance().setDiamonds(UserData.getInstance().getDiamonds() - workerBean.levelUpConsume);
                        iWorkerUpgradeActivity.showMessageToast(message);
                    }
                    iWorkerUpgradeActivity.showUpgradeFailed();
                }
                iWorkerUpgradeActivity.dismissedLoading();
            }
        });
    }
}
