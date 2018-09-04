package com.leyou.game.util.api;

import com.leyou.game.bean.AwardPersonInfo;
import com.leyou.game.bean.GameBean;
import com.leyou.game.bean.GameExtBean;
import com.leyou.game.bean.GamePrizeResult;
import com.leyou.game.bean.GamePrizeResultPersonInfo;
import com.leyou.game.bean.GameRankBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;


import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : 赢大奖相关API接口
 *
 * @author : rocky
 * @Create Time : 2017/4/13 上午10:22
 * @Modified Time : 2017/4/13 上午10:22
 */
public interface WinAwardApi {
    /**
     * 获取赢大奖游戏列表
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("game.do?method=index")
    Observable<ResultArray<GameBean>> getWinAwardList(@Query("source") String source);

    /**
     * 获取当前游戏打开次数
     *
     * @return
     */
    @GET("gameCount.do?method=getCount")
    Observable<Result<GameExtBean>> getGameTime(@Query("gameInfoMark") long gameInfoMark);

    /**
     * 获取赢大奖游戏排行榜列表
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("gameIntegral.do?method=findByMark")
    Observable<ResultArray<GameRankBean>> getGameRankList(@Query("mark") long markId, @Query("pageNum") int page, @Query("pageSize") int pageSize);

    /**
     * 获取上期获奖列表头
     *
     * @return
     */
    @GET("game.do?method=getGameNameList")
    Observable<ResultArray<GamePrizeResult>> getPrizeResultList();

    /**
     * 获取上期游戏获奖列表
     *
     * @return
     */
    @GET("historyList.do?method=findByMark")
    Observable<ResultArray<AwardPersonInfo>> getPrizeResultPersonList(@Query("mark") long mark);

}
