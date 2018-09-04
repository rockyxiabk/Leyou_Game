package com.leyou.game.util.newapi;

import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.friend.FriendExtBean;
import com.leyou.game.dao.Friend;
import com.leyou.game.dao.PhoneContact;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : 好友相关接口API
 *
 * @author : rocky
 * @Create Time : 2017/10/30 下午4:26
 * @Modified By: rocky
 * @Modified Time : 2017/10/30 下午4:26
 */

public interface FriendApi {

    /**
     * 添加好友和邀请好友（非本平台用户将发送邀请下载App的短信）
     *
     * @param phone 手机号
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("friend/addFriendByPhone")
    Observable<Result> addFriend(@Query("phone") String phone);

    /**
     * 通过userId添加好友
     *
     * @param friendUserId
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("friend/addFriendByUserId")
    Observable<Result> addFriendByUserId(@Query("friendUserId") String friendUserId);

    /**
     * 确认是否为添加好友
     *
     * @param friendUserId
     * @param isAgree      0拒绝1.同意
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("friend/confirmAgreeFriend")
    Observable<Result> confirm(@Query("friendUserId") String friendUserId, @Query("isAgree") int isAgree);

    /**
     * 删除好友
     *
     * @param friendUserId
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("friend/deleteByFriendUserId")
    Observable<Result> deleteFriend(@Query("friendUserId") String friendUserId);

    /**
     * 查看单个好友信息
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("friend/getFriendInfoByUserId")
    Observable<Result<Friend>> getFriendInfo(@Query("friendUserId") String friendUserId);

    /**
     * 推荐好友列表
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("friend/getRecFriendList")
    Observable<ResultArray<Friend>> getRecFriendList();

    /**
     * 根据id号查询好友
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("friend/getFriendInfoByIdNo")
    Observable<ResultArray<Friend>> getFriendInfoByIdNo(@Query("idNo") String idNo);

    /**
     * 查询添加为好友
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("friend/getMyNewFriendList")
    Observable<ResultArray<PhoneContact>> getNewFriend(@Query("page") int page, @Query("size") int size);

    /**
     * 查询添加的好友个数
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("friend/getNewFriendNum")
    Observable<Result<FriendExtBean>> getWattingFriendNum();

    /**
     * 获取我的好友列表
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("friend/getMyFriendList")
    Observable<ResultArray<Friend>> getMyFriendList();


    /**
     * 获取我的通讯录信息列表
     *
     * @param list 通讯录数据
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("friend/getMyPhoneBookList")
    Observable<ResultArray<PhoneContact>> updateFriendList(@Body List<PhoneContact> list);

    /**
     * 获取潜在好友列表
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("friend/getPotentialFriends")
    Observable<ResultArray<Friend>> getPotentialFriends();

    /**
     * 同步通讯录信息
     *
     * @param list 通讯录数据
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("friend/synPhoneBook")
    Observable<Result> syncPhoneBook(@Body List<PhoneContact> list);

    /**
     * 修改好友备注
     *
     * @param friendUserId
     * @param comment
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("friend/updFriendComment")
    Observable<Result<Friend>> editFriendComment(@Query("friendUserId") String friendUserId, @Query("comment") String comment);
}
