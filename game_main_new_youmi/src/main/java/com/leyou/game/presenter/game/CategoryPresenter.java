package com.leyou.game.presenter.game;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.ipresenter.game.ICategory;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.GameApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.game
 *
 * @author : rocky
 * @Create Time : 2017/10/31 下午9:39
 * @Modified Time : 2017/10/31 下午9:39
 */
public class CategoryPresenter {

    private Context context;
    private ICategory iCategory;

    public CategoryPresenter(Context context, ICategory iCategory) {
        this.context = context;
        this.iCategory = iCategory;
        getCategory();
    }

    public void getCategory() {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getCategoryTypeList(), new Observer<ResultArray<GameBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iCategory.showDataList(null);
            }

            @Override
            public void onNext(ResultArray<GameBean> gameBeanResultArray) {
                if (gameBeanResultArray.result == 1) {
                    List<GameBean> data = gameBeanResultArray.data;
                    iCategory.showDataList(data);
                } else {
                    iCategory.showDataList(null);
                }
            }
        });
    }
}
