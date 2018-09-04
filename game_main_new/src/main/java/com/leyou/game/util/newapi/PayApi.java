package com.leyou.game.util.newapi;

import com.leyou.game.bean.Result;
import com.leyou.game.bean.pay.PayBean;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : 支付
 *
 * @author : rocky
 * @Create Time : 2017/5/16 下午4:01
 * @Modified Time : 2017/5/16 下午4:01
 */
public interface PayApi {
    /**
     * 原生支付创建订单
     *
     * @param extraId      扩展信息Id 出售钻石的id号 收购钻石的id号
     * @param goodsName    商品名称
     * @param channel      支付渠道
     * @param source       支付来源 android
     * @param money        支付金额
     * @param payType      支付方式 zs钻石支付 zfb支付宝 wx微信 yue余额
     * @param type         支付类型 记录是哪个地方的消费：1.我页面的充值钻石 2.交易所收购钻石 3.交易所购买别人出售的钻石 4.h5页面购买道具支付
     * @param thirdOrderNo 第三方订单
     * @param userId       支付订单的用户id
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("pay/order")
    Observable<Result<PayBean>> createOrder(@Query("extraId") String extraId, @Query("goodsName") String goodsName,
                                            @Query("channel") String channel, @Query("money") double money, @Query("diamondsNum") int diamondNum,
                                            @Query("payType") String payType, @Query("type") int type,
                                            @Query("thirdOrderNo") String thirdOrderNo, @Query("userId") String userId);

    /**
     * 查询订单支付结果
     *
     * @param orderNo
     * @param payType
     * @return
     */
    @GET("pay/orderResult")
    Observable<Result> checkOrder(@Query("orderNo") String orderNo, @Query("payType") String payType);

}
