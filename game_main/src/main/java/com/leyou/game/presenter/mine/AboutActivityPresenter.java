package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UpdateAppBean;
import com.leyou.game.ipresenter.mine.IAboutActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.AppApi;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/5/15 上午10:17
 * @Modified Time : 2017/5/15 上午10:17
 */
public class AboutActivityPresenter {
    private Context context;
    private IAboutActivity iAboutActivity;
    private Subscription subscribe;
    private Observer<Result<UpdateAppBean>> observer = new Observer<Result<UpdateAppBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iAboutActivity.dismissedLoading();
            iAboutActivity.showMessageToast("已是最新版本");
        }

        @Override
        public void onNext(Result<UpdateAppBean> updateAppBeanResult) {
            if (updateAppBeanResult.result == 1) {
                UpdateAppBean data = updateAppBeanResult.data;
                if (null != data) {
                    if (Constants.getVerCode() < data.versionCode) {
                        iAboutActivity.showNewVersionView(data);
                    }
                } else {
                    iAboutActivity.showMessageToast("已是最新版本");
                    iAboutActivity.dismissedLoading();
                }
            }
        }
    };

    public AboutActivityPresenter(Context context, IAboutActivity iAboutActivity) {
        this.context = context;
        this.iAboutActivity = iAboutActivity;
    }

    public void checkUpgrade() {
        iAboutActivity.showLoading();
        iAboutActivity.changeLoadingDes(context.getString(R.string.check_upgrading));
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(AppApi.class, Constants.URL).checkUpgrade(Constants.getVerCode()), observer);
    }

    public void destroy() {
        if (null != subscribe && subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }
}
