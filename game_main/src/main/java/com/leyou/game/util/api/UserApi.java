package com.leyou.game.util.api;

import com.leyou.game.bean.BankCardInfoBean;
import com.leyou.game.bean.AwardAddressBean;
import com.leyou.game.bean.AwardInfoBean;
import com.leyou.game.bean.ConsumeBean;
import com.leyou.game.bean.DeveloperStateBean;
import com.leyou.game.bean.FeedBackInfo;
import com.leyou.game.bean.GameBean;
import com.leyou.game.bean.MarkerPriceBean;
import com.leyou.game.bean.MessageBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.SignBean;
import com.leyou.game.bean.UserData;

import retrofit2.Call;
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
 * @Create Time : 2017/4/12 上午9:42
 * @Modified Time : 2017/4/12 上午9:42
 */
public interface UserApi {

    /**
     * 用户验证登陆信息
     *
     * @param phone
     * @param deviceId
     * @return
     */
    @GET("userInfo.do?method=checkLogin")
    Observable<Result<String>> verifyLogIn(@Query("phone") String phone, @Query("deviceID") String deviceId);

    /**
     * 用户登陆或者注册
     *
     * @param phone
     * @return
     */
    @GET("userInfo.do?method=login")
    Observable<Result<UserData.UserInfo>> logIn(@Query("phone") String phone, @Query("deviceID") String deviceId, @Query("clientId") String clientId);

    /**
     * 获取用户相关信息
     *
     * @return
     */
    @GET("userInfo.do?method=updatedInfo")
    Observable<Result<UserData.UserInfo>> getUserInfo();

    /**
     * 获取用户签到钻石和连续天数
     *
     * @return
     */
    @GET("userInfo.do?method=toSign")
    Observable<Result<SignBean>> getSignDiamond();

    /**
     * 用户签到
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userInfo.do?method=sign")
    Observable<Result<SignBean>> toSign();

    /**
     * 获取赢大奖游戏列表
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("game.do?method=recommend")
    Observable<ResultArray<GameBean>> getRecommendGame();

    /**
     * 用户出售钻石
     * 2.3.3
     *
     * @param price         钻石单价
     * @param virtualNumber 要出售的钻石个数
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("bourse.do?method=add")
    Observable<Result> sale(@Query("price") double price, @Query("virtualCoin") int virtualNumber);

    /**
     * 修改用户相关信息
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userInfo.do?method=edit")
    Observable<Result<UserData.UserInfo>> upgradeUserInfo(@Body UserData.UserInfo userInfo);

    /**
     * 用户退出登陆
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userInfo.do?method=delete")
    Observable<Result<String>> exit();

    /**
     * 获取用户钻石和现金信息
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userInfo.do?method=treasure")
    Observable<Result<UserData.UserInfo>> getTreasure();

    /**
     * 用户申请成为开发者 进度查询
     *
     * @return
     */
    @GET("userInfo.do?method=applicationResult")
    Observable<Result<DeveloperStateBean>> getDeveloperState();

    /**
     * 用户申请绑定银行卡
     * 2.13.1
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("withdrawal.do?method=addCashAccount")
    Observable<Result> bindCard(@Query("cardholderName") String cardholderName, @Query("cardNum") String cardNum, @Query("bankName") String bankName);

    /**
     * 获取绑定的银行卡信息
     * 2.13.2
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("withdrawal.do?method=cashAccount")
    Observable<Result<BankCardInfoBean>> getBindCardInfo();

    /**
     * 用户申请提现
     * 2.13.3
     *
     * @param money
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("withdrawal.do?method=withdrawRequest")
    Observable<Result> applyCash(@Query("money") double money);

    /**
     * 用户提交反馈信息
     *
     * @param feedBackInfo
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("feedback.do?method=add")
    Call<Result<String>> feedBack(@Body FeedBackInfo feedBackInfo);

    /**
     * 查询获奖信息列表
     *
     * @return
     */
    @GET("information.do?method=newInfoCount")
    Observable<Result> checkUnReadMessage();

    /**
     * 获取消息列表
     *
     * @return
     */
    @GET("information.do?method=index")
    Observable<ResultArray<MessageBean>> getMessageList(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    /**
     * 获取消息详情
     *
     * @param id       消息id
     * @param infoType 消息类型
     * @return
     */
    @POST("information.do?method=detail")
    Observable<Result<MessageBean>> getMessageDetail(@Query("informationId") String id, @Query("infoType") int infoType);

    /**
     * 用户申请提现
     * 5.17
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userPrize.do?method=getNewPrizeList")
    Observable<Result> getPrizeDynamic();

    /**
     * 奖品信息及发货信息（实物或者虚拟）
     * 5.19
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userPrize.do?method=getPrizeById")
    Observable<Result<AwardInfoBean>> getMessagePrizeDetail(@Query("id") String id);

    /**
     * 查询获奖信息列表
     *
     * @return
     */
    @GET("userPrize.do?method=getPrizeList")
    Observable<ResultArray<AwardInfoBean>> getAwardList();

    /**
     * 提交用户获奖地址
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userPrize.do?method=save")
    Observable<Result> commitAwardAddress(@Body AwardAddressBean addressBean);

    /**
     * 用户消费记录
     * 5.31
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("statistics.do?method=spendingMemo")
    Observable<ResultArray<ConsumeBean>> getConsumeList(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    /**
     * 市场价格区间
     * 5.32
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("statistics.do?method=marketPrice")
    Observable<Result<MarkerPriceBean>> marketPrice();
}
