package com.leyou.game.presenter.friend;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.leyou.game.R;
import com.leyou.game.bean.UserData;
import com.leyou.game.ipresenter.friend.ICreateCrowdInfo;
import com.leyou.game.util.BitmapUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Description : com.leyou.game.presenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/12/6 下午2:43
 * @Modified Time : 2017/12/6 下午2:43
 */
public class CreateCrowdInfoPresenter {

    private final Context context;
    private final ICreateCrowdInfo iCreateCrowdInfo;
    private String imageUrlCompressed = "";
    private String data;
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
                    break;
            }
        }
    };

    public CreateCrowdInfoPresenter(Context context, ICreateCrowdInfo iCreateCrowdInfo) {
        this.context = context;
        this.iCreateCrowdInfo = iCreateCrowdInfo;
    }

    /**
     * 上传图片
     *
     * @param imagePath
     */
    public void sendImgOkHttp(final String imagePath) {
        imageUrlCompressed = BitmapUtil.getImageUrlCompressed(imagePath);
        HttpUtil.uploadImage(imageUrlCompressed, "group/upload", new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                iCreateCrowdInfo.showMessageToast(context.getString(R.string.img_upload_failed));
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
                iCreateCrowdInfo.setImageUrl(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
