package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.friend.IAddCrowdMember;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.api.FriendApi;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/31 下午6:27
 * @Modified Time : 2017/8/31 下午6:27
 */
public class AddCrowdMemberPresenter {
    private static final String TAG = "AddCrowdMemberPresenter";

    private Context context;
    private IAddCrowdMember iAddCrowdMember;
    private String crowdId;

    public AddCrowdMemberPresenter(Context context, IAddCrowdMember iAddCrowdMember, String crowdId) {
        this.context = context;
        this.iAddCrowdMember = iAddCrowdMember;
        this.crowdId = crowdId;
        getCrowdNumber(crowdId);
    }

    public void getCrowdNumber(String crowdId) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getCrowdMember(crowdId), new Observer<ResultArray<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
            }

            @Override
            public void onNext(ResultArray<Friend> contactBeanResultArray) {
                if (contactBeanResultArray.result == 1) {
                    List<Friend> data = contactBeanResultArray.data;
                    iAddCrowdMember.showCrowdMemberAdapter(data);
                }
            }
        });
    }

    public void inviteToCrowd(List<Friend> list) {
        String json = "";
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                json = json + list.get(i).getUserId() + ",";
            }
        }
        String json1 = "";
        if (json.length() > 0) {
            json1 = json.substring(0, json.length() - 1);
        } else {
            return;
        }
        LogUtil.d(TAG, "--" + json1);

        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).inviteFriendToCrowd(json1, crowdId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                iAddCrowdMember.showInviteResult(false);
            }

            @Override
            public void onNext(Result crowdBeanResult) {
                if (crowdBeanResult.result == 1) {
                    iAddCrowdMember.showInviteResult(true);
                } else {
                    iAddCrowdMember.showInviteResult(false);
                }
            }
        });
    }
}
