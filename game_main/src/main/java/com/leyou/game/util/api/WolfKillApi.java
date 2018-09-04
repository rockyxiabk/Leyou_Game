package com.leyou.game.util.api;

import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.WolfKillMilitaryBean;
import com.leyou.game.bean.WolfKillRoomBean;
import com.leyou.game.bean.WolfKillRoomInfoBean;
import com.leyou.game.bean.WolfPropBean;
import com.leyou.game.bean.WolfRoleBean;

import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Description : com.leyou.game.util.api
 *
 * @author : rocky
 * @Create Time : 2017/7/16 下午4:03
 * @Modified Time : 2017/7/16 下午4:03
 */
public interface WolfKillApi {

    /**
     * 获取狼人杀游戏局数和胜率
     * 接口1.2
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=interactGameDetail")
    Observable<Result<WolfRoleBean>> getUserWinRate();

    /**
     * 获取狼人杀游戏所有角色胜率
     * 接口1.3
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=gameHistoryRoleWinRate")
    Observable<ResultArray<WolfRoleBean>> getRoleWinRate();

    /**
     * 狼人杀游戏战绩
     * 接口1.4
     *
     * @param page
     * @param size
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=gameHistoryList")
    Observable<ResultArray<WolfKillMilitaryBean>> getMilitary(@Query("pageNum") int page, @Query("pageSize") int size);

    /**
     * 狼人杀游戏 创建游戏房间
     * 接口1.5
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=createRoom")
    Observable<Result<WolfKillRoomBean>> createRoom(@Query("type") int type);

    /**
     * 狼人杀游戏 检查游戏房间
     * 接口1.6
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=checkRoomId")
    Observable<Result> checkRoom(@Query("roomId") long roomId);

    /**
     * 狼人杀游戏 游戏房间角色信息
     * 接口1.7
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=findRoomInfo")
    Observable<Result<WolfKillRoomInfoBean>> getRoomInfo(@Query("roomId") String roomId);

    /**
     * 狼人杀游戏 道具商城
     * 接口1.8
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=gameProps")
    Observable<ResultArray<WolfPropBean>> getWolfKillProp(@Query("gameInfoMark") long gameInfoMarkId);

    /**
     * 狼人杀游戏 购买道具
     * 接口1.9
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=buyProps")
    Observable<Result> buyProp(@Query("propsMark") long propsMark, @Query("number") int count);

    /**
     * 狼人杀游戏 获取用户道具
     * 接口1.10
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=userProps")
    Observable<ResultArray<WolfPropBean>> getUserProp();

    /**
     * 狼人杀游戏 游戏房间使用道具争抢游戏角色
     * 接口1.11
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=robRole")
    Observable<Result<WolfKillRoomInfoBean>> snatchRole(@Query("roomId") long roomId, @Query("gamePropsMark") long propMarkId);

    /**
     * 狼人杀游戏 根据选择的游戏类型 自动进入游戏房间
     * 接口1.12
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=autoRoom")
    Observable<Result<WolfKillRoomInfoBean>> autoJoinRoom(@Query("type") int type);

    /**
     * 狼人杀游戏 邀请好友进入同一个房间
     * 接口1.13
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=autoRoom")
    Observable<Result<WolfKillRoomInfoBean>> inviteFriendJoinRoom(@Query("friendUserId") String userId);

    /**
     * 狼人杀游戏 房主移除玩家
     * 接口1.14
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=kickOut")
    Observable<Result> kickRoom(@Query("userId") String userId);

    /**
     * 狼人杀游戏 玩家进入房间 准备开始游戏
     * 接口1.15
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=prepare")
    Observable<Result> playerPrepare();

    /**
     * 狼人杀游戏 游戏开始倒计时
     * 接口1.16
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=countDown")
    Observable<Result<WolfKillRoomInfoBean>> gameStartCountDown();

    /**
     * 狼人杀游戏 狼人杀人
     * 接口1.17
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=werwolf")
    Observable<Result<WolfKillRoomInfoBean>> wolfKillPerpore(@Query("userId") String userId);

    /**
     * 狼人杀游戏 投票表决
     * 接口1.18
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=vote")
    Observable<Result<WolfKillRoomInfoBean>> vote(@Query("userId") String userId);

    /**
     * 狼人杀游戏 预言家验证玩家身份
     * 接口1.19
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=checkUser")
    Observable<Result<WolfKillRoomInfoBean>> checkUserRole(@Query("userId") String userId);

    /**
     * 狼人杀游戏 猎人死亡 带走一名玩家
     * 接口1.20
     *
     * @param type 0猎人杀人，1巫医杀人
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=hunter")
    Observable<Result<WolfKillRoomInfoBean>> hunt(@Query("userId") String userId, @Query("type") int type);

    /**
     * 狼人杀游戏 巫师救人
     * 接口1.21
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=resurgence")
    Observable<Result<WolfKillRoomInfoBean>> savePerson(@Query("userId") String userId);

    /**
     * 狼人杀游戏 守卫
     * 接口1.22
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=protectMan")
    Observable<Result<WolfKillRoomInfoBean>> protectOther(@Query("userId") String userId);

    /**
     * 狼人杀游戏  获取投票结果
     * 接口1.23
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=voteResult")
    Observable<Result<WolfKillRoomInfoBean>> gameVoteResult(@Query("roomId") String roomId);

    /**
     * 玩家退出游戏
     * 接口1.26
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=quitRoom")
    Observable<Result> quitRoom(@Query("roomId") long roomId);

    /**
     * 举报玩家
     * 接口1.27
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("multiPlayer.do?method=inform")
    Observable<Result> report(@Query("userId") String userId);


}
