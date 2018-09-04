package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.ipresenter.mine.IModifyWorkerDialog;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.TreasureApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/6/20 上午11:22
 * @Modified Time : 2017/6/20 上午11:22
 */
public class ModifyWorkerDialogPresenter {
    private final String id;
    private Context context;
    private IModifyWorkerDialog iModifyWorkerDialog;

    public ModifyWorkerDialogPresenter(Context context, IModifyWorkerDialog iModifyWorkerDialog, String id) {
        this.context = context;
        this.iModifyWorkerDialog = iModifyWorkerDialog;
        this.id = id;
        getMineWorker();
    }

    public void getMineWorker() {
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
                        iModifyWorkerDialog.showMineWorker(data);
                    }
                }
            }
        });
    }

    public void getMineCanUseWorker(int type,int level) {
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
                        iModifyWorkerDialog.showMineCanUseWorker(data);
                    }
                }
            }
        });
    }

    public void modifyWorker(final String id, List<WorkerBean> data) {
        String json = "";
        for (int i = 0; i < data.size(); i++) {
            json += data.get(i).id + ",";
        }
        String json1 = json.substring(0, json.length() - 1);
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).modifyTreasureWorker(id, json1), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iModifyWorkerDialog.showMessageToast("修改失败");
            }

            @Override
            public void onNext(Result stringResult) {
                if (stringResult.result == 1) {
                    iModifyWorkerDialog.showMessageToast("修改成功");
                    iModifyWorkerDialog.modifyTreasure(true);
                } else {
                    iModifyWorkerDialog.showMessageToast("修改失败");
                }
            }
        });
    }

    public void deleteTreasure() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).deleteTreasure(id), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iModifyWorkerDialog.showMessageToast("废除宝窟失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iModifyWorkerDialog.showMessageToast("废除宝窟成功");
                    iModifyWorkerDialog.deleteTreasure(true);
                } else {
                    iModifyWorkerDialog.showMessageToast("废除宝窟失败");
                }
            }
        });
    }
}
