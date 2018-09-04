package com.leyou.game.util.api;

import com.leyou.game.bean.FightGameBean;
import com.leyou.game.bean.ResultArray;

import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : com.leyou.game.util.api
 *
 * @author : rocky
 * @Create Time : 2017/7/19 上午9:58
 * @Modified Time : 2017/7/19 上午9:58
 */
public interface FightGameApi {
    /**
     * 获取对战游戏列表
     * 接口1.1
     *
     * @param page
     * @param size
     * @return
     */

    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=interactGameList")
    Observable<ResultArray<FightGameBean>> getFightGameList(@Query("pageNum") int page, @Query("pageSize") int size);

}
