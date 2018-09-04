package com.leyou.game.presenter.treasure;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.PropBean;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.ipresenter.treasure.IPropFragment;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.TreasureApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/6/30 上午11:08
 * @Modified Time : 2017/6/30 上午11:08
 */
public class PropFragmentPresenter {
    private Context context;
    private IPropFragment iPropFragment;

    public PropFragmentPresenter(Context context, IPropFragment iPropFragment) {
        this.context = context;
        this.iPropFragment = iPropFragment;
        getShopProp();
    }

    public void getShopProp() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getShopProp(0, 0), new Observer<ResultArray<PropBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iPropFragment.showPropListData(null);
            }

            @Override
            public void onNext(ResultArray<PropBean> propBeanResultArray) {
                if (propBeanResultArray.result == 1) {
                    List<PropBean> data = propBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iPropFragment.showPropListData(data);
                    } else {
                        iPropFragment.showPropListData(null);
                    }
                }
            }
        });
    }
}
