package com.leyou.game.util.newapi;

import com.leyou.game.bean.MessageBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.user.BankCardBean;
import com.leyou.game.bean.user.SignBean;
import com.leyou.game.bean.user.UploadUserInfoBean;
import com.leyou.game.bean.user.UserExtBean;
import com.leyou.game.bean.user.WithCashNoteBean;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : 用户相关信息的API接口
 *
 * @author : rocky
 * @Create Time : 2017/10/26 下午3:55
 * @Modified By: rocky
 * @Modified Time : 2017/10/26 下午3:55
 */

public interface UserApi {

    /**
     * 用户申请绑定银行卡
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("user/card/bind")
    Observable<Result> bindCard(@Query("realName") String cardholderName, @Query("cardNum") String cardNum, @Query("bank") String bankName);

    /**
     * 用户银行卡信息
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("user/card/info")
    Observable<Result<BankCardBean>> getBindCard();

    /**
     * 用户申请
     *
     * @param money
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("user/cash/exchange")
    Observable<Result> applyCash(@Query("money") double money);

    /**
     * 用户申请体现历史记录
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("user/cash/history")
    Observable<ResultArray<WithCashNoteBean>> getWithCashHistory(@Query("page") int pageNum, @Query("size") int pageSize);

    /**
     * 验证用户手机号
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("user/check/mobile")
    Observable<Result> checkUserMobile(@Query("mobile") String mobile, @Query("code") String code);

    /**
     * 获取用户中奖信息未读数量
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("user/getWinMsgUnReadNum")
    Observable<Result<UserExtBean>> getUserUnReadNum();

    /**
     * 获取用户相关信息
     *
     * @return
     */
    @GET("user/info")
    Observable<Result<UserData.UserInfo>> getUserInfo();

    /**
     * 更新用户中奖信息未读信息标记为已读
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("user/updWinMsgStatus")
    Observable<Result> changeUnReadState();

    /**
     * 用户登陆或者注册
     *
     * @param phone
     * @param code
     * @param getuiClientId
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("user/login")
    Observable<Result<UserData.UserInfo>> logIn(@Query("mobile") String phone, @Query("code") String code, @Query("getuiClientId") String getuiClientId);

    /**
     * 用户登陆或者注册(自动验证)
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("user/login/auto")
    Observable<Result<UserData.UserInfo>> autoLogIn();

    /**
     * 用户财富查询
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("user/property")
    Observable<Result<UserData.UserInfo>> getUserWealth();

    /**
     * 用户签到
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("user/sign")
    Observable<Result> sign();

    /**
     * 获取用户签到钻石和连续天数
     *
     * @return
     */
    @GET("user/sign/info")
    Observable<Result<SignBean>> getSignInfo();

    /**
     * 修改用户相关信息
     *
     * @return
     */
    @POST("user/update")
    Observable<Result<UserData.UserInfo>> updateUserInfo(@Body UploadUserInfoBean uploadUserInfoBean);

    /**
     * 修改用户手机号
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("user/update/mobile")
    Observable<Result> upgradeUserMobile(@Query("mobile") String mobile, @Query("code") String code);

    /**
     * 获取消息列表
     *
     * @return
     */
    @GET("message/list")
    Observable<ResultArray<MessageBean>> getMessageList(@Query("page") int pageNum, @Query("size") int pageSize);

    /**
     * 修改消息列表中奖信息为已读状态
     *
     * @param winId 消息id
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("message/updReadByWinId")
    Observable<Result<MessageBean>> editMessageState(@Query("winId") int winId);
}
