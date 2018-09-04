package com.leyou.game.util.newapi;

import com.leyou.game.bean.game.GameCommentBean;
import com.leyou.game.bean.game.GameExtBean;
import com.leyou.game.bean.game.GameRankBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.bean.game.GameWinPriseBean;
import com.leyou.game.bean.win.WinGameAwardBean;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : 游戏相关的API接口
 *
 * @author : rocky
 * @Create Time : 2017/4/12 上午9:42
 * @Modified Time : 2017/4/12 上午9:42
 */
public interface GameApi {
    /**
     * 游戏轮播列表
     *
     * @return
     */
    @GET("game/banner")
    Observable<ResultArray<GameBean>> getGameBanner();

    /**
     * 游戏-赢大奖奖励
     *
     * @return
     */
    @GET("game/bonus")
    Observable<ResultArray<WinGameAwardBean>> getGameBonus(@Query("uniqueMark") String uniqueMark);

    /**
     * 游戏-好友排行-赢大奖奖励
     *
     * @return
     */
    @GET("game/bonus/friend")
    Observable<ResultArray<WinGameAwardBean>> getFriendGameBonus(@Query("uniqueMark") String uniqueMark);

    /**
     * 游戏类别列表
     *
     * @return
     */
    @GET("game/category")
    Observable<ResultArray<GameBean>> getCategoryGameList(@Query("categoryId") String categoryId, @Query("page") int page, @Query("size") int size);

    /**
     * 更多游戏分类列表
     *
     * @return
     */
    @GET("game/category/list")
    Observable<ResultArray<GameBean>> getCategoryTypeList();

    /**
     * 游戏详情
     *
     * @param uniqueMark
     * @return
     */
    @GET("game/detail")
    Observable<Result<GameBean>> getGameDetail(@Query("uniqueMark") String uniqueMark);

    /**
     * 游戏评论列表
     *
     * @param uniqueMark
     * @param page
     * @param size
     * @return
     */
    @GET("game/getCommentList")
    Observable<ResultArray<GameCommentBean>> getGameCommentList(@Query("uniqueMark") String uniqueMark, @Query("page") int page, @Query("size") int size);

    /**
     * 我的获奖列表
     *
     * @param page
     * @param size
     * @return
     */
    @GET("game/getMyWinList")
    Observable<ResultArray<GameWinPriseBean>> getMyWinList(@Query("page") int page, @Query("size") int size);


    /**
     * 我的获奖列表-奖品详情
     *
     * @return
     */
    @GET("game/winDetail")
    Observable<Result<GameWinPriseBean>> getPrizeDetail(@Query("id") int id);

    /**
     * 我的获奖列表-奖品详情
     *
     * @return
     */
    @POST("game/updWinInfo")
    Observable<Result> commitPrizeAddress(@Query("winId") int id, @Query("realName") String realName,
                                          @Query("phone") String phone, @Query("address") String address, @Query("email") String email);

    /**
     * 游戏-赢大奖-TAB
     *
     * @return
     */
    @GET("game/getWinGameList")
    Observable<ResultArray<GameBean>> getWinGameTabList();

    /**
     * 游戏-赢大奖-上期获奖名单
     *
     * @param uniqueMark
     * @return
     */
    @GET("game/getlatestWinnerList")
    Observable<ResultArray<GameWinPriseBean>> getLatestWinnerList(@Query("uniqueMark") String uniqueMark);

    /**
     * 游戏列表
     *
     * @param type 游戏类型1赢大奖,2H5对接游戏,3侧边栏游戏
     * @param page
     * @param size
     * @return
     */
    @GET("game/list")
    Observable<ResultArray<GameBean>> getGameList(@Query("type") int type, @Query("page") int page, @Query("size") int size);

    /**
     * 游戏-最新上架
     *
     * @param page
     * @param size
     * @return
     */
    @GET("game/new/list")
    Observable<ResultArray<GameBean>> getNewGameList(@Query("page") int page, @Query("size") int size);


    /**
     * 游戏-推荐
     *
     * @return
     */
    @GET("game/recommendList")
    Observable<ResultArray<GameBean>> getRecommendList();

    /**
     * 游戏-赢大奖-排行
     *
     * @param uniqueMark
     * @return
     */
    @GET("game/rank")
    Observable<ResultArray<GameRankBean>> getGameRankList(@Query("uniqueMark") String uniqueMark);

    /**
     * 游戏-赢大奖-好友排行
     *
     * @param uniqueMark
     * @return
     */
    @GET("game/rank/friends")
    Observable<ResultArray<GameRankBean>> getGameRankFriendsList(@Query("uniqueMark") String uniqueMark);

    /**
     * 游戏-侧边栏游戏
     *
     * @return
     */
    @GET("game/sliderInfo")
    Observable<Result<GameBean>> getGameSliderInfo();


    /**
     * 游戏-开始游戏时-获取游戏次数
     *
     * @param uniqueMark
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("game/start/new")
    Observable<Result<GameExtBean>> getGameCountId(@Query("uniqueMark") String uniqueMark);

    /**
     * 游戏-填写收获地址
     *
     * @param uniqueMark
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("game/sliderInfo")
    Observable<Result> commitAddress(@Query("realName") String realName, @Query("phone") String phone, @Query("address") String address, @Query("winId") int winId, @Query("email") String email, @Query("uniqueMark") String uniqueMark);

    /**
     * 游戏-热词
     *
     * @return
     */
    @GET("game/word/hot")
    Observable<ResultArray<String>> getHotWord();

    /**
     * 游戏-热词-游戏列表
     *
     * @param hotWord
     * @param page
     * @param size
     * @return
     */
    @GET("game/word/search")
    Observable<ResultArray<GameBean>> getGameListByHotWord(@Query("hotWord") String hotWord, @Query("page") int page, @Query("size") int size);

    /**
     * 游戏-我玩过的游戏
     *
     * @return
     */
    @GET("game/myPlayList")
    Observable<ResultArray<GameBean>> getMyPlayedGames();

    /**
     * 游戏-我的好友玩过的游戏
     *
     * @return
     */
    @GET("game/myFriendsPlayList")
    Observable<ResultArray<GameBean>> getMyFriendsPlayedGames(@Query("friendUserId") String friendUserId);

    /**
     * 游戏-其他玩过的游戏
     *
     * @return
     */
    @GET("game/otherPlayList")
    Observable<ResultArray<GameBean>> getOtherPlayedGames();

}
