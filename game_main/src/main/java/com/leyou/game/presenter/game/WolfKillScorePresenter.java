package com.leyou.game.presenter.game;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.WolfKillMilitaryBean;
import com.leyou.game.bean.WolfRoleBean;
import com.leyou.game.ipresenter.fight.IWolfKillScore;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.WolfKillApi;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/7/16 下午4:02
 * @Modified Time : 2017/7/16 下午4:02
 */
public class WolfKillScorePresenter {

    private final Context context;
    private final IWolfKillScore iWolfKillScore;
    private Subscription subscribe;
    private Subscription subscribe1;
    private Observer<ResultArray<WolfKillMilitaryBean>> observerMilitary = new Observer<ResultArray<WolfKillMilitaryBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iWolfKillScore.setMilitaryIsLoadAll(true);
            e.printStackTrace();
        }

        @Override
        public void onNext(ResultArray<WolfKillMilitaryBean> wolfKillMilitaryBeanResultArray) {
            if (wolfKillMilitaryBeanResultArray.result == 1) {
                List<WolfKillMilitaryBean> data = wolfKillMilitaryBeanResultArray.data;
                if (null != data) {
                    if (currentPage == 1) {
                        if (data.size() == Constants.FIFTEEN) {
                            currentPage++;
                            iWolfKillScore.setMilitaryIsLoadAll(false);
                            iWolfKillScore.refreshFightResultList(data);
                        } else {
                            iWolfKillScore.setMilitaryIsLoadAll(true);
                            iWolfKillScore.refreshFightResultList(data);
                        }
                    } else {
                        if (data.size() == Constants.FIFTEEN) {
                            currentPage++;
                            iWolfKillScore.setMilitaryIsLoadAll(false);
                        } else {
                            iWolfKillScore.setMilitaryIsLoadAll(true);
                        }
                        iWolfKillScore.loadMoreFightResultList(data);
                    }
                } else {
                    iWolfKillScore.setMilitaryIsLoadAll(true);
                }
            } else {
                iWolfKillScore.setMilitaryIsLoadAll(true);
            }
        }
    };
    private Observer<ResultArray<WolfRoleBean>> observerRole = new Observer<ResultArray<WolfRoleBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(ResultArray<WolfRoleBean> wolfRoleBeanResultArray) {
            if (wolfRoleBeanResultArray.result == 1) {
                List<WolfRoleBean> data = wolfRoleBeanResultArray.data;
                iWolfKillScore.showWolfRolePercent(data);
            }
        }
    };
    private int currentPage;
    private boolean isLoadAll;

    public WolfKillScorePresenter(Context context, IWolfKillScore iWolfKillScore) {
        this.context = context;
        this.iWolfKillScore = iWolfKillScore;
        getRolePercent();
        getMilitaryList();
    }

    public void getMilitaryList() {
        currentPage = 1;
        isLoadAll = false;
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).getMilitary(currentPage, Constants.FIFTEEN), observerMilitary);
    }

    public void loadMoreMilitary() {
        if (!isLoadAll) {
            subscribe = HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).getMilitary(currentPage, Constants.FIFTEEN), observerMilitary);
        } else {

        }
    }

    private void getRolePercent() {
        subscribe1 = HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).getRoleWinRate(), observerRole);
    }
}
