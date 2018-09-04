package com.leyou.game.util.api;

import com.leyou.game.bean.ExChangeBuyBean;
import com.leyou.game.bean.ExChangeSaleBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.TradeBean;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : 交易相关Api接口
 *
 * @author : rocky
 * @Create Time : 2017/4/25 上午11:32
 * @Modified Time : 2017/4/25 上午11:32
 */
public interface TradeApi {
    /**
     * 获取交易所列表
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("bourse.do?method=index")
    Observable<ResultArray<ExChangeSaleBean>> getExChangeList(@Query("pageNum") int page, @Query("pageSize") int size, @Query("maxVirtualCoin") int maxCoin, @Query("minVirtualCoin") int minCoin);

    /**
     * 获取交易所列表
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("bourse.do?method=buyIndex")
    Observable<ResultArray<ExChangeBuyBean>> getExChangeBuyList(@Query("pageNum") int page, @Query("pageSize") int size);

    /**
     * 获取交易所列表
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("bourse.do?method=buy")
    Observable<Result> purcharsSell(@Query("virtualCoin") int number, @Query("id") String id);

    /**
     * 获取交易所购买钻石
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("consumption.do?method=add")
    Observable<Result<String>> exchangeBuy(@Body ExChangeSaleBean exChangeBean);

    /**
     * 钻石充值，支付成功后调用该接口
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("consumption.do?method=add")
    Observable<Result<String>> addVerify(@Query("sign") String sign, @Query("type") int type);

    /**
     * 个人交易买入记录
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("consumption.do?method=buyRecords")
    Observable<ResultArray<TradeBean>> getBuyRecords();

    /**
     * 个人交易卖出记录
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("consumption.do?method=sellRecords")
    Observable<ResultArray<TradeBean>> getSellRecords();
}
