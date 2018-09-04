package com.leyou.game.util.api;

import com.leyou.game.bean.AdBean;
import com.leyou.game.bean.GameBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.UpdateAppBean;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : 应用相关的接口Api
 *
 * @author : rocky
 * @Create Time : 2017/5/15 上午10:06
 * @Modified Time : 2017/5/15 上午10:06
 */
public interface AppApi {
    /**
     * 应用检查更新
     *
     * @param version
     * @return
     */
    @GET("clientManager.do?method=versionCheck")
    Observable<Result<UpdateAppBean>> checkUpgrade(@Query("versionCode") int version);

    /**
     * 更新个推id
     *
     * @param clientId
     * @return
     */
    @POST("userInfo.do?method=updateClientId")
    Observable<Result> updateClientId(@Query("clientId") String clientId);


    /**
     * 客户端扫描登录
     *
     * @param code
     * @return
     */
    @POST("userInfo.do?method=developConfirm")
    Observable<Result> scanConfirm(@Query("code") String code);

    /**
     * 获取广告
     *
     * @return
     */
    @POST("advertisement.do?method=getAdvertisement")
    Observable<Result<AdBean>> getAd();
}
