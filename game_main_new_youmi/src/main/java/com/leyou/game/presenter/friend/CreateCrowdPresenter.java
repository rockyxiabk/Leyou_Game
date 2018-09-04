package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.friend.ICreateCrowd;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.CrowdApi;
import com.leyou.game.util.newapi.FriendApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/25 下午3:28
 * @Modified Time : 2017/8/25 下午3:28
 */
public class CreateCrowdPresenter {

    private static final String TAG = "CreateCrowdPresenter";
    private Context context;
    private ICreateCrowd iCreateCrowd;

    public CreateCrowdPresenter(Context context, ICreateCrowd iCreateCrowd) {
        this.context = context;
        this.iCreateCrowd = iCreateCrowd;
    }

    public void createCrowd(List<Friend> list, Crowd crowdInfo) {
        for (int i = 0; i < list.size(); i++) {
            boolean ignoreCase = list.get(i).getUserId().equalsIgnoreCase(UserData.getInstance().getId());
            if (ignoreCase) {
                list.remove(i);
            }
        }
        HttpUtil.subscribe(HttpUtil.createApi(CrowdApi.class, Constants.URL).createCrowd(list,
                crowdInfo.getHeadImgUrl(), crowdInfo.getName(), crowdInfo.getIntroduction()), new Observer<Result<Crowd>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                iCreateCrowd.showMessageToast("创建失败");
                iCreateCrowd.showCreateCrowdResult(false, null);
            }

            @Override
            public void onNext(Result<Crowd> crowdBeanResult) {
                if (crowdBeanResult.result == 1) {
                    Crowd crowd = crowdBeanResult.data;
                    if (null != crowd) {
                        iCreateCrowd.showCreateCrowdResult(true, crowd);
                    } else {
                        iCreateCrowd.showMessageToast("创建失败");
                        iCreateCrowd.showCreateCrowdResult(false, null);
                    }
                } else {
                    iCreateCrowd.showMessageToast("创建失败");
                    iCreateCrowd.showCreateCrowdResult(false, null);
                }
            }
        });
    }
}
