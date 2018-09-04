package com.leyou.game.util.api;

import com.leyou.game.bean.treasure.PropBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.treasure.TreasureBean;
import com.leyou.game.bean.treasure.TreasureExtBean;
import com.leyou.game.bean.treasure.TreasureGainBean;
import com.leyou.game.bean.treasure.WorkerBean;
import com.leyou.game.bean.treasure.WorkerExtBean;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : 宝窟相关接口
 *
 * @author : rocky
 * @Create Time : 2017/5/4 上午9:34
 * @Modified Time : 2017/5/4 上午9:34
 */
public interface TreasureApi {

/*********************************************************以下是宝窟矿工相关**************************************************************/
    /**
     * 获取用户所有矿工
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userActive.do?method=queryWorkerNumber")
    Observable<Result<WorkerExtBean>> getUserWorkerCount();

    /**
     * 获取用户所有矿工
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("worker.do?method=findByUserId")
    Observable<ResultArray<WorkerBean>> getUserWorker();

    /**
     * 制定查询用户矿工
     * 参数查询：0白色，1橙色，2红色，3蓝色，4紫色
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("worker.do?method=findByUserId")
    Observable<ResultArray<WorkerBean>> getUserWorker(@Query("typeId") int typeId);

    /**
     * 查找用户可出战矿工
     *
     * @param type
     * @param level
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("worker.do?method=findCombatWorkers")
    Observable<ResultArray<WorkerBean>> getUserCanFightWorker(@Query("type") int type, @Query("level") int level);

    /**
     * 获取用户可雇佣的矿工
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("worker.do?method=hireWorkerList")
    Observable<ResultArray<WorkerBean>> getUserCanEmployWorker();

    /**
     * 钻石刷新可雇用的矿工
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("worker.do?method=virtualCoinRefreshWorker")
    Observable<ResultArray<WorkerBean>> getUserCanEmployWorkerBuyRefresh(@Query("virtualCoin") int coin);

    /**
     * 获取用户可雇佣的矿工
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("worker.do?method=queryWorkerRefreshTime")
    Observable<Result<WorkerExtBean>> getCanEmployRefreshTime();

    /**
     * 解雇矿工
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("worker.do?method=delete")
    Observable<Result<String>> dismissWorker(@Query("id") String id);

    /**
     * 合成矿工
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("worker.do?method=merge")
    Observable<Result<WorkerExtBean>> composeWorker(@Query("workerId1") String id1, @Query("workerId2") String id2);

    /**
     * 升级矿工
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("worker.do?method=update")
    Observable<Result<WorkerExtBean>> upgradeWorker(@Query("workerId") String workerId);

    /**
     * 一键升级矿工
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("worker.do?method=update")
    Observable<Result<WorkerExtBean>> upgradeWorker(@Query("workerId") String workerId, @Query("fast") boolean flag);

    /**
     * 雇佣矿工
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("worker.do?method=add")
    Observable<Result<WorkerExtBean>> employWorker(@Query("typeId") int typeId, @Query("typeName") String typeName, @Query("attribute") int attribute);

    /**
     * 增加矿工工位
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userActive.do?method=addUpperLimit")
    Observable<Result<String>> addWorkerPlaceCount();


/***********************************************************以下是宝窟相关****************************************************************/

