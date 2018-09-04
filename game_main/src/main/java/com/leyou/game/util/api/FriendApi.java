package com.leyou.game.util.api;

import com.leyou.game.bean.ContactBean;
import com.leyou.game.bean.ContactMap;
import com.leyou.game.bean.GameBean;
import com.leyou.game.bean.GameRankBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;

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
 * @Create Time : 2017/7/24 下午5:04
 * @Modified Time : 2017/7/24 下午5:04
 */
public interface FriendApi {
    /**
     * 查看单个好友基本信息
     * 接口1.1
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("userInfo.do?method=queryFriendBaseInfo")
    Observable<Result<Friend>> getFriendInfo(@Query("userId") String userId);

    /**
     * 更新好友信息列表
     * 接口1.2
     *
     * @param map 通讯录数据
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userInfo.do?method=queryFriendList")
    Observable<ResultArray<Friend>> updateFriendList(@Body ContactMap map);

    /**
     * 修改好友备注
     * 接口1.3
     *
     * @param remark 昵称
     * @param userId
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("userInfo.do?method=editFriendRemark")
    Observable<Result> editRemark(@Query("remark") String remark, @Query("userId") String userId);

    /**
     * 添加好友和邀请好友（非本平台用户将发送邀请下载App的短信）
     * 接口1.4
     *
     * @param phone 手机号
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("userInfo.do?method=addFriend")
    Observable<Result> inviteFriend(@Query("phone") String phone);

    /**
     * 确认是否为添加好友
     * 接口1.5
     *
     * @param userId
     * @param type   0拒绝1.同意
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("userInfo.do?method=agreeAddFriend")
    Observable<Result> confirm(@Query("userId") String userId, @Query("type") int type);

    /**
     * 查询是否有人添加自己为好友
     * 接口1.6
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("userInfo.do?method=confirmAddFriend")
    Observable<ResultArray<Friend>> getConfirmFriend();

    /**
     * 查看好友玩过的游戏
     * 接口1.7
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("userInfo.do?method=queryFriendGames")
    Observable<ResultArray<GameBean>> getFriendPlayedGames(@Query("userId") String userId);

    /**
     * 查询我的好友
     * 接口1.8
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userInfo.do?method=findMyFriend")
    Observable<ResultArray<Friend>> getMyFriends();

    /**
     * 删除好友
     * 接口1.9
     *
     * @param userId
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userInfo.do?method=deleteFriend")
    Observable<Result> deleteFriend(@Query("userId") String userId);

    /**
     * 好友游戏排行榜
     * 接口1.10
     *
     * @param gameMark
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userInfo.do?method=runGameRanking")
    Observable<ResultArray<GameRankBean>> getFriendRankByMarkId(@Query("gameMark") long gameMark);

    /**
     * 通过userId添加好友
     * 接口1.11
     *
     * @param userId
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userInfo.do?method= addFriendById")
    Observable<Result> addFriendByUserId(@Query("userId") String userId);

    /***************************************群聊相关接口*******************************************/

    /**
     * 创建群聊
     * 接口2.1
     *
     * @param userIds
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userInfo.do?method=addCrowd")
    Observable<Result<Crowd>> createCrowd(@Query("userIds") String userIds);

    /**
     * 邀请群聊
     * 接口2.2
     *
     * @param userIds
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("userInfo.do?method=inviteToCrowd")
    Observable<Result> inviteFriendToCrowd(@Query("userIds") String userIds, @Query("crowdId") String crowdId);

    /**
     * 退出群聊
     * 接口2.3
     *
     * @param userIds
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("userInfo.do?method=kickOutToCrowd")
    Observable<Result> quitCrowd(@Query("userIds") String userIds, @Query("crowdId") String crowdId);

    /**
     * 查看群聊成员
     * 接口2.4
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("userInfo.do?method=queryCrowdMember")
    Observable<ResultArray<Friend>> getCrowdMember(@Query("crowdId") String crowdId);

    /**
     * 查看群聊信息
     * 接口2.5
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("userInfo.do?method=queryCrowdInfo")
    Observable<Result<Crowd>> getCrowdInfo(@Query("crowdId") String crowdId);

    /**
     * 修改群聊信息
     * 接口2.6
     *
     * @param crowdId       群id
     * @param listenMessage 0不接收消息1.接收消息
     * @param top           0不置顶1.置顶
     * @param myName        我在群聊的昵称
     * @param crowdName     群昵称
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("userInfo.do?method=EditCrowdInfo")
    Observable<Result> editCrowdInfo(@Query("crowdId") String crowdId, @Query("listenMessage") int listenMessage, @Query("top") int top, @Query("myName") String myName, @Query("crowdName") String crowdName);

    /**
     * 解散群聊
     * 接口2.7
     *
     * @param crowdId 群id
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userInfo.do?method=deleteCrowd")
    Observable<Result> deleteCrowd(@Query("crowdId") String crowdId);

    /**
     * 设置群聊置顶
     * 接口2.12
     *
     * @param crowdId 群id
     * @param top     0不置顶    1.置顶
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userInfo.do?method=groupIsTop")
    Observable<Result> setCrowdTop(@Query("crowdId") String crowdId, @Query("top") int top);
}
