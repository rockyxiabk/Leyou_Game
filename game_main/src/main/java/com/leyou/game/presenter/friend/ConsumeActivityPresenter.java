package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.ConsumeBean;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.ipresenter.mine.IConsumeActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.UserApi;

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

    private final Context context;
    private final IConsumeActivity iConsumeActivity;
    private int currentPage = 1;
    private Subscription subscribe;
    private boolean isLoadAll = false;
    private Observer<ResultArray<ConsumeBean>> observer = new Observer<ResultArray<ConsumeBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iConsumeActivity.showErrorView();
            iConsumeActivity.showMessageToast(context.getString(R.string.data_load_failed_try));
        }

        @Override
        public void onNext(ResultArray<ConsumeBean> messageBeanResultArray) {
            int result = messageBeanResultArray.result;
            if (1 == result) {
                List<ConsumeBean> data = messageBeanResultArray.data;
                if (null != data && data.size() > 0) {
                    if (data.size() < Constants.FIFTEEN) {
                        isLoadAll = true;
                        iConsumeActivity.setConsumeLoadAll(true);
                        iConsumeActivity.showLoadMoreList(data);
                    }
                    if (data.size() == Constants.FIFTEEN) {
                        isLoadAll = false;
                        currentPage++;
                        iConsumeActivity.setConsumeLoadAll(false);
                        iConsumeActivity.showLoadMoreList(data);
                    }
                } else {
                    isLoadAll = true;
                    iConsumeActivity.showErrorView();
                    iConsumeActivity.showMessageToast(context.getString(R.string.data_load_failed_try));
                }
            } else {
                iConsumeActivity.showErrorView();
                iConsumeActivity.showMessageToast(context.getString(R.string.data_load_failed_try));
            }
        }
    };

    public ConsumeActivityPresenter(Context context, IConsumeActivity iConsumeActivity) {
        this.context = context;
        this.iConsumeActivity = iConsumeActivity;
        getConsumeList();
    }
    public void getConsumeList() {
        currentPage = 1;
        isLoadAll = false;
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getConsumeList(currentPage, Constants.FIFTEEN), observer);
    }

    public void loadMoreConsumeList() {
        if (!isLoadAll) {
            subscribe = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getConsumeList(currentPage, Constants.FIFTEEN), observer);
        } else {
            iConsumeActivity.showMessageToast(context.getString(R.string.no_more_message));
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
