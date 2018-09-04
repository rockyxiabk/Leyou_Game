package com.leyou.game.presenter.friend;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.dao.Friend;
import com.leyou.game.event.CrowdEvent;
import com.leyou.game.ipresenter.friend.IDeleteCrowdMember;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.CrowdApi;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/31 下午9:58
 * @Modified Time : 2017/8/31 下午9:58
 */
public class DeleteCrowdMemberPresenter {

    private static final String TAG = "DeleteCrowdMemberPresenter";
    private Context context;
    private IDeleteCrowdMember iDeleteCrowdMember;
    private String crowdId;

    public DeleteCrowdMemberPresenter(Context context, IDeleteCrowdMember iDeleteCrowdMember, String crowdId) {
        this.context = context;
        this.iDeleteCrowdMember = iDeleteCrowdMember;
        this.crowdId = crowdId;
        getCrowdNumber(crowdId);
    }

    public void getCrowdNumber(String crowdId) {
        HttpUtil.subscribe(HttpUtil.createApi(CrowdApi.class, Constants.URL).getGroupMember(crowdId), new Observer<ResultArray<Friend>>() {
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
                    iDeleteCrowdMember.showCrowdMemberAdapter(data);
                }
            }
        });
    }

    public void deleteCrowdMember(final List<Friend> friendList) {
        HttpUtil.subscribe(HttpUtil.createApi(CrowdApi.class, Constants.URL).removeGroup(friendList, crowdId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iDeleteCrowdMember.showMessageToast("移除失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iDeleteCrowdMember.showMessageToast("移除成功");
                    EventBus.getDefault().post(new CrowdEvent(friendList.size()));
                } else {
                    iDeleteCrowdMember.showMessageToast("移除失败");
                }
            }
        });
    }
}
