package com.leyou.game.util.newapi;

import com.leyou.game.bean.AdBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UpdateAppBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : 应用相关的接口Api
 *
 * @author : rocky
 * @Create Time : 2017/10/26 下午3:57
 * @Modified By: rocky
 * @Modified Time : 2017/10/26 下午3:57
 */
public interface SystemApi {
    /**
     * 短信发送验证码
     *
     * @param phone
     * @return
     */
    @GET("sms/sendCode")
    Observable<Result> sendSMSCode(@Query("phone") String phone);

    /**
     * 意见反馈
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("system/feedback")
    Call<Result> feedback(@Query("title") String title, @Query("content") String content, @Query("remark") String remark);

    /**
     * 检查应用是否需要更新
     *
     * @return
     */
    @GET("system/version/check")
    Observable<Result<UpdateAppBean>> checkAppUpdate();

    /**
     * 开发者登录-识别二维码
     *
     * @param code
     * @return
     */
    @GET("qrConfirm")
    Observable<Result> sendCode(@Query("code") String code);

    /**
     * 获取广告
     *
     * @return
     */
    @GET("system/ad")
    Observable<Result<AdBean>> getAd();

}
