package com.leyou.game.util.newapi;

import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.diamond.DiamondBean;
import com.leyou.game.bean.diamond.DiamondExchangeBean;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : com.leyou.game.util.newapi
 *
 * @author : rocky
 * @Create Time : 2017/10/27 上午11:26
 * @Modified Time : 2017/10/27 上午11:26
 */
public interface DiamondApi {
    /**
     * 查询用户钻石消费记录
     *
     * @param page
     * @param size
     * @param type 消费类型
     * @return
     */
    @GET("diamonds/expend/record")
    Observable<ResultArray<DiamondBean>> getDiamondConsumeRecordList(@Query("page") int page, @Query("size") int size, @Query("type") int type);

    /**
     * 查询钻石消费记录类表类型
     *
     * @return
     */
    @GET("diamonds/expend/record/type")
    Observable<ResultArray<DiamondBean>> getDiamondConsumeRecordType();


    /**
     * 查询钻石平均交易价格
     *
     * @return
     */
    @GET("diamonds/price")
    Observable<Result<DiamondBean>> getDiamondPrice();


    /**
     * 查询钻石手续费
     *
     * @return
     */
    @GET("diamonds/feeDes")
    Observable<Result> getExChangeFee();

    /**
     * 交易所-收购-出售钻石
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("diamonds/purchase")
    Observable<Result> purchase(@Query("diamondsNum") int diamondNum, @Query("saleId") int saleId);

    /**
     * 交易所-收购-出售钻石
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("diamonds/sale")
    Observable<Result> sale(@Query("diamondsNum") int diamondNum, @Query("money") double money);

    /**
     * 交易所-出售-出售列表
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("diamonds/sale/list")
    Observable<ResultArray<DiamondExchangeBean>> getSaleList(@Query("min") int min, @Query("max") int max, @Query("page") int page, @Query("size") int size);

    /**
     * 交易所-收购-收购列表
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("diamonds/takeover/list")
    Observable<ResultArray<DiamondExchangeBean>> getPurchaseList(@Query("page") int page, @Query("size") int size);

}
