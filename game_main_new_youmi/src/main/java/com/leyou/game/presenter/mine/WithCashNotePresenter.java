package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.user.WithCashNoteBean;
import com.leyou.game.ipresenter.mine.IWithCashNote;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.UserApi;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/10 下午4:00
 * @Modified Time : 2017/8/10 下午4:00
 */
public class WithCashNotePresenter {

    private static final String TAG = "ConsumeActivityPresenter";
    private Context context;
    private IWithCashNote iConsumeActivity;
    private int currentPage = 1;
    private Subscription subscribe;
    private boolean isLoadAll = false;
    private int pageSize = Constants.TWENTY;
    private Observer<ResultArray<WithCashNoteBean>> observer = new Observer<ResultArray<WithCashNoteBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            LogUtil.e(TAG, e.toString());
            iConsumeActivity.showNullView();
            iConsumeActivity.showMessageToast(context.getString(R.string.data_load_failed_try));
        }

        @Override
        public void onNext(ResultArray<WithCashNoteBean> messageBeanResultArray) {
            int result = messageBeanResultArray.result;
            if (1 == result) {
                List<WithCashNoteBean> data = messageBeanResultArray.data;
                if (null != data && data.size() > 0) {
                    if (currentPage == 1) {
                        if (data.size() < pageSize) {
                            isLoadAll = true;
                            iConsumeActivity.setConsumeLoadAll(true);
                            iConsumeActivity.showDataList(data);
                        } else if (data.size() == pageSize) {
                            isLoadAll = false;
                            currentPage++;
                            iConsumeActivity.setConsumeLoadAll(false);
                            iConsumeActivity.showDataList(data);
                        }
                    } else {
                        if (data.size() < pageSize) {
                            isLoadAll = true;
                            iConsumeActivity.setConsumeLoadAll(true);
                            iConsumeActivity.showLoadMoreList(data);
                        } else if (data.size() == pageSize) {
                            isLoadAll = false;
                            currentPage++;
                            iConsumeActivity.setConsumeLoadAll(false);
                            iConsumeActivity.showLoadMoreList(data);
                        }
                    }
                } else {
                    isLoadAll = true;
                    iConsumeActivity.showMessageToast("暂无提现记录");
                    iConsumeActivity.showNullView();
                    iConsumeActivity.dismissedLoading();
                }
            } else {
                isLoadAll = true;
                iConsumeActivity.showNullView();
            }
        }
    };

    public WithCashNotePresenter(Context context, IWithCashNote iConsumeActivity) {
        this.context = context;
        this.iConsumeActivity = iConsumeActivity;
        getConsumeList();
    }


    public void getConsumeList() {
        iConsumeActivity.showLoading();
        iConsumeActivity.changeLoadingDes(context.getString(R.string.data_loading));
        currentPage = 1;
        isLoadAll = false;
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getWithCashHistory(currentPage, pageSize), observer);
    }

    public void loadMoreConsumeList() {
        iConsumeActivity.showLoading();
        iConsumeActivity.changeLoadingDes(context.getString(R.string.data_loading));
        if (!isLoadAll) {
            subscribe = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getWithCashHistory(currentPage, pageSize), observer);
        } else {
            iConsumeActivity.dismissedLoading();
        }
    }

    /**
     * 解除订阅，停止数据请求
     */
    public void unSubscribe() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }
}