    /**
     * 宝窟激活状态
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userActive.do?method=queryActiveState")
    Observable<Result> getTreasureActivateState();

    /**
     * 激活宝窟
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userActive.do?method=doUserActive")
    Observable<Result> activate();

    /**
     * 刷新宝窟
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("treasure.do?method=refreshTreasure")
    Observable<Result<TreasureBean>> refreshTreasure();

    /**
     * 碎钻石兑换
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userActive.do?method=exchange")
    Observable<Result> convertChips(@Query("chipInt") int chipsInt);

    /**
     * 查询碎钻
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("userActive.do?method=queryChips")
    Observable<Result<TreasureExtBean>> queryChips();

    /**
     * 查询用户当前拥有的宝窟
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("treasure.do?method=queryTreasures")
    Observable<ResultArray<TreasureBean>> queryTreasures();

    /**
     * 查询当前宝窟可收获碎钻个数
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("treasure.do?method=queryChips")
    Observable<Result<TreasureExtBean>> queryCanHarvestChips(@Query("treasureId") String treasureId);

    /**
     * 收获碎钻
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("treasure.do?method=addChips")
    Observable<Result> harvestChips(@Query("treasureId") String treasureId);

    /**
     * 查询宝库产出的道具（4级5级宝库才会调用此接口）
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("treasure.do?method=queryItem")
    Observable<ResultArray<PropBean>> getCanHarvestProp(@Query("Id") String id);

    /**
     * 废除自己的宝窟
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("treasure.do?method=deleteTreasure")
    Observable<Result> deleteTreasure(@Query("treasureId") String treasureId);


    /**
     * 占领无人宝窟
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("treasure.do?method=add")
    Observable<Result<String>> getPlaceNoWorkerTreasure(@Query("workerId") String workerId);

    /**
     * 抢占有人宝窟
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @POST("treasure.do?method=plunder")
    Observable<Result<TreasureExtBean>> getPlaceHasWorkerTreasure(@Query("id") String id, @Query("workerId") String workerId);

    /**
     * 修改当前宝窟矿工
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("treasure.do?method=sendWorker")
    Observable<Result> modifyTreasureWorker(@Query("treasureId") String id, @Query("workerId") String workerId);

    /**
     * 查找当前宝窟的矿工
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("treasure.do?method=queryWorker")
    Observable<ResultArray<WorkerBean>> getTreasureWorker(@Query("treasureId") String id);

    /**
     * 查找当前宝窟的矿工
     *
     * @return
     */
    @Headers({
            "Content-Type:text/plain;charset=UTF-8"
    })
    @GET("treasure.do?method=queryWorkerHeader")
    Observable<ResultArray<WorkerBean>> getCurrentTreasureWorker(@Query("treasureId") String id);

    /**
     * 攻占宝库 返回的档期那宝库
     *
     * @return
     */
    @GET("treasure.do?method=occupyTreasure")
    Observable<Result<TreasureBean>> getRaceTreasure(@Query("workerId") String workerId);

    /**
     * 获取宝库采矿排行
     * 获前10名的数据
     *
     * @return
     */
    @GET("treasure/getTreasureDiamonds")
    Observable<ResultArray<TreasureGainBean>> getTreasureRank();

    /**
     * 获取宝库采矿排行
     * 获前10名的除去抢两名的数据
     *
     * @return
     */
    @GET("treasure/getTreasureDiamonds/5")
    Observable<ResultArray<TreasureGainBean>> getTreasureRank1();

    /**
     * 获取宝库采矿排行
     * 获取前两名
     *
     * @return
     */
    @GET("treasure/getTreasureDiamonds/2")
    Observable<ResultArray<TreasureGainBean>> getTreasureRank2();

    /**************************************************宝窟商城相关**************************************************/

    /**
     * 购买道具
     *
     * @param itemId 道具id
     * @param number 要购买的数量
     * @param type   0碎钻购买1.钻石购买
     * @return
     */
    @GET("userItemController.do?method=buyItem")
    Observable<Result> buyProp(@Query("itemId") String itemId, @Query("number") int number, @Query("type") int type);

    /**
     * 查看用户拥有的道具
     *
     * @param type 0.所有道具1.回复体力道具，升星道具2.寻找宝窟道具
     * @param sort 0.价格从高到低1.价格从低到高
     * @return
     */
    @GET("userItemController.do?method=queryUserItem")
    Observable<ResultArray<PropBean>> getUserProp(@Query("type") int type, @Query("sort") int sort);


    /**
     * 查看商城的道具
     *
     * @param type 0.所有道具1.回复体力道具2.寻找宝窟道具3.升星道具
     * @param sort 0.价格从高到低1.价格从低到高
     * @return
     */
    @GET("treasureItemController.do?method=queryShopItem")
    Observable<ResultArray<PropBean>> getShopProp(@Query("type") int type, @Query("sort") int sort);

    /**
     * 查看商城的道具
     *
     * @param id       道具id
     * @param type     0．矿工升星1.回复体力道具2.寻找宝窟道具
     * @param workerId 矿工id type为0和1时此字段必填
     * @return
     */
    @GET("userItemController.do?method=userItem")
    Observable<Result> useProp(@Query("Id") String id, @Query("type") int type, @Query("workerId") String workerId);
}
