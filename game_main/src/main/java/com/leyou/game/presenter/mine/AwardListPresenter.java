package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.AwardInfoBean;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.ipresenter.mine.IAwardListActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.UserApi;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/6/19 下午5:33
 * @Modified Time : 2017/6/19 下午5:33
 */
public class AwardListPresenter {
    private Context context;
    private IAwardListActivity iAwardListActivity;
    private Subscription subscribe;
    private Observer<ResultArray<AwardInfoBean>> observer = new Observer<ResultArray<AwardInfoBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iAwardListActivity.showErrorView();
        }

        @Override
        public void onNext(ResultArray<AwardInfoBean> awardInfoBeanResultArray) {
            if (awardInfoBeanResultArray.result == 1) {
                List<AwardInfoBean> data = awardInfoBeanResultArray.data;
                if (null != data && data.size() > 0) {
                    iAwardListActivity.showMyAwardList(data);
                } else {
                    iAwardListActivity.showErrorView();
                }
            } else {
                iAwardListActivity.showErrorView();
            }
        }
    };

    public AwardListPresenter(Context context, IAwardListActivity iAwardListActivity) {
        this.context = context;
        this.iAwardListActivity = iAwardListActivity;
        getUserAwardList();
    }

    public void getUserAwardList() {
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getAwardList(), observer);
    }

    /**
     * 解除订阅，停止数据请求
     */
    public void destroy() {
        if (null != subscribe && subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }
}
