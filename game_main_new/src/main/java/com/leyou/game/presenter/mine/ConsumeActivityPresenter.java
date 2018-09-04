package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.diamond.DiamondBean;
import com.leyou.game.ipresenter.mine.IConsumeActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.DiamondApi;

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
public class ConsumeActivityPresenter {

    private static final String TAG = "ConsumeActivityPresenter";
    private Context context;
    private IConsumeActivity iConsumeActivity;
    private int currentType = 0;
    private int currentPage = 1;
    private Subscription subscribe;
    private boolean isLoadAll = false;
    private int pageSize = Constants.TWENTY;
    private Observer<ResultArray<DiamondBean>> observer = new Observer<ResultArray<DiamondBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            LogUtil.e(TAG, e.toString());
            iConsumeActivity.showErrorView();
            iConsumeActivity.showMessageToast(context.getString(R.string.data_load_failed_try));
        }

        @Override
        public void onNext(ResultArray<DiamondBean> messageBeanResultArray) {
            int result = messageBeanResultArray.result;
            if (1 == result) {
                List<DiamondBean> data = messageBeanResultArray.data;
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
                    iConsumeActivity.showMessageToast("暂无消费记录");
                    iConsumeActivity.showNullView();
                    iConsumeActivity.dismissedLoading();
                }
            } else {
                isLoadAll = true;
                iConsumeActivity.showNullView();
            }
        }
    };

    public ConsumeActivityPresenter(Context context, IConsumeActivity iConsumeActivity, int currentType) {
        this.context = context;
        this.iConsumeActivity = iConsumeActivity;
        this.currentType = currentType;
        getConsumeTab();
        getConsumeList(currentType);
    }

    public void getConsumeTab() {
        HttpUtil.subscribe(HttpUtil.createApi(DiamondApi.class, Constants.URL).getDiamondConsumeRecordType(), new Observer<ResultArray<DiamondBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                iConsumeActivity.showConsumeTab(null);
            }

            @Override
            public void onNext(ResultArray<DiamondBean> diamondBeanResultArray) {
                if (diamondBeanResultArray.result == 1) {
                    iConsumeActivity.showConsumeTab(diamondBeanResultArray.data);
                } else {
                    iConsumeActivity.showConsumeTab(null);
                }
            }
        });
    }

    public void getConsumeList(int type) {
        iConsumeActivity.showLoading();
        iConsumeActivity.changeLoadingDes(context.getString(R.string.data_loading));
        currentPage = 1;
        currentType = type;
        isLoadAll = false;
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(DiamondApi.class, Constants.URL).getDiamondConsumeRecordList(currentPage, pageSize, type), observer);
    }

    public void loadMoreConsumeList() {
        iConsumeActivity.showLoading();
        iConsumeActivity.changeLoadingDes(context.getString(R.string.data_loading));
        if (!isLoadAll) {
            subscribe = HttpUtil.subscribe(HttpUtil.createApi(DiamondApi.class, Constants.URL).getDiamondConsumeRecordList(currentPage, pageSize, currentType), observer);
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
