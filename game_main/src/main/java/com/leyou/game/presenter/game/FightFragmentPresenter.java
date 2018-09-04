package com.leyou.game.presenter.game;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.FightGameBean;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.ipresenter.fight.IFightGameFragment;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.FightGameApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/7/15 下午12:17
 * @Modified Time : 2017/7/15 下午12:17
 */
public class FightFragmentPresenter {

    private final Context context;
    private final IFightGameFragment iFightGameFragment;

    public FightFragmentPresenter(Context context, IFightGameFragment iFightGameFragment) {
        this.context = context;
        this.iFightGameFragment = iFightGameFragment;
        getFightData();
    }

    public void getFightData() {
        HttpUtil.subscribe(HttpUtil.createApi(FightGameApi.class, Constants.WOLF_KILL_URL).getFightGameList(1, Constants.FIFTEEN), new Observer<ResultArray<FightGameBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iFightGameFragment.isShowNullListView(true);
            }

            @Override
            public void onNext(ResultArray<FightGameBean> resultArray) {
                if (resultArray.result == 1) {
                    List<FightGameBean> data = resultArray.data;
                    if (null != data && data.size() > 0) {
                        iFightGameFragment.isShowNullListView(false);
                        iFightGameFragment.showRefreshData(data);
                    } else {
                        iFightGameFragment.isShowNullListView(true);
                    }
                } else {
                    iFightGameFragment.isShowNullListView(true);
                }
            }
        });
    }
}
