package com.leyou.game.presenter.mine;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.user.BankCardBean;
import com.leyou.game.bean.user.UploadUserInfoBean;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.ipresenter.mine.IModifyUserInfoActivity;
import com.leyou.game.util.BitmapUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.UserApi;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/5/2 下午5:27
 * @Modified Time : 2017/5/2 下午5:27
 */
public class ModifyUserInfoPresenter {
    private static final String TAG = "ModifyUserInfoPresenter";
    private Context context;
    private IModifyUserInfoActivity iModifyUserInfoActivity;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    File file = new File(imageUrlCompressed);
                    if (file.exists()) {
                        file.delete();
                    }
                    break;
                case 2:
                    upgradePersonInfo(UserData.getInstance().getNickname(), UserData.getInstance().getBirthday(), UserData.getInstance().getSex(), data);
                    break;
            }
        }
    };
    private String imageUrlCompressed = "";
    private String data;

    public ModifyUserInfoPresenter(Context context, IModifyUserInfoActivity iModifyUserInfoActivity) {
        this.context = context;
        this.iModifyUserInfoActivity = iModifyUserInfoActivity;
    }

    /**
     * 上传图片
     *
     * @param imagePath
     */
    public void sendImgOkHttp(final String imagePath) {
        iModifyUserInfoActivity.showLoadingView();
        iModifyUserInfoActivity.setLoadingDes("图片上传中...");
        imageUrlCompressed = BitmapUtil.getImageUrlCompressed(imagePath);
        LogUtil.d(TAG, "---" + imageUrlCompressed);
        HttpUtil.uploadImage(imageUrlCompressed, "user/upload", new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                LogUtil.e(TAG, e);
                iModifyUserInfoActivity.showMessage(context.getString(R.string.img_upload_failed));
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String string = response.body().string();
                paramsString(string);
            }
        });
    }

    private void paramsString(String string) {
        try {
            JSONObject object = new JSONObject(string);
            int result = object.getInt("result");
            data = object.getString("data");
            if (1 == result) {
                handler.sendEmptyMessage(1);
                handler.sendEmptyMessageDelayed(2, 1000);
                iModifyUserInfoActivity.dismissedLoading();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            iModifyUserInfoActivity.dismissedLoading();
            iModifyUserInfoActivity.toOtherActivity(1);
        }
    }

    /**
     * 修改用户信息
     *
     * @param nickName
     * @param birthday
     * @param sex
     * @param headUrl
     */
    public void upgradePersonInfo(String nickName, long birthday, final int sex, String headUrl) {
        iModifyUserInfoActivity.showLoadingView();
        iModifyUserInfoActivity.setLoadingDes("资料同步中...");
        UploadUserInfoBean uploadUserInfoBean = new UploadUserInfoBean();
        uploadUserInfoBean.birthday = birthday;
        uploadUserInfoBean.headImgName = headUrl;
        uploadUserInfoBean.nickname = nickName;
        uploadUserInfoBean.sex = sex;
        HttpUtil.subscribe(HttpUtil.createApi(com.leyou.game.util.newapi.UserApi.class, Constants.URL).updateUserInfo(uploadUserInfoBean), new Observer<Result<UserData.UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iModifyUserInfoActivity.dismissedLoading();
            }

            @Override
            public void onNext(Result<UserData.UserInfo> userInfoResult) {
                if (userInfoResult.result == 1) {
                    UserData.getInstance().saveUserNickNameAndPicture(userInfoResult.data.getNickname(), userInfoResult.data.getHeadImgUrl(), UserData.getInstance().getIDNo());
                    UserData.getInstance().saveModifyInfo(userInfoResult.data);
                    EventBus.getDefault().post(new RefreshEvent(RefreshEvent.REFRESH, RefreshEvent.MINE));
                    iModifyUserInfoActivity.dismissedLoading();
                } else {
                    iModifyUserInfoActivity.dismissedLoading();
                }
            }
        });
    }
}
