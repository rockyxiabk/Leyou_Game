package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.MessageBean;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.ipresenter.friend.IMessageActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.UserApi;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/5/5 上午10:38
 * @Modified Time : 2017/5/5 上午10:38
 */
public class MessagePresenter {

    private IMessageActivity iMessageActivity;
    private Context context;
    private int currentPage = 1;
    private Subscription subscribe;
    private boolean isLoadAll = false;
    private Observer<ResultArray<MessageBean>> observer = new Observer<ResultArray<MessageBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iMessageActivity.showErrorView();
            iMessageActivity.showMessageToast(context.getString(R.string.data_load_failed_try));
        }

        @Override
        public void onNext(ResultArray<MessageBean> messageBeanResultArray) {
            int result = messageBeanResultArray.result;
            if (1 == result) {
                List<MessageBean> data = messageBeanResultArray.data;
                if (null != data && data.size() > 0) {
                    if (data.size() < Constants.TWENTY) {
                        isLoadAll = true;
                        iMessageActivity.setMessageLoadAll(true);
                        iMessageActivity.showLoadMoreList(data);
                    }
                    if (data.size() == Constants.TWENTY) {
                        isLoadAll = false;
                        currentPage++;
                        iMessageActivity.setMessageLoadAll(false);
                        iMessageActivity.showLoadMoreList(data);
                    }
                } else {
                    isLoadAll = true;
                    iMessageActivity.setMessageLoadAll(true);
//                    iMessageActivity.showErrorView();
//                    iMessageActivity.showMessageToast(context.getString(R.string.data_load_failed_try));
                }
            } else {
                iMessageActivity.showErrorView();
                iMessageActivity.showMessageToast(context.getString(R.string.data_load_failed_try));
            }
        }
    };

    public MessagePresenter(IMessageActivity iMessageActivity, Context context) {
        this.context = context;
        this.iMessageActivity = iMessageActivity;

        getMessageList();
    }

    public void getMessageList() {
        currentPage = 1;
        isLoadAll = false;
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getMessageList(currentPage, Constants.TWENTY), observer);
    }

    public void loadMoreMessageList() {
        if (!isLoadAll) {
            subscribe = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getMessageList(currentPage, Constants.TWENTY), observer);
        } else {
            iMessageActivity.showMessageToast(context.getString(R.string.no_more_message));
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
