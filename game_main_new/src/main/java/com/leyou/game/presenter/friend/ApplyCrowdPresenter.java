package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.dao.Crowd;
import com.leyou.game.ipresenter.friend.ICrowdApply;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.CrowdApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/12/7 上午11:56
 * @Modified Time : 2017/12/7 上午11:56
 */
public class ApplyCrowdPresenter {

    private static final String TAG = "ApplyCrowdPresenter";
    private final Context context;
    private final ICrowdApply iCrowdApply;
    private int size = Constants.FIFTEEN;

    public ApplyCrowdPresenter(Context context, ICrowdApply iCrowdApply) {
        this.context = context;
        this.iCrowdApply = iCrowdApply;
        getApplyCrowdList();
    }

    private void getApplyCrowdList() {
        HttpUtil.subscribe(HttpUtil.createApi(CrowdApi.class, Constants.URL).applyList(1, size), new Observer<ResultArray<Crowd>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.toString());
                iCrowdApply.showNullView();
            }

            @Override
            public void onNext(ResultArray<Crowd> crowdResultArray) {
                if (crowdResultArray.result == 1) {
                    List<Crowd> data = crowdResultArray.data;
                    iCrowdApply.showApplyCrowd(data);
                } else {
                    iCrowdApply.showNullView();
                }
            }
        });
    }

    public void agreeApply(Crowd crowd) {
        HttpUtil.subscribe(HttpUtil.createApi(CrowdApi.class, Constants.URL).agreeCrowd(crowd.applyId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iCrowdApply.showMessageToast("操作失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    getApplyCrowdList();
                    iCrowdApply.showMessageToast("已处理");
                } else {
                    iCrowdApply.showMessageToast("操作失败");
                }
            }
        });
    }

    public void ignoreApply(Crowd crowd) {
        HttpUtil.subscribe(HttpUtil.createApi(CrowdApi.class, Constants.URL).ignoreCrowd(crowd.applyId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iCrowdApply.showMessageToast("操作失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    getApplyCrowdList();
                    iCrowdApply.showMessageToast("已处理");
                } else {
                    iCrowdApply.showMessageToast("操作失败");
                }
            }
        });
    }
}
