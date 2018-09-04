package com.leyou.game.util.api;

import com.leyou.game.bean.AliPayOrderInfoBean;
import com.leyou.game.bean.GameBean;
import com.leyou.game.bean.OrderInfoBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.WXOrderInfoBean;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : com.leyou.game.util.api
 *
 * @author : rocky
 * @Create Time : 2017/5/16 下午4:01
 * @Modified Time : 2017/5/16 下午4:01
 */
public interface PayApi {
    /**
     * 原生支付创建订单
     * 微信
     *
     * @param order
     * @return
     */
    @POST("consumption.do?method=add")
    Observable<Result<WXOrderInfoBean>> generatorOrder_wx(@Body OrderInfoBean order);

    /**
     * 原生支付创建订单
     * 支付宝
     *
     * @param order
     * @return
     */
    @POST("consumption.do?method=add")
    Observable<Result<AliPayOrderInfoBean>> generatorOrder_alipay(@Body OrderInfoBean order);

    /**
     * 原生支付创建订单
     * 余额支付
     *
     * @param order
     * @return
     */
    @POST("consumption.do?method=add")
    Observable<Result<AliPayOrderInfoBean>> generatorOrder_yue(@Body OrderInfoBean order);

    /**
     * 查询订单支付结果
     *
     * @param orderNo
     * @return
     */
    @GET("orderResult.do?method=queryOrder")
    Observable<Result> checkOrder(@Query("outTraderNo") String orderNo);

    /**
     * 通知服务端支付失败
     *
     * @param orderNo
     * @return
     */
    @GET("newPay/fail.do")
    Observable<Result> payFailed(@Query("payType") String payType, @Query("orderNo") String orderNo);

    /**
     * 获取赢大奖游戏列表
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("gameConsume.do?method=born")
    Observable<Result> payWinAwardGame(@Query("gameCountId") String gameCountId);
}
