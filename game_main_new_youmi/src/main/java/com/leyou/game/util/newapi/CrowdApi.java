package com.leyou.game.util.newapi;

import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : com.leyou.game.util.newapi
 *
 * @author : rocky
 * @Create Time : 2017/10/31 下午3:32
 * @Modified Time : 2017/10/31 下午3:32
 */
public interface CrowdApi {


    /**
     * 申请加入群聊
     *
     * @param groupId
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("group/applyJoin")
    Observable<Result> applyJoin(@Query("groupId") String groupId);

    /**
     * 获取群申请列表
     *
     * @param page
     * @param size
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("group/applyList")
    Observable<ResultArray<Crowd>> applyList(@Query("page") int page, @Query("size") int size);

    /**
     * 创建群聊 含有群信息
     *
     * @param list
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("group/create")
    Observable<Result<Crowd>> createCrowd(@Body List<Friend> list,
                                          @Query("groupImg") String groupImg,
                                          @Query("groupName") String groupName,
                                          @Query("introduction") String introduction);

    /**
     * 修改群聊资料
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("group/updGroupInfo")
    Observable<Result> updateCrowdInfo(
            @Query("groupId") String groupId,
            @Query("groupImg") String groupImg,
            @Query("groupName") String groupName,
            @Query("introduction") String introduction);

    /**
     * 邀请加入群聊
     *
     * @param list
     * @param groupId
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("group/invite")
    Observable<Result> inviteFriendToGroup(@Body List<Friend> list, @Query("groupId") String groupId);

    /**
     * 移除群聊
     *
     * @param list
     * @param groupId
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("group/remove")
    Observable<Result> removeGroup(@Body List<Friend> list, @Query("groupId") String groupId);

    /**
     * 退出群聊
     *
     * @param groupId
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("group/exit")
    Observable<Result> exitGroup(@Query("groupId") String groupId);

    /**
     * 获取推荐群
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("group/getRecGroupList")
    Observable<ResultArray<Crowd>> getRecGroupList();

    /**
     * 查找群
     *
     * @param groupNo
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("group/getGroupInfoByGroupNo")
    Observable<ResultArray<Crowd>> getGroupInfoByGroupNo(@Query("groupNo") String groupNo);

    /**
     * 查看群聊成员
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("group/getGroupMembers")
    Observable<ResultArray<Friend>> getGroupMember(@Query("groupId") String groupId);

    /**
     * 查看群聊信息
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("group/detail")
    Observable<Result<Crowd>> getGroupDetail(@Query("groupId") String groupId);

    /**
     * 修改群聊信息
     *
     * @param groupId 群id
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("group/editGroupName")
    Observable<Result> editGroupName(@Query("value") String value, @Query("groupId") String groupId);

    /**
     * 群组 操作是否同意加入群聊 同意
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("group/join/agree")
    Observable<Result> agreeCrowd(@Query("applyId") String applyId);

    /**
     * 群组 操作是否同意加入群聊 忽略
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("group/join/ignore")
    Observable<Result> ignoreCrowd(@Query("applyId") String applyId);

    /**
     * 修改群聊信息
     *
     * @param groupId 群id
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("group/editMyComment")
    Observable<Result> editMyComment(@Query("value") String value, @Query("groupId") String groupId);


    /**
     * 修改群聊信息
     *
     * @param groupId 群id
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("group/editIsTop")
    Observable<Result> editGroupTop(@Query("value") int value, @Query("groupId") String groupId);


    /**
     * 修改群聊信息
     *
     * @param groupId 群id
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("group/editIsShielding")
    Observable<Result> editGroupShielding(@Query("value") int value, @Query("groupId") String groupId);

}
