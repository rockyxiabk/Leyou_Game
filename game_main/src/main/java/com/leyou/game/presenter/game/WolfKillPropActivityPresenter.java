package com.leyou.game.presenter.game;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.WolfPropBean;
import com.leyou.game.ipresenter.fight.IWolfKillProp;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.WolfKillApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/7/19 下午4:46
 * @Modified Time : 2017/7/19 下午4:46
 */
public class WolfKillPropActivityPresenter {

    private final Context context;
    private final IWolfKillProp iWolfKillProp;

    public WolfKillPropActivityPresenter(Context context, IWolfKillProp iWolfKillProp) {
        this.context = context;
        this.iWolfKillProp = iWolfKillProp;
        getPropList();
    }

    public void getPropList() {
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).getWolfKillProp(1), new Observer<ResultArray<WolfPropBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultArray<WolfPropBean> resultArray) {
                if (resultArray.result == 1) {
                    List<WolfPropBean> data = resultArray.data;
                    iWolfKillProp.showPropList(data);
                }
            }
        });
    }
}
