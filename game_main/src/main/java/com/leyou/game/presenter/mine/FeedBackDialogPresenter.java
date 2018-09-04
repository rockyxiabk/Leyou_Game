package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.FeedBackInfo;
import com.leyou.game.bean.Result;
import com.leyou.game.ipresenter.mine.IFeedBackDialog;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/4/26 下午3:39
 * @Modified Time : 2017/4/26 下午3:39
 */
public class FeedBackDialogPresenter {
    private Context context;
    private IFeedBackDialog iFeedBackDialog;

    public FeedBackDialogPresenter(Context context, IFeedBackDialog iFeedBackDialog) {
        this.context = context;
        this.iFeedBackDialog = iFeedBackDialog;
    }

    public void commit(String title, String content, String contact) {
        FeedBackInfo feedBackInfo = new FeedBackInfo(title, content, contact);
        HttpUtil.createApi(UserApi.class, Constants.URL).feedBack(feedBackInfo).enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                if (response.isSuccessful()) {
                    int result = response.body().result;
                    if (0 != result) {
                        iFeedBackDialog.showSuccess("提交成功");
                    } else {
                        iFeedBackDialog.showError("提交失败,请重新提交反馈信息！");
                    }
                } else {
                    iFeedBackDialog.showError("提交失败,请重新提交反馈信息！");
                }
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {
                iFeedBackDialog.showError("提交失败,请重新提交反馈信息！");
            }
        });
    }
}
