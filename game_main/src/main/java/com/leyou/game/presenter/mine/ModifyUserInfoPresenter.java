package com.leyou.game.presenter.mine;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.ipresenter.mine.IModifyUserInfoActivity;
import com.leyou.game.util.BitmapUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.api.UserApi;

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
                    if (!TextUtils.isEmpty(nickName) || birthday > 0) {
                        upgradePersonInfo(nickName, birthday, sex);
                    } else {
                        iModifyUserInfoActivity.dismissedLoading();
                        iModifyUserInfoActivity.showMessage("保存成功");
                        iModifyUserInfoActivity.toOtherActivity(0);
                    }
                    break;
            }
        }
    };
    private String imageUrlCompressed = "";
    private String url;
    private long birthday;
    private int sex;
    private String nickName;

    public ModifyUserInfoPresenter(Context context, IModifyUserInfoActivity iModifyUserInfoActivity) {
        this.context = context;
        this.iModifyUserInfoActivity = iModifyUserInfoActivity;
    }

    public void savePersonInfo(String nickName, final String url, long birthday, int sex) {
        this.url = url;
        this.birthday = birthday;
        this.sex = sex;
        this.nickName = nickName;
        if (!TextUtils.isEmpty(url)) {
            iModifyUserInfoActivity.setLoadingDes("图片上传中...");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    sendImgOkHttp(url);
                }
            });
        }
        if (!TextUtils.isEmpty(nickName) || birthday > 0) {
            iModifyUserInfoActivity.setLoadingDes("资料上传中...");
            upgradePersonInfo(nickName, birthday, sex);
        } else {
            iModifyUserInfoActivity.setLoadingDes("资料上传中...");
            upgradePersonInfo(UserData.getInstance().getNickname(), UserData.getInstance().getBirthday(), sex);
        }
    }

    /**
     * 上传图片
     *
     * @param imagePath
     */
    private void sendImgOkHttp(final String imagePath) {
        imageUrlCompressed = BitmapUtil.getImageUrlCompressed(imagePath);
        LogUtil.d(TAG, "---" + imageUrlCompressed);
        HttpUtil.uploadImage(imageUrlCompressed, "userInfo.do?method=pictureUpload", new okhttp3.Callback() {
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
            if (1 == result) {
                handler.sendEmptyMessage(1);
                handler.sendEmptyMessageDelayed(2, 1000);
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
     */
    private void upgradePersonInfo(String nickName, long birthday, final int sex) {
        UserData.UserInfo userInfo = new UserData.UserInfo();
        userInfo.nickname = nickName;
        if (birthday == 0) {
            userInfo.birthday = UserData.getInstance().getBirthday();
        } else {
            userInfo.birthday = birthday;
        }
        userInfo.sex = sex;
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).upgradeUserInfo(userInfo), new Observer<Result<UserData.UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iModifyUserInfoActivity.showMessage(context.getString(R.string.save_failed));
                iModifyUserInfoActivity.dismissedLoading();
            }

            @Override
            public void onNext(Result<UserData.UserInfo> stringResult) {
                int result = stringResult.result;
                if (1 == result) {
                    UserData.getInstance().saveModifyInfo(stringResult.data);
                    iModifyUserInfoActivity.showMessage(context.getString(R.string.save_success));
                    iModifyUserInfoActivity.toOtherActivity(0);
                } else {
                    iModifyUserInfoActivity.showMessage(context.getString(R.string.save_failed));
                }
                iModifyUserInfoActivity.dismissedLoading();
            }
        });
    }
}
